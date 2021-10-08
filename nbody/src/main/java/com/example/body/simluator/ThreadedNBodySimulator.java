package com.example.body.simluator;

public interface ThreadedNBodySimulator extends NBodySimulator {

    /**
     * If simulation thread is stopped or not existed or been interrupted
     * Implementer should create a new thread.
     * Else return current simulation thread.
     * Hence, the returned thread must be alive or not started and not interrupted
     * @return simulation thread, to be started or running and not been interrupted
     */
    Thread thread(Runnable task);

    Thread thread();

    default void start() {
        synchronized (this) {
            Thread thread = thread(this::run);
            if (thread == null || thread.getState()
                    == Thread.State.TERMINATED
                    || thread.isInterrupted()) {
                throw new IllegalStateException("Thread is not alive or have been interrupted");
            }
            // thread either not started yet or running
            if (thread.getState() == Thread.State.NEW) {
                thread.start();
            }
        }
    }

    default void stop() {
        synchronized (this) {
            Thread thread = thread();
            if (thread != null
                    && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    default void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                simulate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                break;
            }
        }
    }

}
