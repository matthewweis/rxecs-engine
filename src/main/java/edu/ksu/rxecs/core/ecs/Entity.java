package edu.ksu.rxecs.core.ecs;

import reactor.core.publisher.Flux;

public interface Entity {

    Flux<Component> getComponents();

}
