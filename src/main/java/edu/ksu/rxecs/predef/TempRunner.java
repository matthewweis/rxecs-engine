package edu.ksu.rxecs.predef;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.Engine;
import edu.ksu.rxecs.core.ecs.MutableEntity;
import edu.ksu.rxecs.core.ecs.system.TestSystem;

public class TempRunner {

    public static void main(String[] args) {

        final Component component1_1 = new Component1();
        final Component component1_2 = new Component1();
        final Component component1_3 = new Component1();

        final Component component2_1 = new Component2();
        final Component component2_2 = new Component2();
        final Component component2_3 = new Component2();

        final Component component3_1 = new Component3();
        final Component component3_2 = new Component3();
        final Component component3_3 = new Component3();

        final Component component4_1 = new Component4();
        final Component component4_2 = new Component4();
        final Component component4_3 = new Component4();

        final Component component5_1 = new Component5();
        final Component component5_2 = new Component5();
        final Component component5_3 = new Component5();

        final Component component6_1 = new Component6();
        final Component component6_2 = new Component6();
        final Component component6_3 = new Component6();

        final Component component7_1 = new Component7();
        final Component component7_2 = new Component7();
        final Component component7_3 = new Component7();

        final Component component8_1 = new Component8();
        final Component component8_2 = new Component8();
        final Component component8_3 = new Component8();

        final Component component9_1 = new Component9();
        final Component component9_2 = new Component9();
        final Component component9_3 = new Component9();

        final Component component10_1 = new Component10();
        final Component component10_2 = new Component10();
        final Component component10_3 = new Component10();

        final TestSystem system1 = new TestSystem(Component1.class);
        final TestSystem system2 = new TestSystem(Component2.class);
        final TestSystem system3 = new TestSystem(Component3.class);
        final TestSystem system4 = new TestSystem(Component4.class);
        final TestSystem system5 = new TestSystem(Component5.class);
        final TestSystem system6 = new TestSystem(Component6.class);
        final TestSystem system7 = new TestSystem(Component7.class);
        final TestSystem system8 = new TestSystem(Component8.class);
        final TestSystem system9 = new TestSystem(Component9.class);
        final TestSystem system10 = new TestSystem(Component10.class);


        final Engine.Builder builder = Engine.builder();
        builder.addSystem(system1);
        builder.addSystem(system2);
        builder.addSystem(system3);
        builder.addSystem(system4);
        builder.addSystem(system5);
        builder.addSystem(system6);
        builder.addSystem(system7);
        builder.addSystem(system8);
        builder.addSystem(system9);
        builder.addSystem(system10);
        final Engine engine = builder.build();


        final MutableEntity entity_1 = new MutableEntity();
        entity_1.addComponent(component1_1, component1_2, component1_3);

        final MutableEntity entity_2 = new MutableEntity();
        entity_1.addComponent(component2_1, component2_2, component2_3);

        final MutableEntity entity_3 = new MutableEntity();
        entity_1.addComponent(component3_1, component3_2, component3_3);

        final MutableEntity entity_4 = new MutableEntity();
        entity_1.addComponent(component4_1, component4_2, component4_3);

        final MutableEntity entity_5 = new MutableEntity();
        entity_1.addComponent(component5_1, component5_2, component5_3);

        final MutableEntity entity_6 = new MutableEntity();
        entity_1.addComponent(component6_1, component6_2, component6_3);

        final MutableEntity entity_7 = new MutableEntity();
        entity_1.addComponent(component7_1, component7_2, component7_3);

        final MutableEntity entity_8 = new MutableEntity();
        entity_1.addComponent(component8_1, component8_2, component8_3);

        final MutableEntity entity_9 = new MutableEntity();
        entity_1.addComponent(component9_1, component9_2, component9_3);

        final MutableEntity entity_10 = new MutableEntity();
        entity_1.addComponent(component10_1, component10_2, component10_3);

        engine.addEntity(entity_1);
        engine.addEntity(entity_2);
        engine.addEntity(entity_3);
        engine.addEntity(entity_4);
        engine.addEntity(entity_5);
        engine.addEntity(entity_6);
        engine.addEntity(entity_7);
        engine.addEntity(entity_8);
        engine.addEntity(entity_9);
        engine.addEntity(entity_10);


        while (true) {
            engine.update(1.0f/60.0f);
        }

    }

    public static abstract class MockC extends Component {
        public int data = 1;
    }

    private static class Component1 extends MockC {
//        public int data = 1;

        @Override
        public String toString() {
            return "Component1{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component2 extends MockC {
//        public int data = 2;

        @Override
        public String toString() {
            return "Component2{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component3 extends MockC {
//        public int data = 3;

        @Override
        public String toString() {
            return "Component3{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component4 extends MockC {
//        public int data = 4;

        @Override
        public String toString() {
            return "Component4{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component5 extends MockC {
//        public int data = 5;

        @Override
        public String toString() {
            return "Component5{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component6 extends MockC {
//        public int data = 6;

        @Override
        public String toString() {
            return "Component6{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component7 extends MockC {
//        public int data = 7;

        @Override
        public String toString() {
            return "Component7{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component8 extends MockC {
//        public int data = 8;

        @Override
        public String toString() {
            return "Component8{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component9 extends MockC {
//        public int data = 9;

        @Override
        public String toString() {
            return "Component9{" +
                    "data=" + data +
                    '}';
        }
    }

    private static class Component10 extends MockC {
//        public int data = 10;

        @Override
        public String toString() {
            return "Component10{" +
                    "data=" + data +
                    '}';
        }
    }

}
