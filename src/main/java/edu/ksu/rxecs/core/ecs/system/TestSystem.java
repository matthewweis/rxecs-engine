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
        // MOCK AN EXPENSIVE OPERATION
        for (int i=0; i < 30000; i++) {
            final double random = Math.random();
            Math.sqrt(Integer.parseInt("230914122") * random);
            Math.sqrt(Integer.parseInt("123499214") * random);
            Math.sqrt(Integer.parseInt("999823983") * random);
            Math.sqrt(Integer.parseInt("123241222") * random);
            Math.sqrt(Integer.parseInt("123455678") * random);
        }
        log.debug("testSystem update: " + ownedComponent.toString());
    }
}