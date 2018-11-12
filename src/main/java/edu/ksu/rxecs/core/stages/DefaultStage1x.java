package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.stages.partial.Stage1x;
import reactor.core.publisher.Flux;

public abstract class DefaultStage1x<T, R> implements Stage1x<T, R> {

    private final Stage<?, T> upstream;

    public DefaultStage1x(Stage<?, T> upstream) {
        this.upstream = upstream;
    }

    @Override
    public final Flux<Stage<?, T>> upstream() {
        return Flux.just(upstream);
    }

}
