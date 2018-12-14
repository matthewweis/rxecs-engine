package edu.ksu.rxecs.core.ecs;

import com.gs.collections.api.set.ImmutableSet;
import reactor.core.publisher.Flux;

public final class EntitySnapshot implements Entity {

    private final ImmutableSet<Component> components;

    EntitySnapshot(MutableEntity entity) {
        this.components = entity.getComponentsImmutableSet();
    }

    @Override
    public Flux<Component> getComponents() {
        return Flux.fromIterable(components);
    }


}
