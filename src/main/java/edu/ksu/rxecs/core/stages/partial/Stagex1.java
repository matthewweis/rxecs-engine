package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import reactor.core.publisher.Flux;

public abstract class Stagex1<T, R> extends Stage<T, R> implements Downstream<R> {

    private final Stage<R, ?> downstream;

    public Stagex1(Stage<R, ?> downstream) {
        super();
        this.downstream = downstream;
    }

    @Override
    public final Flux<Stage<R, ?>> downstream() {
        return Flux.just(downstream);
    }

    @Override
    public final Channel upstreamChannel() {
        return Channel.ONE;
    }

}
