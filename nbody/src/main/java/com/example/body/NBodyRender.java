package com.example.body;

import java.util.Collection;
import java.util.WeakHashMap;

public interface NBodyRender extends Runnable {

    WeakHashMap<NBodyRender, Thread> renderMap = new WeakHashMap<>();

    Collection<Body> bodies();

    @Override
    default void run() {
        while (!Thread.currentThread().isInterrupted()) {
           render();
        }
    }

    default void render() {
        Collection<Body> bodies = bodies();
        if (bodies == null) {
            return;
        }
        synchronized (bodies) {
            render(bodies);
            try {
                bodies.wait(1000 / 60);
            } catch (InterruptedException e) {
                // stop rendering
                thread().interrupt();
            }
        }
    }

    void render(Collection<Body> bodies);

    default Thread thread() {
        return renderMap.get(this);
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
        renderMap.put(this, thread);
        thread.start();
    }

    default void init() {

    }

}
