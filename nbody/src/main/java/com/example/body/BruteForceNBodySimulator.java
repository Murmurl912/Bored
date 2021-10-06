package com.example.body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class BruteForceNBodySimulator implements NBodySimulator {

    private final Collection<Body> bodies = new ArrayList<>();
    private double timestep = 1e11;

    @Override
    public Collection<Body> bodies() {
        Objects.requireNonNull(bodies);
        return bodies;
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
                        if (body != another) {
                            body.force(another);
                        }
                    }
                });
        bodies.stream()
                .parallel()
                .forEach(body -> {
                    body.update(timestep);
                    System.out.println(body);
                });
    }

    public void init() {
        init(100);
    }

    public void init(int count) {
        double radius = 1e18; // universe radius
        double mass = 1.98892e30;

        for (int i = 0; i < count; i++) {
            double px = radius * Math.exp(-1.8) * (0.5 - Math.random());
            double py = radius * Math.exp(-1.8) * (0.5 - Math.random());
            double magv = Math.sqrt(
                    Body.G * 1e6 * mass /
                            Math.sqrt(px * px + py * py)
            );
            double absangle = Math.atan(Math.abs(py / px));
            double thetav = Math.PI / 2 - absangle;
            double phiv = Math.random() * Math.PI;
            double vx = -Math.signum(py) * Math.cos(thetav) * magv;
            double vy = Math.signum(px) * Math.sin(thetav) * magv;

            if (Math.random() <= 0.5) {
                vx = -vx;
                vy = -vy;
            }

            double m = Math.random() * mass * 10 + 1e20;
            int color = (int) (Math.random() * Integer.MAX_VALUE);
            Body body = new Body();
            body.sx = px;
            body.sy = py;
            body.vx = vx;
            body.vy  = vy;
            body.color = color;
            body.mass = m;
            body.radius = 8;
            bodies.add(body);
        }
        Body body = new Body();
        body.vx = 0;
        body.vy = 0;
        body.sx = 0;
        body.sy = 0;
        body.mass = 1e6 * mass;
        body.color = 0x00FF0000;
        body.radius = 16;
        bodies.add(body);
    }

}
