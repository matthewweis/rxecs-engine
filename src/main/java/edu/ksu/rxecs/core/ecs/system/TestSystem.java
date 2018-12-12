package edu.ksu.rxecs.core.ecs.system;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySnapshot;
import edu.ksu.rxecs.core.ecs.EntitySystem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSystem extends EntitySystem {

    public TestSystem(Class<? extends Component> owns) {
        super(owns);
    }

    @Override
    protected void update(Component ownedComponent, EntitySnapshot snapshot, float dt) {
        log.debug("testSystem update: " + ownedComponent.toString());
    }
}