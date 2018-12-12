package edu.ksu.rxecs.core.ecs;

import reactor.util.annotation.Nullable;

public abstract class Component implements Cloneable {

    /**
     * Special package-only visibility value which the ECS uses internally for O(1) component-to-entity lookups without
     * a lose of cache locality to do so.
     */
    @Nullable
    MutableEntity entity = null;

    protected Component() {

    }

}