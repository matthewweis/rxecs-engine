package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.stages.partial.Stage0x;
import edu.ksu.rxecs.core.stages.partial.Upstream;
import reactor.core.publisher.Flux;

public abstract class DefaultStage0x<T, R> extends Stage<T, R> implements Upstream<T> {

    public DefaultStage0x() {

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
