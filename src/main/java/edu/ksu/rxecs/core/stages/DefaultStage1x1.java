package edu.ksu.rxecs.core.stages;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.StageType;
import lombok.Getter;
import lombok.Setter;
import org.reactivestreams.Processor;
import reactor.core.publisher.Mono;

public abstract class DefaultStage1x1<T, R> implements Stage<T,R> {

    @Getter
    @Setter
    Processor<T, R> processor;

//    @Getter
//    Stage<?, T> upstream;
//
//    @Getter
//    Stage<R, ?> downstream;

    public DefaultStage1x1(Processor<T, R> processor) {
        this.processor = processor;
    }

    public DefaultStage1x1(Processor<T, R> processor, Stage<?, T> upstream) {
        this(processor);
    }

    public DefaultStage1x1(Processor<T, R> processor, Stage<?, T> upstream, Stage<R, ?> downstream) {
        this(processor, upstream);
//        this.andThen(downstream);
    }

//    public void andThen(Stage<R, ?> downstream) {
//        if (downstream.upstreamChannel().block() == Channel.ZERO) {
//        }
//        this.downstream = nextStage;
//        nextStage.previousStage = this;
//    }

    @Override
    public Channel upstreamChannel() {
        return Channel.ONE;
    }

    @Override
    public Channel downstreamChannel() {
        return Channel.ONE;
    }

    @Override
    public StageType stageType() {
        return StageType.ONE_TO_ONE;
    }


//    @Override
//    public Channel getUpstreamChannel() {
//        return null;
//    }
//
//    @Override
//    public Channel getDownstreamChannel() {
//        return null;
//    }
//
//    @Override
//    public StageType getStageType() {
//        return null;
//    }



}
