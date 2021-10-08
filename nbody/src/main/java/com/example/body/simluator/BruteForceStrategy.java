package com.example.body.simluator;

import com.example.body.universe.Body;

import java.util.Collection;

public class BruteForceStrategy implements NBodySimulationStrategy {

    private double timestep = 1e4;

    @Override
    public double timestep() {
        return timestep;
    }

    public void timestep(double timestep) {
        this.timestep = timestep;
    }

    @Override
    public void simulate(Collection<Body> bodies) {
        bodies.parallelStream()
                .forEach(Body::resetForce);
        bodies.parallelStream()
                .forEach(body -> bodies.parallelStream()
                        .forEach(body::force)
                );
        bodies.parallelStream()
                .forEach(body -> {
                    body.update(timestep);
                });
    }

}
