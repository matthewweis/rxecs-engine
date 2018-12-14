package edu.ksu.rxecs.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Stage was based on an earlier concept of creating "stages" which allowed for some forms of serialization while
 * still keeping the update cycle mostly parallel. This ended up being confusing and didn't provide any functionality
 * which couldn't be achieved under the current system.
 *
 * @param <T>
 * @param <R>
 */
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
