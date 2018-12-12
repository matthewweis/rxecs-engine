package edu.ksu.rxecs.core.ecs;


import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import edu.ksu.rxecs.core.Profiling;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.WorkQueueProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.WaitStrategy;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
// engine: use WorkQueueProcessor to distribute work to each component's Subscriber

@Slf4j
public class Engine {

    private volatile boolean isProcessing;

    @Getter
    private final ImmutableSet<EntitySystem> systems;

    @Getter
    final ImmutableSet<Class<? extends Component>> components;

    @Getter
    final ImmutableMap<Class<? extends Component>, EntitySystem> componentToOwnerMap;

    @Getter
    final ImmutableMap<Class<? extends Component>, MutableBag<Component>> componentToStorageMap;

    private final MutableBag<Entity> entities = Bags.mutable.empty();



    public final void update(float dt) {
        isProcessing = true;

        Flux.fromIterable(components)
                .map(cls -> Tuples.of(componentToOwnerMap.get(cls), componentToStorageMap.get(cls)))
                .parallel()
                .runOn(Schedulers.parallel())
                .subscribe(tuple -> tuple.getT2().iterator().forEachRemaining(
                                component -> tuple.getT1().update(component, new EntitySnapshot(component.entity), dt)
                        )
        );

        isProcessing = false;
    }

    public final boolean addEntity(Entity entity) {
        synchronized (entities) {
            if (!entities.contains(entity)) {
                entity.getComponents()
                        .subscribeOn(Schedulers.immediate())
                        .subscribe(component -> {
                                    final MutableBag<Component> bag = componentToStorageMap.get(component.getClass());

                                    if (bag == null) {
                                        throwNewUnmanagedComponentAddedException(entity, component);
                                    } else {
                                        bag.add(component);
                                    }

                                },
                                Throwable::printStackTrace);
                return entities.add(entity);
            } else {
                return false;
            }
        }
    }

    public final void addEntity(Entity ... entities) {
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



    private Engine(MutableBag<EntitySystem> systems) {
        this.systems = Sets.immutable.withAll(systems);

        /**
         * Change 1
         */

        final List<? extends Class<? extends Component>> collect = systems.stream().map(sys -> sys.owns()).collect(Collectors.toList());

        this.components = Sets.immutable.withAll(
//                Flux.fromIterable(systems).map(EntitySystem::owns).collectList().block()
                collect
        );

//        final Iterator<EntitySystem> iter1 = systems.iterator();
//        final MutableSet<Class<? extends Component>> set1 = Sets.mutable.empty();
//        while (iter1.hasNext()) {
//            set1.add(iter1.next().owns());
//        }
//        this.components = set1.toImmutable();

        /**
         * Change 2
         */

        this.componentToOwnerMap = Maps.immutable.withAll(
                systems.stream() // ? check this, want .class
                        .collect(Collectors.toUnmodifiableMap(EntitySystem::owns, sys -> sys))
        );

//        final Iterator<EntitySystem> iter2 = systems.iterator();
//        final MutableMap<Class<? extends Component>, EntitySystem> map1 = Maps.mutable.empty();
//        while (iter2.hasNext()) {
//            final EntitySystem next = iter2.next();
//            map1.put(next.owns(), next);
//        }
//        this.componentToOwnerMap = map1.toImmutable();

        /**
         * Change 3
         */

        this.componentToStorageMap = Maps.immutable.withAll(
                systems.stream().collect(Collectors.toMap(sys -> sys.owns(), sys -> Bags.mutable.empty()))
        );

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final MutableBag<EntitySystem> systems = Bags.mutable.empty();

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

        public Builder addSystem(EntitySystem ... systems) {
            for (EntitySystem system : systems) {
                addSystem(system);
            }
            return this;
        }

        public Engine build() {
            return new Engine(systems);
        }

        private Builder() { }

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

    public enum MemoryStrategy {
        AS_NEEDED,
        GROW_LINEARLY_NEVER_SHRINK,
        GROW_AND_SHRINK_LINEARLY,
        GROW_BY_DOUBLING_NEVER_SHRINK,
        GROW_AND_SHRINK_BY_DOUBLING
    }

    public enum MemoryHints {
        ENTITIES_NEVER_ADDED_OR_DELETED_AFTER_LOAD,
    }

    public enum PerformanceStrategy {
        PRIORITIZE_BATTERY_AND_ENERGY_USAGE,
        PRIORITIZE_SPEED_AND_SMOOTHNESS
    }

    public enum PerformanceHints {

    }


}
