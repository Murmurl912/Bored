package com.example.body.simluator;

import com.example.body.universe.Body;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public interface NBodySimulator {

    Collection<Body> bodies();

    BlockingQueue<Collection<Body>> buffer();

    NBodySimulationStrategy strategy();

    default void simulate() throws InterruptedException {
        Collection<Body> bodies = bodies();
        strategy().simulate(bodies);
        Collection<Body> result = bodies.stream().parallel()
                .map(Body::clone)
                .collect(Collectors.toList());
        buffer().put(result);
    }

}
