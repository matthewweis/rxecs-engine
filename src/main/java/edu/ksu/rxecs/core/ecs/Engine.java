package edu.ksu.rxecs.core.ecs;


import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import edu.ksu.rxecs.core.Profiling;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.WorkQueueProcessor;
import reactor.util.concurrent.WaitStrategy;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
// engine: use WorkQueueProcessor to distribute work to each component's Subscriber

@Slf4j
public class Engine {

    private volatile boolean isProcessing;

    @Getter
    private final ImmutableSet<EntitySystem> systems;

    @Getter
    private final ImmutableSet<Class<? extends Component>> components;

    // does not contain entries for unowned components
    @Getter
    private final ImmutableMap<Class<? extends Component>, EntitySystem> componentToOwnerMap;

    private final ImmutableMap<Class<? extends Component>, ImmutableSet<EntitySystem>> componentToAllSystemsMap;

    private final MutableBag<Entity> entities = Bags.mutable.empty();

    private final WorkQueueProcessor<Tuple2<EntitySystem, Component>> workQueueProcessor =
            createNewWorkQueueProcessor();


    public final void update(float dt) {
        isProcessing = true;

        final ImmutableBag<Entity> entitySnapshot;

        synchronized (entities) {
            entitySnapshot = entities.toImmutable();
        }

//        final WorkQueueProcessor<Object> build = WorkQueueProcessor.builder()
//                .executor(Executors.newWorkStealingPool(Profiling.getAvailableProcessors()))
//                .bufferSize(Profiling.getAvailableProcessors())
//                .waitStrategy(WaitStrategy.busySpin()) // prioritize low-latency
//                .share(false) // false because all onNext() called from Engine
//                .build();

//        workQueueProcessor.drain().subscribe(object -> {
//           log.info(object.toString());
//        });

//        Flux.fromIterable(entitySnapshot)
////                .groupBy(entity -> entity, Entity::getComponents)
//                .map(Entity::getComponents)
//                .map(flux -> flux.)
//                .groupBy(gflux -> componentToOwnerMap.get(gflux.))
//                .doOnNext(gflux -> gflux.key().update(gflux.))
//                .subscribe(workQueueProcessor);
//                .doOnNext(groupedEntity -> groupedEntity.key().update(Bags.immutable.ofAll(groupedEntity.toIterable())));

//        Flux.fromIterable(entitySnapshot).subscribe()

        Flux.fromIterable(entitySnapshot)
                .groupBy(entity -> entity, Entity::getComponents)
                .map(Entity::getComponents)
                .map(flux -> flux.)

        isProcessing = false;
    }

    public final boolean addEntity(Entity entity) {
        synchronized (entities) {
            return entities.add(entity);
        }
    }

    public final boolean removeEntity(Entity entity) {
        synchronized (entities) {
            return entities.remove(entity);
        }
    }

    public final boolean containsEntity(Entity entity) {
        synchronized (entities) {
            return entities.contains(entity);
        }
    }

    private WorkQueueProcessor<Tuple2<EntitySystem, ImmutableSet<Component>>> createNewWorkQueueProcessor() {
        return WorkQueueProcessor.<Tuple2<EntitySystem, ImmutableSet<Component>>>builder()
//                    .executor(Executors.newCachedThreadPool())  // cache for quick-run tasks
                .executor(Executors.newWorkStealingPool(Profiling.getAvailableProcessors()))
                .bufferSize(Profiling.getAvailableProcessors())
                .waitStrategy(WaitStrategy.busySpin()) // prioritize low-latency
                .share(false) // false because all onNext() called from Engine
                .build();
    }



    private Engine(MutableBag<EntitySystem> systems) {
        this.systems = Sets.immutable.withAll(systems);

        this.components = Sets.immutable.withAll(Flux.fromIterable(systems.toList())
                .map(EntitySystem::owns).collectList().block());

        this.componentToOwnerMap = Maps.immutable.withAll(
                systems.stream() // ? check this, want .class
                        .collect(Collectors.toUnmodifiableMap(EntitySystem::owns, sys -> sys))
        );

        final MutableMap<Class<? extends Component>, ImmutableSet<EntitySystem>> map = Maps.mutable.empty();
        for (Class<? extends Component> component : components) {
            for (EntitySystem system : this.systems) {
                map.putIfAbsent(component, Sets.immutable.empty());
                if (system.uses(component)) {
                    map.put(component, map.get(component).newWith(system));
                }
            }
        }
        componentToAllSystemsMap = Maps.immutable.withAll(map);


//        final ImmutableMap<Class<? extends Component>, ImmutableSet<EntitySystem>> componentToAllSystemsMap;
//
//        final Map<? extends Class<? extends Class>, ImmutableSet<EntitySystem>> block = Flux.fromIterable(components)
//                .collectMap(cls -> cls, cls -> getSystems().select(sys -> sys.uses(cls)))
//                .block();
//
//        final ImmutableMap<Class<? extends Component>, ImmutableSet<EntitySystem>> immutableSets = Maps.immutable.withAll(
//
//        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(int capacity) {
        return new Builder();
    }

    public static class Builder {

        private final MutableBag<EntitySystem> systems = Bags.mutable.empty();

        public Builder addSystem(EntitySystem system) {

            final boolean predicate = systems.parallelStream()
                    .map(EntitySystem::owns)
                    .anyMatch(c -> c.equals(system.owns()));

            if (predicate) {

                final String warningString = String.format(
                        "Conflicting ownership of component %s when adding system %s",
                        system.owns().toString(),
                        system.toString()
                );

                log.error(warningString);
                throw new RuntimeException(warningString);
            } else {
                systems.add(system);
                log.debug("System %s was added to Engine Builder", system.toString());
            }
            return this;
        }

        public Engine build() {
            return new Engine(systems);
        }

        private Builder() { }

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
