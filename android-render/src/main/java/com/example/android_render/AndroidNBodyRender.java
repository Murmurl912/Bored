package com.example.android_render;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import com.example.body.Body;
import com.example.body.NBodyRender;
import com.example.body.NBodySimulator;

import java.util.Collection;
import java.util.Objects;

public class AndroidNBodyRender implements NBodyRender {

    public static final String TAG = AndroidNBodyRender.class.getSimpleName();

    private Collection<Body> bodies;

    private SurfaceHolder holder;
    private Paint paint;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    public static final double DEFAULT_SCALE = 1D;
    private double scale = DEFAULT_SCALE;

    private double width;
    private double height;
    private double tx;
    private double ty;

    private long time;
    public AndroidNBodyRender(NBodySimulator simulator, SurfaceHolder holder) {
        init(holder, simulator.bodies());
    }

    @Override
    public Collection<Body> bodies() {
        Objects.requireNonNull(bodies);
        return bodies;
    }

    @Override
    public void render() {
        time = System.currentTimeMillis();
        NBodyRender.super.render();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void render(Collection<Body> bodies) {
        if (holder == null) {
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            return;
        }

        width = canvas.getWidth();
        height = canvas.getHeight();

        try {
            canvas.translate(Math.round(tx + width / 2), Math.round(ty + height / 2));
            draw(canvas, bodies);
            canvas.translate(-Math.round(tx + width / 2), -Math.round(ty + height / 2));
        } catch (Exception e) {
            Log.d(TAG, "failed to render frame: " + bodies, e);
        } finally {
            long duration = System.currentTimeMillis() - time;
            paint.setTextSize(40);
            paint.setColor(Color.BLACK);
            canvas.drawText(String.format("%3.2f FPS",
                            (1000f / duration)),
                    20, 60, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    protected void draw(Canvas canvas, Collection<Body> bodies) {
        canvas.drawColor(backgroundColor);
        bodies.stream()
                .forEach(body -> {
                    draw(canvas, body);
                });
    }

    protected void draw(Canvas canvas, Body body) {
        paint.setColor(body.color);
        canvas.drawCircle(Math.round(body.sx * scale),
                Math.round(body.sy * scale),
                Math.round(body.radius * scale),
                paint);
    }

    public void scale(double scale) {
        if (scale <= 0) {
            throw new IllegalArgumentException("scale must larger than zero");
        }
        this.scale = scale;
    }

    public double scale() {
        return scale;
    }

    public void moveBy(double dx, double dy) {
        tx += dx;
        ty += dy;
    }

    public void moveTo(double x, double y) {
        tx = x;
        ty = y;
    }

    public double x() {
        return tx;
    }

    public double y() {
        return ty;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public void background(int color) {
        backgroundColor = color;
    }

    public int background() {
        return backgroundColor;
    }

    public void init(SurfaceHolder holder, Collection<Body> bodies) {
        this.holder = holder;
        this.bodies = bodies;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

}
