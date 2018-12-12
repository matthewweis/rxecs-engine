package edu.ksu.rxecs.core.ecs;


import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.factory.Sets;
import edu.ksu.rxecs.core.ecs.component.MutableComponent;

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
public abstract class EntitySystem<T extends Component> {

    private final Class<T> owns;
//    private final ImmutableSet<Class<? extends Component>> uses;

    protected EntitySystem(Class<T> owns) {
        this.owns = owns;
//        this.uses = this.borrows.newWith(owns);
    }

    /**
     * Override this method for code to be executed before {@link #update(ImmutableSet)}.
     */
    protected void beforeUpdate() { }

    /**
     * Executed on each update.
     */
//    protected abstract void update(ImmutableBag entities);
    protected abstract void update(T ownedComponent, EntitySnapshot snapshot, float dt);

    /**
     * Override this method for code to be executed after {@link #update(ImmutableSet)}.
     */
    protected void afterUpdate() { }

    /**
     * Executed on start.
     */
    protected void init() { }

    /**
     * Executed on stop.
     */
    protected void close() { }

//    public final Class<? extends Component> getOwnedComponent() {
//        return owns;
//    }
//
//    public final ImmutableSet<Class<? extends Component>> getBorrowedComponents() {
//        return borrows;
//    }

//    public final boolean uses(Class<? extends Component> component) {
//        return uses.contains(component);
//    }

    public final boolean owns(Class<? extends Component> component) {
        return owns.equals(component);
    }

//    public final boolean borrows(Class<? extends Component> component) {
//        return borrows.contains(component);
//    }
//
//    public final ImmutableSet<Class<? extends Component>> uses() {
//        return uses;
//    }

    public final Class<? extends Component> owns() {
        return owns;
    }

//    public final ImmutableSet<Class<? extends Component>>  borrows() {
//        return borrows;
//    }

}
