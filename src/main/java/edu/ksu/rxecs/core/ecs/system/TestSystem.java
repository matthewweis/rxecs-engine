package edu.ksu.rxecs.core.ecs.system;

import com.gs.collections.api.bag.ImmutableBag;
import edu.ksu.rxecs.core.ecs.Component;
import edu.ksu.rxecs.core.ecs.EntitySystem;

import java.util.Iterator;
import java.util.Set;

public class TestSystem extends EntitySystem {

    @SafeVarargs
    public TestSystem(Class<? extends Component> owns, Class<? extends Component> ... borrows) {
        super(owns, borrows);
    }

    @Override
    protected void update(ImmutableBag entities) {
        final Iterator iterator = entities.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next().toString());
        }
    }

}
