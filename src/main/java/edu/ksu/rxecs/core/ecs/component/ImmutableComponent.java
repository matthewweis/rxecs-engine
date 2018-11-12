package edu.ksu.rxecs.core.ecs.component;

import edu.ksu.rxecs.core.ecs.Component;

/**
 * {@link Component}s retrieved from the {@link edu.ksu.rxecs.core.ecs.Engine} are returned as
 * {@link ImmutableComponent}s whenever editing is not thread-safe.
 */
public abstract class ImmutableComponent extends Component {

}
