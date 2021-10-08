package com.example.body.render;

import com.example.body.universe.Body;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public interface NBodyRender {

    BlockingQueue<Collection<Body>> buffer();

    void render(Collection<Body> bodies);

    default void render() throws InterruptedException {
        Collection<Body> bodies = buffer().take();
        render(bodies);
    }

}
