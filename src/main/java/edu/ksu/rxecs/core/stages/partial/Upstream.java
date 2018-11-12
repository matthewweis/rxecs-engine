package edu.ksu.rxecs.core.stages.partial;

import edu.ksu.rxecs.core.Channel;
import edu.ksu.rxecs.core.Stage;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public interface Upstream<T> {

    Flux<Stage<?, T>> upstream();

}
