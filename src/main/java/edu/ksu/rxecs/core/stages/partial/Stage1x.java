package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.StageType;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public abstract class Stage1x<T, R> extends Stage<T, R> implements Upstream<T> {

//    private final Stage<?, T> upstream;

    public Stage1x(Stage<?, T> upstream) {
        super(Flux.just(upstream), null);
//        this.upstream = upstream;
    }

    @Override
    public Channel downstreamChannel() {
        return Channel.ONE;
    }
}
