package edu.ksu.rxecs.core.ecs;


import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import edu.ksu.rxecs.core.Profiling;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.WorkQueueProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.WaitStrategy;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class Engine {

    @Getter
    final ImmutableSet<Class<? extends Component>> components;
    @Getter
    final ImmutableMap<Class<? extends Component>, EntitySystem> componentToOwnerMap;
    @Getter
    private final ImmutableSet<EntitySystem> systems;
    private final MutableBag<Entity> entities = Bags.mutable.empty();
    @Getter
    ImmutableMap<Class<? extends Component>, MutableBag<ChronoBox>> componentToStorageMap;
    private volatile boolean isProcessing;

    private Engine(MutableBag<EntitySystem> systems) {
        this.systems = Sets.immutable.withAll(systems);

        this.components = Sets.immutable.withAll(
                systems.stream().map(sys -> sys.owns()).collect(Collectors.toList())
        );

        this.componentToOwnerMap = Maps.immutable.withAll(
                systems.stream()
                        .collect(Collectors.toUnmodifiableMap(EntitySystem::owns, sys -> sys))
        );

        this.componentToStorageMap = Maps.immutable.withAll(
                systems.stream().collect(Collectors.toMap(sys -> sys.owns(), sys -> Bags.mutable.empty()))
        );

    }

    public static Builder builder() {
        return new Builder();
    }

    private static void throwNewConflictingComponentOwnershipException(EntitySystem system) {
        final String warningString = String.format(
                "Conflicting ownership of component {} when adding system {}",
                system.owns().toString(),
                system.toString()
        );

        log.error(warningString);
        throw new RuntimeException(warningString);
    }

    private static void throwNewUnmanagedComponentAddedException(Entity entity, Component component) {
        final String errorMsg = "Engine attempted to add entity containing a component" +
                " not owned by any system contained in the engine." +
                "Entity: {}\n Component: {}\n";

        log.error(errorMsg, entity.toString(), component.toString());
        throw new RuntimeException(errorMsg);
    }

    public final Flux<Component> observe(Component component) {
        for (ChronoBox box : componentToStorageMap.get(component.getClass())) {
            if (box.now().equals(component)) {
                return Flux.from(box);
            }
        }
        System.out.println("ERROR SUBSCRIBING");
        return Flux.never();
    }

    public final void update(float dt) {
        isProcessing = true;

        final CountDownLatch countDownLatch = new CountDownLatch(Profiling.getAvailableProcessors());

        final Disposable subscription = Flux.fromIterable(components)
                .map(cls -> Tuples.of(componentToOwnerMap.get(cls), componentToStorageMap.get(cls)))
                .parallel(Profiling.getAvailableProcessors())
                .runOn(Schedulers.parallel())
                .doOnComplete(() -> { // this occurs for each
                    countDownLatch.countDown();
                    log.debug("parallel thread completed");
                })
                .subscribe(tuple -> {
                    tuple.getT1().beforeUpdate();
                    tuple.getT2().forEach((Consumer<? super ChronoBox>) box -> {
                        tuple.getT1().update(box.now(), new EntitySnapshot(box.then().entity), dt);
                    });
                    tuple.getT1().afterUpdate();
                });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("InterruptedException thrown when awaiting parallel threads to complete.", e);
        }

        // following the countdown shove returned components into
        componentToStorageMap.forEach((Consumer<? super MutableBag<ChronoBox>>) bag -> {
            bag.forEach((Consumer<? super ChronoBox>) box -> {
                log.debug(box.then() + " -> " + box.now());
                box.advance();
            });
        });

        log.debug("all parallel tasks finished. update cycle completed." + subscription.isDisposed());

        isProcessing = false;
    }

    public final boolean addEntity(MutableEntity entity) {
        synchronized (entities) {
            if (!entities.contains(entity)) {
                entity.getComponents()
                        .subscribeOn(Schedulers.immediate())
                        .subscribe(component -> {
                                    synchronized (componentToStorageMap.get(component.getClass())) {
                                        final MutableBag<ChronoBox> bag = componentToStorageMap.get(component.getClass());
                                        if (bag == null) {
                                            throwNewUnmanagedComponentAddedException(entity, component);
                                        } else {
                                            if (!bag.contains(entity.components.get(component.getClass()))) {
                                                bag.add(new ChronoBox(component));
                                            }
                                        }
                                    }
                                },
                                Throwable::printStackTrace);
                return entities.add(entity);
            } else {
                return false;
            }
        }
    }

    public final void addEntity(Entity... entities) {
        for (Entity entity : entities) {
            addEntity(entity);
        }
    }

    public final boolean removeEntity(Entity entity) {
        synchronized (entities) {
            if (entities.contains(entity)) {
                entity.getComponents().subscribe(component -> componentToStorageMap.get(component.getClass()).remove(component));
                return entities.add(entity);
            } else {
                return false;
            }
        }
    }

    public final boolean containsEntity(Entity entity) {
        synchronized (entities) {
            return entities.contains(entity);
        }
    }

    private WorkQueueProcessor<Tuple2<EntitySystem, MutableBag<Component>>> createNewWorkQueueProcessor() {
        return WorkQueueProcessor.<Tuple2<EntitySystem, MutableBag<Component>>>builder()
//                    .executor(Executors.newCachedThreadPool())  // cache for quick-run tasks
                .executor(Executors.newWorkStealingPool(Profiling.getAvailableProcessors()))
                .bufferSize(Profiling.getAvailableProcessors())
                .waitStrategy(WaitStrategy.busySpin()) // prioritize low-latency
                .share(false) // false because all onNext() called from Engine
                .build();
    }

    public static class Builder {

        private final MutableBag<EntitySystem> systems = Bags.mutable.empty();

        private Builder() {
        }

        public Builder addSystem(EntitySystem system) {

            final boolean predicate = systems.parallelStream()
                    .map(EntitySystem::owns)
                    .anyMatch(c -> c.equals(system.owns()));

            if (predicate) {
                throwNewConflictingComponentOwnershipException(system);
            } else {
                systems.add(system);
                log.debug("System {} was added to Engine Builder", system.toString());
            }
            return this;
        }

        public Builder addSystem(EntitySystem... systems) {
            for (EntitySystem system : systems) {
                addSystem(system);
            }
            return this;
        }

        public Engine build() {
            return new Engine(systems);
        }

    }

//    public enum MemoryStrategy {
//        AS_NEEDED,
//        GROW_LINEARLY_NEVER_SHRINK,
//        GROW_AND_SHRINK_LINEARLY,
//        GROW_BY_DOUBLING_NEVER_SHRINK,
//        GROW_AND_SHRINK_BY_DOUBLING
//    }
//
//    public enum MemoryHints {
//        ENTITIES_NEVER_ADDED_OR_DELETED_AFTER_LOAD,
//    }
//
//    public enum PerformanceStrategy {
//        PRIORITIZE_BATTERY_AND_ENERGY_USAGE,
//        PRIORITIZE_SPEED_AND_SMOOTHNESS
//    }
//
//    public enum PerformanceHints {
//
//    }


}
