package edu.ksu.rxecs.core.ecs;

import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

import java.util.List;
import java.util.Set;

/**
 * health: gravitySystem, buffSystem, debuffSystem, bulletSystem...
 *
 * systems need to be able to depend ON other systems, but ordering all systems reduce parallelism
 *  --> can easily create circular dependencies
 *  --> solution: allow "TerminalSystem" which is computed after Systems, but can only depend on other Systems, not TerminalSystems
 *
 *  * if system provides a component to handle, it must automatically loop over that component
 *
 */
public abstract class EntitySystem {

    private final Class<Component> owns;
    private final Set<Class<Component>> borrows;

    protected EntitySystem(Class<Component> owns, Class<Component> ... borrows) {
        this.owns = owns;
        this.borrows = ObjectSets.unmodifiable(new ObjectOpenHashSet<>(borrows));
    }

    /**
     * Override this method for code to be executed before {@link #update()}.
     */
    protected void beforeUpdate() { }

    /**
     * Executed on each update.
     */
    protected abstract void update();

    /**
     * Override this method for code to be executed after {@link #update()}.
     */
    protected void afterUpdate() { }

    /**
     * Executed on start.
     */
    protected void start() { }

    /**
     * Executed on stop.
     */
    protected void stop() { }

    public final Class<Component> getOwnedComponent() {
        return owns;
    }

    public final Set<Class<Component>> getBorrowedComponents() {
        return borrows;
    }

}
