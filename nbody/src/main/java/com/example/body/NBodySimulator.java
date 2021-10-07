package com.example.body;

import java.util.Collection;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface NBodySimulator extends Runnable {

    WeakHashMap<NBodySimulator, Thread> simulatorMap = new WeakHashMap<>();

    Collection<Body> bodies();

    BlockingQueue<Collection<Body>> buffer();

    @Override
    default void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Collection<Body> bodies = bodies();
            BlockingQueue<Collection<Body>> buffer = buffer();
            update(bodies);
            Collection<Body> clone = bodies.parallelStream()
                    .map(Body::clone)
                    .collect(Collectors.toList());
            boolean success;
            do {
                try {
                    success = buffer.offer(clone, 1000 / 6, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } while (!success);
        }
    }

    void update(Collection<Body> bodies);

    default Thread thread() {
        return simulatorMap.get(this);
    }

    default void stop() {
        Thread thread = thread();
        if (thread != null) {
            thread.interrupt();
        }
    }

    default void start() {
        init();
        Thread thread = thread();
        if (thread != null && thread.isAlive()
                && !thread.isInterrupted()) {
            return;
        }
        thread = new Thread(this);
        simulatorMap.put(this, thread);
        thread.start();
    }

    default void init() {

    }

}
