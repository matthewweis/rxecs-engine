package edu.ksu.rxecs.core.ecs.component;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.Entity;
import lombok.Getter;

final class BoxedComponent {

    @Getter
    final Entity entity;

    @Getter
    final Component component;

    BoxedComponent(Entity entity, Component component) {
        this.entity = entity;
        this.component = component;
    }

}
