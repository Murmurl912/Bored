package com.example.body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BruteForceNBodySimulator implements NBodySimulator {

    private final BlockingQueue<Collection<Body>> buffer
            = new ArrayBlockingQueue<>(100000);
    private final Collection<Body> bodies = new ArrayList<>();
    private double timestep = 1e4;

    @Override
    public Collection<Body> bodies() {
        return bodies;
    }

    @Override
    public BlockingQueue<Collection<Body>> buffer() {
        return buffer;
    }

    @Override
    public void update(Collection<Body> bodies) {
        bodies.stream()
                .parallel()
                .forEach(Body::resetForce);
        bodies.stream()
                .parallel()
                .forEach(body -> {
                    for (Body another : bodies) {
                        if (!body.equals(another)) {
                            body.force(another);
                        }
                    }
                });
        bodies.stream()
                .parallel()
                .forEach(body -> {
                    body.update(timestep);
                });
        System.out.println(bodies);
    }

    public void init() {
        solar();
        buffer.offer(bodies);
    }

    public void solar() {
        Random random = new Random(System.currentTimeMillis());

        Body sun = new Body();
        sun.id = "sun";
        sun.mass = 1.9885e30;
        sun.radius = 6.957e8;
        sun.drawRadius = 24;
        sun.color = random.nextInt();

        Body mercury = new Body();
        mercury.id = "mercury";
        mercury.mass = 0.33011e24;
        mercury.radius = 2440.5e3;
        mercury.sy = 69.817e9;
        mercury.vx = 38.86e3;
        mercury.drawRadius = 12;
        mercury.color = random.nextInt();

        Body venus = new Body();
        venus.id = "venus";
        venus.mass = 4.8675e24;
        venus.radius = 6051.8e3;
        venus.sy = 108.939e9;
        venus.vx = 34.79e3;
        venus.drawRadius = 12;
        venus.color = random.nextInt();

        Body earth = new Body();
        earth.id = "earth";
        earth.mass = 5.9724e24;
        earth.sy = 152.099e9; // 152.099e6 km
        earth.vx = 29.29e3; // 29.29km
        earth.radius = 6.371e6; // 6371km
        earth.drawRadius = 12;
        earth.color = random.nextInt();

        Body moon = new Body();
        moon.id = "moon";
        moon.mass = 0.07346e24;
        moon.sy = earth.sy + 0.4055e9; // moon to earth 0.4055e6 km
        moon.vx = earth.vx + 0.970e3; // min orbital velocity to earth 0.970 km//s
        moon.radius = 1.7374e6; // radius 1737.4 km
        moon.drawRadius = 6;
        moon.color =  random.nextInt();

        Body mars = new Body();
        mars.id = "mars";
        mars.mass = 0.64171e24;
        mars.radius = 3389.5e3;
        mars.sy = 249.229e9;
        mars.vx = 21.97e3;
        mars.color = random.nextInt();
        mars.drawRadius = 12;

        Body jupiter = new Body();
        jupiter.id = "jupiter";
        jupiter.mass = 1898.19e24;
        jupiter.radius = 71492e3;
        jupiter.sy = 816.618e9;
        jupiter.vx = 12.44e3;
        jupiter.color = random.nextInt();
        jupiter.drawRadius = 12;

        Body saturn = new Body();
        saturn.id = "saturn";
        saturn.mass = 568.34e24;
        saturn.radius = 58232e3;
        saturn.sy = 1514.504e9;
        saturn.vx = 9.09e3;
        saturn.drawRadius = 12;
        saturn.color = random.nextInt();

        Body uranus = new Body();
        uranus.id = "uranus";
        uranus.mass = 86.813e24;
        uranus.radius = 25362e3;
        uranus.sy = 3003.625e9;
        uranus.vx = 6.49e3;
        uranus.drawRadius = 12;
        uranus.color = random.nextInt();

        bodies.add(earth);
        bodies.add(moon);
        bodies.add(sun);
        bodies.add(mercury);
        bodies.add(venus);
        bodies.add(mars);
        bodies.add(jupiter);
        bodies.add(saturn);
        bodies.add(uranus);
    }

    public void asteroid () {
        Body body = new Body();
        body.mass = (0.000035 + Math.random()) * 939300e15;
        body.radius = (0.1 + Math.random()) * 900e3;
    }
}
