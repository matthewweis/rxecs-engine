package edu.ksu.rxecs.core.ecs;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import reactor.core.publisher.Flux;

public final class MutableEntity implements Entity {

    private final MutableMap<Class<? extends Component>, Component> components = Maps.mutable.empty();

    public MutableEntity() { }

    @Override
    public Flux<Component> getComponents() {
        return Flux.fromIterable(components.values());
    }

    public Component getComponent(Class<? extends Component> component) {
        return components.get(component);
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    public void removeComponent(Class<? extends Component> component) {
        components.remove(component);
    }

    public boolean containsComponent(Class<? extends Component> component) {
        return components.containsKey(component);
    }

    protected ImmutableSet<Component> getComponentsImmutableSet() {
        return Sets.immutable.ofAll(components.values());
    }

    protected MutableSet<Component> getComponentsSet() {
        return Sets.mutable.ofAll(components.values());
    }

}
