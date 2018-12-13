package edu.ksu.rxecs.core.ecs;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
final class Bootstrapper {

    private Bootstrapper() { }

//    {
//        final Reflections reflections = new Reflections("");
//        final Set<Class<? extends Component>> components = reflections.getSubTypesOf(Component.class);
//
//        components.forEach(component -> {
//            final Field[] fields = component.getFields();
//
//            for (Field field : fields) {
//
//            }
//
//        });
//    }

}
