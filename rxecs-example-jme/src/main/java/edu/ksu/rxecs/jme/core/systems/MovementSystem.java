package edu.ksu.rxecs.jme.core.systems;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySnapshot;
import edu.ksu.rxecs.core.ecs.EntitySystem;
import edu.ksu.rxecs.jme.core.components.PositionComponent;

public class MovementSystem extends EntitySystem {

    public MovementSystem() {
        super(PositionComponent.class);
    }

    @Override
    protected void update(Component ownedComponent, EntitySnapshot snapshot, float dt) {
        final PositionComponent geom = (PositionComponent) ownedComponent;
        geom.x = geom.dx * dt; // treating x, y, and z as a time-normalized dx, dy, and dz for the purpose of demo
        geom.y = geom.dy * dt;
        geom.z = geom.dz * dt;
    }

}
