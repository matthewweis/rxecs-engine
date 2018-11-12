package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;

import reactor.core.publisher.Flux;

public abstract class StageNx<T, R> extends Stage<T, R> implements Upstream<T> {

    private final Stage<?, T>[] upstream;

    @SafeVarargs
    public StageNx(Stage<?, T> ... upstream) {
        super(upstream, null);
        this.upstream = upstream;
    }

    @Override
    public final Flux<Stage<?, T>> upstream() {
        return Flux.fromArray(upstream);
    }

    @Override
    public final Channel downstreamChannel() {
        return Channel.MANY;
    }

}
