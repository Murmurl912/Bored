package com.example.body.simluator;

import com.example.body.universe.Body;

import java.util.Collection;

public interface NBodySimulationStrategy {

    double timestep();

    void simulate(Collection<Body> bodies);

}
