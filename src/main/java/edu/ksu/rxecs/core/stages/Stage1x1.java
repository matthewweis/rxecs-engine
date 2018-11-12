package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.StageType;
import edu.ksu.rxecs.core.stages.partial.*;
import reactor.core.publisher.Flux;

public abstract class Stage1x1<T, R> extends Stage<T, R> implements Upstream<T>, Downstream<R> {

    private final Stage1x<?, T> upstream;
    private final Stagex1<R, ?> downstream;

    public Stage1x1(Stage1x<?, T> upstream, Stagex1<R, ?> downstream) {
        this.upstream = upstream;
        this.downstream = downstream;
    }

    @Override
    public StageType stageType() {
        return StageType.ONE_TO_ONE;
    }

    @Override
    public Flux<Stage<R, ?>> downstream() {
        return Flux.just(downstream);
    }

    @Override
    public Channel upstreamChannel() {
        return upstream.downstreamChannel();
    }

    @Override
    public Flux<Stage<?, T>> upstream() {
        return Flux.just();
    }

    @Override
    public Channel downstreamChannel() {
        return downstream.upstreamChannel();
    }
}
