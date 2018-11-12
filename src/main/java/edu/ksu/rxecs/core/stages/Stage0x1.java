package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.StageType;
import edu.ksu.rxecs.core.stages.partial.Downstream;
import edu.ksu.rxecs.core.stages.partial.Stage0x;
import edu.ksu.rxecs.core.stages.partial.Stagex1;
import edu.ksu.rxecs.core.stages.partial.Upstream;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public abstract class Stage0x1<T, R> extends Stage<T, R> implements Downstream<R> {

//    private final Stage0x<?, T> upstream;
    private final Stagex1<R, ?> downstream;

    public Stage0x1(/*Stage0x<?, T> upstream,*/ Stagex1<R, ?> downstream) {
//        this.upstream = upstream;
        this.downstream = downstream;
    }

    @Override
    public StageType stageType() {
        return StageType.ZERO_TO_ONE;
    }

    @Override
    public Flux<Stage<R, ?>> downstream() {
        return Flux.just(downstream);
    }

    @Override
    public Channel upstreamChannel() {
        return null;
    }

}
