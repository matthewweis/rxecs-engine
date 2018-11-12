package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import reactor.core.publisher.Flux;

public abstract class DefaultStagexN<T, R> implements Stage<T, R> {

    private final Stage<R, ?>[] downstream;

    @SafeVarargs
    public DefaultStagexN(Stage<R, ?> ... downstream) {
        this.downstream = downstream;
    }

    @Override
    public final Flux<Stage<R, ?>> downstream() {
        return Flux.just(downstream);
    }

    @Override
    public final Channel downstreamChannel() {
        return Channel.ONE;
    }

}
