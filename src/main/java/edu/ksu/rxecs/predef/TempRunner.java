package edu.ksu.rxecs.predef;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.stages.partial.Downstream;
import edu.ksu.rxecs.core.Stage;
import edu.ksu.rxecs.core.stages.partial.Upstream;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class TempRunner<T, R> implements Upstream<T>, Downstream<R> {

    public static void main(String[] args) {


        Stage<String, Character> s2;

        Flux<Integer> data = Flux.just(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);




    }

    @Override
    public Flux<Stage<R, ?>> downstream() {
        return null;
    }

    @Override
    public Channel upstreamChannel() {
        return null;
    }

    @Override
    public Function<?, R> function() {
        return null;
    }

    @Override
    public Flux<Stage<?, T>> upstream() {
        return null;
    }

    @Override
    public Channel downstreamChannel() {
        return null;
    }
}
