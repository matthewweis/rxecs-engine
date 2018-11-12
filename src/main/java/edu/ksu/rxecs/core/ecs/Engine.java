package edu.ksu.rxecs.core.ecs;


import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.stream.Collectors;
import java.util.stream.Stream;
// engine: use WorkQueueProcessor to distribute work to each component's Subscriber

@Slf4j
public class Engine {

    private final ImmutableBag<EntitySystem> systems;
    private final ImmutableBag<Class<Component>> components;
    private final ImmutableMap<Class<Component>, EntitySystem> componentToOwnerMap; // does not contain entries for unowned components


    private Engine(MutableBag<EntitySystem> systems) {
        this.systems = Bags.immutable.withAll(systems);

        this.components = Bags.immutable.withAll(
                Stream.concat(
                        systems.parallelStream().flatMap(s -> s.getBorrowedComponents().stream()),
                        systems.parallelStream().map(EntitySystem::getOwnedComponent)
                ).collect(Collectors.toUnmodifiableSet())
        );

        this.componentToOwnerMap = Maps.immutable.withAll(
                systems.parallelStream()
                        .collect(Collectors.toUnmodifiableMap(EntitySystem::getOwnedComponent, sys -> sys))
        );
    }

    public Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private final MutableBag<EntitySystem> systems = Bags.mutable.empty();

        public Builder addSystem(EntitySystem system) {

            val predicate = systems.parallelStream()
                    .map(EntitySystem::getOwnedComponent)
                    .anyMatch(c -> c.equals(system.getOwnedComponent()));

            if (predicate) {

                final String warningString = String.format("Conflicting ownership of component %s when adding system %s",
                        system.getOwnedComponent().toString(),
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


}
