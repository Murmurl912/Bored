package com.example.body;

import java.util.Objects;
import java.util.UUID;

/**
 * represent a body in our 2d universe
 */
public class Body implements Cloneable {

    public static double G = 6.673e-11;

    public String id;

    public double sx; // absolute position x
    public double sy; // absolute position y

    public double mass; // mass of the body
    public double radius; // radius of the body
    public double drawRadius; // radius use for drawing
    public int color; // color of the body

    public double vx; // absolute velocity x
    public double vy; // absolute velocity y

    public double fx; // gravity force in x
    public double fy; // gravity force in y

    public Body() {
        id = UUID.randomUUID().toString();
    }

    public Body(String id,
                double sx,
                double sy,
                double mass,
                double radius,
                int color,
                double vx,
                double vy,
                double fx,
                double fy) {
        this.id = id;
        this.sx = sx;
        this.sy = sy;
        this.mass = mass;
        this.radius = radius;
        this.color = color;
        this.vx = vx;
        this.vy = vy;
        this.fx = fx;
        this.fy = fy;
    }

    /**
     * update current velocity and position using a timestep dt
     * @param dt timestep
     */
    public void update(double dt) {
        vx += dt * fx / mass;
        vy += dt * fy / mass;
        sx += dt * vx;
        sy += dt * vy;
    }

    /**
     * calculate distance between two bodies
     * @param body another body
     * @return distance between to another body
     */
    public double distance(Body body) {
        double dx = sx - body.sx;
        double dy = sy - body.sy;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void force(Body body) {
        double dx = body.sx - sx;
        double dy = body.sy - sy;

        double distanceSquare = dx * dx + dy * dy;
        double distance = Math.sqrt(distanceSquare);

        double f = (G * mass * body.mass) / distanceSquare;

        if (Double.isNaN(f) || Double.isInfinite(f)) {
            f = 0D;
        }

        // update force
        fx += f * (dx / distance);
        fy += f * (dy / distance);
    }

    public void resetForce() {
        fx = 0D;
        fy = 0D;
    }

    @Override
    public Body clone() {
        try {
            return (Body) super.clone();
        } catch (CloneNotSupportedException e) {
            // failed to clone
            Body body = new Body();
            body.id = id;
            body.radius = radius;
            body.mass = mass;
            body.sx = sx;
            body.sy = sy;
            body.vx = vx;
            body.vy = vy;
            body.color = color;
            body.fx = fx;
            body.fy = fy;

            return body;
        }
    }

    @Override
    public String toString() {
        return "[s = (" + sx + ", " + sy + "), " +
                "v = (" + vx + ", " + vy + "), " +
                "f = (" + fx + ", " + fy + "), " +
                "m = " + mass + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return id.equals(body.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
