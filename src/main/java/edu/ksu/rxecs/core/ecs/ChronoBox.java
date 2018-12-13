package edu.ksu.rxecs.core.ecs;

import lombok.Getter;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.util.annotation.Nullable;

final class ChronoBox implements Publisher<Component> {

    final DirectProcessor<Component> delegateProcessor = DirectProcessor.create();

    ChronoBox(Component component) {
        then = (Component) component.clone();
        now = component;
    }

    @Nullable
    private Component then = null;

    @Nullable
    private Component now = null;

    void advance() {

        if (!then.equals(now)) {
            delegateProcessor.onNext(now);
        }

        then = now;
        now = (Component) now.clone();
    }

    Component then() {
        return then;
    }

    Component now() {
        return now;
    }

    @Override
    public void subscribe(Subscriber<? super Component> s) {
        delegateProcessor.subscribe(s);
    }
}
