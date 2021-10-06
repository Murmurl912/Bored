package com.example.body;

import java.util.Collection;
import java.util.WeakHashMap;

public interface NBodySimulator extends Runnable {

    WeakHashMap<NBodySimulator, Thread> simulatorMap = new WeakHashMap<>();

    Collection<Body> bodies();

    @Override
    default void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Collection<Body> bodies = bodies();
            synchronized (bodies) {
                update(bodies);
                bodies.notifyAll();
            }
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
