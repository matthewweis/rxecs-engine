package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.StageType;
import reactor.core.publisher.Flux;

public abstract class DefaultStagex0<T, R> implements Stage<T, R> {

    public DefaultStagex0() {

    }

    @Override
    public final Flux<Stage<R, ?>> downstream() {
        return Flux.empty();
    }

    @Override
    public final Channel downstreamChannel() {
        return Channel.ZERO;
    }

}
