package com.example.body;

import java.util.Collection;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public interface NBodyRender extends Runnable {

    WeakHashMap<NBodyRender, Thread> renderMap = new WeakHashMap<>();

    BlockingQueue<Collection<Body>> buffer();

    default Collection<Body> bodies() throws InterruptedException {
        BlockingQueue<Collection<Body>> buffer = buffer();
        if (buffer != null) {
            return buffer.take();
        }
        return null;
    }

    @Override
    default void run() {
        while (!Thread.currentThread().isInterrupted()) {
           render();
        }
    }

    default void render() {
        try {
            Collection<Body> bodies = bodies();
            if (bodies == null) {
                return;
            }
            render(bodies);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        thread.setPriority(Thread.MAX_PRIORITY);
        renderMap.put(this, thread);
        thread.start();
    }

    default void init() {

    }

}
