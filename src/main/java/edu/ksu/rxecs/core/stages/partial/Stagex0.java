package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import reactor.core.publisher.Flux;

public abstract class Stagex0<T, R> extends Stage<T, R> implements Downstream<R> {

    public Stagex0(Stage<?, T>[] upstream, Stage<R, ?>[] downstream) {
        super(upstream, downstream);
    }

    @Override
    public final Flux<Stage<R, ?>> downstream() {
        return Flux.empty();
    }

    @Override
    public final Channel upstreamChannel() {
        return Channel.ZERO;
    }

}
