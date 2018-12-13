package edu.ksu.rxecs.core.ecs;

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

    private final Class<? extends Component> owns;

    boolean isPaused;

    protected EntitySystem(Class<? extends Component> owns) {
        this.owns = owns;
    }

    /**
     * Override this method for code to be executed before {@link #update(Component, EntitySnapshot, float)}.
     */
    protected void beforeUpdate() { }

    /**
     * Executed on each update.
     */
    protected abstract void update(Component ownedComponent, EntitySnapshot snapshot, float dt);

    /**
     * Override this method for code to be executed after {@link #update(Component, EntitySnapshot, float)}.
     */
    protected void afterUpdate() { }

    /**
     * Executed when paused.
     * (Does not execute if {@link #pause()} is called while {@link EntitySystem} is already paused.
     */
    protected void doWhenPaused() { }

    /**
     * Executed on resumed.
     * (Does not execute if {@link #resume()} ()} is called while {@link EntitySystem} is not paused.
     */
    protected void doWhenResumed() { }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        if (!isPaused) {
            isPaused = true;
            doWhenPaused();
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            doWhenResumed();
        }
    }

    public final boolean owns(Class<? extends Component> component) {
        return owns.equals(component);
    }


    public final <T extends Component> Class<T> owns() {
        return (Class<T>) owns;
    }

}
