package edu.ksu.rxecs.core.ecs;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySnapshot;
import edu.ksu.rxecs.core.ecs.EntitySystem;
import edu.ksu.rxecs.predef.TempRunner;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@Slf4j
public class TestSystem extends EntitySystem {

    public TestSystem(Class<? extends Component> owns) {
        super(owns);
    }

    @Override
    protected void update(Component ownedComponent, EntitySnapshot snapshot, float dt) {
        // MOCK AN EXPENSIVE OPERATION
        double sum = 0;
        for (int i=0; i < 30000; i++) {
            final double random = Math.random();
            double x1 = Math.sqrt(Integer.parseInt(createParseableIntString()) * random);
            double x2 = Math.sqrt(Integer.parseInt(createParseableIntString()) * random);
            double x3 = Math.sqrt(Integer.parseInt(createParseableIntString()) * random);
            double x4 = Math.sqrt(Integer.parseInt(createParseableIntString()) * random);
            double x5 = Math.sqrt(Integer.parseInt(createParseableIntString()) * random);
            double x6 = x1 + x2 - x3 * x4 / x5;
            sum += x6;
        }

        if (ownedComponent instanceof TempRunner.MockC) {
            final TempRunner.MockC oc = (TempRunner.MockC) ownedComponent;
            final int n = (int) (Math.random() * 100);
            log.debug("testSystem update: was: " + oc.data + " changing to: " + n);
            oc.data = n;
        }

//        log.debug("testSystem update: " + ownedComponent.toString() + sum);

    }

    private String createParseableIntString() {
        final Random random = new Random();
        StringBuilder sb = new StringBuilder(9);
        for (int i=0; i < 9; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }
}