package edu.ksu.rxecs.core.ecs;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;
import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.Objects;

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

    /**
     *
     * @param component the new component to include. Only one per class will be kept for an entity.
     * @return the last (now removed) component if one was removed to add the new component
     */
    public @Nullable Component addComponent(@NonNull Component component) {
        if (component.entity != null) {
            throw new RuntimeException("Component added which was already associated with another entity.");
        }
        component.entity = this;
        return components.put(component.getClass(), component);
    }

    public @Nullable Component removeComponent(@NonNull Class<? extends Component> component) {
        final Component removedComponent = components.remove(component);
        if (removedComponent != null) {
            if (removedComponent.entity == null) {
                throw new RuntimeException("Component should never lose entity association before removal.");
            }
            removedComponent.entity = null;
        }
        return removedComponent;
    }

    public boolean containsComponent(@NonNull Class<? extends Component> component) {
        return components.containsKey(component);
    }

    protected ImmutableSet<Component> getComponentsImmutableSet() {
        return Sets.immutable.ofAll(components.values());
    }

    protected MutableSet<Component> getComponentsSet() {
        return Sets.mutable.ofAll(components.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableEntity)) return false;
        MutableEntity that = (MutableEntity) o;
        return Objects.equals(getComponents(), that.getComponents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getComponents());
    }

    @Override
    public String toString() {
        return "MutableEntity{" +
                "components=" + components +
                '}';
    }
}
