package edu.ksu.rxecs.core.ecs.system;

import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySystem;

import java.util.Set;

public class TestSystem extends EntitySystem {


    protected TestSystem(Class<Component> owns, Class<Component> ... borrows) {
        super(owns, borrows);
    }

    @Override
    protected void update() {

    }



}
