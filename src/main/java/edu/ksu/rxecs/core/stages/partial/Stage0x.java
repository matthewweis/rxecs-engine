package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import reactor.core.publisher.Flux;

public abstract class Stage0x<T, R> extends Stage<T, R> implements Upstream<T> {

    public Stage0x() {
        super(Flux.empty(), null);
    }

    @Override
    public final Flux<Stage<?, T>> upstream() {
        return Flux.empty();
    }

    @Override
    public final Channel downstreamChannel() {
        return Channel.ZERO;
    }

}
