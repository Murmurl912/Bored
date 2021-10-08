package com.example.body.simluator;

import com.example.body.universe.Body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SimpleNBodySimulator implements ThreadedNBodySimulator {
    private Thread thread;
    private final Collection<Body> bodies;
    private final ArrayBlockingQueue<Collection<Body>> queue;
    private NBodySimulationStrategy strategy;

    public SimpleNBodySimulator() {
        bodies = new ArrayList<>();
        queue = new ArrayBlockingQueue<>(1000);
        strategy = new BruteForceStrategy();
    }
    @Override
    public Collection<Body> bodies() {
        return bodies;
    }

    @Override
    public BlockingQueue<Collection<Body>> buffer() {
        return queue;
    }

    @Override
    public NBodySimulationStrategy strategy() {
        return strategy;
    }

    @Override
    synchronized public Thread thread(Runnable task) {
        if (thread == null || !thread.isAlive()
                || thread.isInterrupted()) {
            thread = new Thread(task);
        }
        return thread;
    }

    synchronized public Thread thread() {
        return thread;
    }
}
