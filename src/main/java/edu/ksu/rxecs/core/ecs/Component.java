package edu.ksu.rxecs.core.ecs;

import reactor.util.annotation.Nullable;

public abstract class Component implements Cloneable {

    /**
     * Special package-only visibility value which the ECS uses internally for O(1) component-to-entity lookups without
     * a lose of cache locality to do so.
     */
    @Nullable
    MutableEntity entity = null;

    @Nullable
    Component _this = this;

    @Nullable
    Component clone = null;

    @Override
    public final Object clone() {
        try {
            clone = (Component) super.clone();
            return clone;
        } catch(Exception e) {
            return null;
        }
    }

    void step() {

    }

}