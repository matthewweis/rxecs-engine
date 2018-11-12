package edu.ksu.rxecs.core.annotations;

import edu.ksu.rxecs.core.ecs.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Wire {

    Class<Component> owns();

    Class<Component>[] borrows();

}
