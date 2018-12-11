package edu.ksu.rxecs.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.function.Function;

public abstract class Stage<T, R> {

    private final Flux<Stage<?, T>> upstream;
    private final Flux<Stage<R, ?>> downstream;

    public Stage(Flux<Stage<?, T>> upstream, Flux<Stage<R, ?>> downstream) {
        this.upstream = upstream;
        this.downstream = downstream;
    }

    public Stage(Flux<Stage<?, T>> upstream, Mono<Stage<R, ?>> downstream) {
        this.upstream = upstream;
        this.downstream = Flux.from(downstream);
    }

    public Stage(Mono<Stage<?, T>> upstream, Flux<Stage<R, ?>> downstream) {
        this.upstream = Flux.from(upstream);
        this.downstream = downstream;
    }

    public Stage(Mono<Stage<?, T>> upstream, Mono<Stage<R, ?>> downstream) {
        this.upstream = Flux.from(upstream);
        this.downstream = Flux.from(downstream);
    }

    public abstract Function<T, R> function();

    public final Flux<Stage<?, T>> upstream() {
        return upstream;
    }

    public final Flux<Stage<R, ?>> downstream() {
        return downstream;
    }

}
