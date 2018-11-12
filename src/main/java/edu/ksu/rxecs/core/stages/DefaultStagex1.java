package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.stages.partial.Downstream;
import reactor.core.publisher.Flux;

public abstract class DefaultStagex1<T, R> extends Stage<T,R> implements Downstream {

    private final Stage<R, ?> downstream;

    public DefaultStagex1(Stage<R, ?> downstream) {
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
