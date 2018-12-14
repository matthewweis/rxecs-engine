package edu.ksu.rxecs.jme.core.systems;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySnapshot;
import edu.ksu.rxecs.core.ecs.EntitySystem;
import edu.ksu.rxecs.jme.core.components.ScaleComponent;

public class SizeSystem extends EntitySystem {

    public SizeSystem() {
        super(ScaleComponent.class);
    }

    @Override
    protected void update(Component ownedComponent, EntitySnapshot snapshot, float dt) {
        final ScaleComponent scale = (ScaleComponent) ownedComponent;
        final int negOrPos = scale.invertDirection ? -1 : 1;
        scale.scale += negOrPos * scale.delta * dt;
        if (scale.scale < scale.minScale || scale.scale > scale.maxScale) {
            scale.invertDirection = true;
        }
    }


}
