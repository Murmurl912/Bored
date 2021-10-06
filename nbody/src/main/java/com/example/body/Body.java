package com.example.body;

/**
 * represent a body in our 2d universe
 */
public class Body {

    public static double G = 6.673e-11;

    public double sx; // absolute position x
    public double sy; // absolute position y

    public double mass; // mass of the body
    public double radius; // radius of the body
    public int color; // color of the body

    public double vx; // absolute velocity x
    public double vy; // absolute velocity y

    public double fx; // gravity force in x
    public double fy; // gravity force in y

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
        double dx = body.sx - sx;
        double dy = body.sy - sy;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void force(Body body) {
        double dx = body.sx - sx;
        double dy = body.sy - sy;
        double distanceSquare = dx * dx + dy + dy;
        double distance = Math.sqrt(distanceSquare);

        if (distance <= radius + body.radius) {
            // collision
            distanceSquare = Math.pow(radius + body.radius, 2);
        }

        double f = (G * mass * body.mass) / distanceSquare;

        if (f == Double.POSITIVE_INFINITY) {
            // avoid infinity
            f = 0;
        }

        // update force
        fx += f * dx / distance;
        fy += f * dy / distance;
    }

    public void resetForce() {
        fx = 0;
        fy = 0;
    }

    @Override
    public String toString() {
        return "[s = (" + sx + ", " + sy + "), " +
                "v = (" + vx + ", " + vy + ", " +
                "f = (" + fx + ", " + fy + ", " +
                "m = " + mass + "]";
    }
}
