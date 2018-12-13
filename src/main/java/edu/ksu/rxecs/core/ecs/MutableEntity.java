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
import java.util.stream.Collectors;

public final class MutableEntity implements Entity {

    final MutableMap<Class<? extends Component>, ChronoBox> components = Maps.mutable.empty();

    public MutableEntity() { }

    @Override
    public Flux<Component> getComponents() {
        return Flux.fromIterable(components.values()).map(box -> box.then());
    }

    public Component getComponent(Class<? extends Component> component) {
        return components.get(component).then();
    }

    /**
     *
     * @param component the new component to include. Only one per class will be kept for an entity.
     * @return the last (now removed) component if one was removed to add the new component
     */
    public void addComponent(@NonNull Component component, Engine engine) {
        if (component.entity != null) {
            throw new RuntimeException("Component added which was already associated with another entity.");
        }
        component.entity = this;

        if (components.containsKey(component.getClass())) {

            // remove existing
            engine.componentToStorageMap.get(component.getClass()).remove(components.get(component.getClass()));
            components.remove(component.getClass());
        }

        final ChronoBox box = new ChronoBox(component);
        engine.componentToStorageMap.get(component.getClass()).add(box);
        components.put(component.getClass(), box);

    }

    public void addComponent(Engine engine, @NonNull Component ... components) {
        for (Component component : components) {
            addComponent(component, engine);
        }
    }

    public void removeComponent(@NonNull Class<? extends Component> component, Engine engine) {
        final ChronoBox removedComponent = components.remove(component);
        engine.componentToStorageMap.get(component).remove(removedComponent);
        if (removedComponent != null) {
            if (removedComponent.now().entity == null || removedComponent.then().entity == null) {
                throw new RuntimeException("Component should never lose entity association before removal.");
            }
            removedComponent.now().entity = null; // prevent memory leaks
            removedComponent.then().entity = null; // prevent memory leaks
        }
    }

    public boolean containsComponent(@NonNull Class<? extends Component> component) {
        return components.containsKey(component);
    }

    protected ImmutableSet<Component> getComponentsImmutableSet() {
        return Sets.immutable.ofAll(components.values().parallelStream().map(box -> box.then()).collect(Collectors.toList()));
    }

    protected MutableSet<Component> getComponentsSet() {
        return Sets.mutable.ofAll(components.values().parallelStream().map(box -> box.then()).collect(Collectors.toList()));
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
