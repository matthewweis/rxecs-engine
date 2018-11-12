package edu.ksu.rxecs.core.stages;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.impl.factory.Bags;
import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;

import reactor.core.publisher.Flux;

import java.util.Set;

public abstract class DefaultStageNx<T, R> implements Stage<T, R> {

    private final Stage<?, T>[] upstream;

    @SafeVarargs
    public DefaultStageNx(Stage<?, T> ... upstream) {
        this.upstream = upstream;
    }

    @Override
    public final Flux<Stage<?, T>> upstream() {
        return Flux.fromArray(upstream);
    }

    @Override
    public final Channel upstreamChannel() {
        return Channel.MANY;
    }

}
