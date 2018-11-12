package edu.ksu.rxecs.core;

import edu.ksu.rxecs.core.stages.partial.Stage1x;
import edu.ksu.rxecs.core.stages.partial.Stagex1;
import reactor.core.publisher.Flux;
import reactor.util.annotation.Nullable;

import java.util.function.Function;

public abstract class Stage<T, R> {

    private final Flux<Stage<?, T>> upstream;
    private final Flux<Stage<R, ?>> downstream;

    public Stage(@Nullable Flux<Stage<?, T>> upstream, @Nullable Flux<Stage<R, ?>> downstream) {
        this.upstream = upstream;
        this.downstream = downstream;
    }

    public abstract Function<T, R> function();

    public abstract StageType stageType();

    public final Flux<Stage<?, T>> upstream() {
        return upstream;
    }

    public final Flux<Stage<R, ?>> downstream() {
        return downstream;
    }

}
