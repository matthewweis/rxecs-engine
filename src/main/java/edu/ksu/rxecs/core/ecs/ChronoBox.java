package edu.ksu.rxecs.core.ecs;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.DirectProcessor;
import reactor.util.annotation.Nullable;

final class ChronoBox implements Publisher<Component> {

    final DirectProcessor<Component> delegateProcessor = DirectProcessor.create();
    @Nullable
    private Component then = null;
    @Nullable
    private Component now = null;

    ChronoBox(Component component) {
        then = (Component) component.clone();
        now = component;
    }

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
