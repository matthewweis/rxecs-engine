package edu.ksu.rxecs.predef;

import edu.ksu.rxecs.core.ecs.*;
import edu.ksu.rxecs.core.ecs.system.TestSystem;

public class TempRunner {

    public static void main(String[] args) {

        TestSystem system = new TestSystem(Component1.class, Component2.class);

        Engine engine = Engine.builder().addSystem(system).build();

        MutableEntity entity1 = new MutableEntity();
        entity1.addComponent(new Component1());
        entity1.addComponent(new Component2());

        engine.addEntity(entity1);

//        while (true) {
            engine.update(1.0f/60.0f);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        }

    }

    private static class Component1 extends Component {
        @Override
        public String toString() {
            return "Component1{}";
        }
    }

    private static class Component2 extends Component {
        @Override
        public String toString() {
            return "Component2{}";
        }
    }
}
