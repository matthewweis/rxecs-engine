package edu.ksu.rxecs.core.ecs;

import edu.ksu.rxecs.core.annotations.Wire;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
//import org.reflections.Reflections;
import reactor.util.annotation.NonNull;

import java.util.Set;

@Slf4j
final class Bootstrapper {



    private Bootstrapper() { }

    @NonNull Bootstrapper createWithFullScan() {
        log.info("running a full scan");
//        val reflections = new Reflections();
//
//        final Set<Class<?>> systems = reflections.getTypesAnnotatedWith(Wire.class);
//
//        for (Class<?> clazz : systems) {
//
//            final Class<Component> owns = clazz.getDeclaredAnnotation(Wire.class).owns();
//            final Class<Component>[] borrows = clazz.getDeclaredAnnotation(Wire.class).borrows();
//
//            if (EntitySystem.class.isAssignableFrom(clazz)) {
//
//                val constructor = system.getDeclaredConstructor(Class.class, Set.class);
//
//                final EntitySystem system = (Entity) clazz.getDeclaredConstructor()
//
//
////                if (wireType == WireType.HEADLESS) {
////                    headlessCapDefs.add(capDef);
////                } else if (wireType == WireType.GUI) {
////                    guiCapDefs.add(capDef);
////                }
//                log.debug("found and instantiated {}", );
//            } else {
//                System.err.format("")
//            }
//        }

        return null;
    }

}
