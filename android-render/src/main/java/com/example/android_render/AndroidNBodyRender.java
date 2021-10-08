package com.example.android_render;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.SurfaceHolder;
import com.example.body.render.ThreadedNBodyRender;
import com.example.body.simluator.NBodySimulator;
import com.example.body.universe.Body;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiFunction;

public class AndroidNBodyRender implements ThreadedNBodyRender {

    public static final String TAG = AndroidNBodyRender.class.getSimpleName();

    private Thread thread;
    private BlockingQueue<Collection<Body>> buffer;
    private HashMap<Body, Path> paths;

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

    private double fps;
    private long count;
    private long time;

    public AndroidNBodyRender(NBodySimulator simulator, SurfaceHolder holder) {
        init(holder, simulator.buffer());
        paths = new HashMap<>();
    }

    @Override
    public BlockingQueue<Collection<Body>> buffer() {
        return buffer;
    }

    @Override
    synchronized public Thread thread(Runnable task) {
        if (thread == null || !thread.isAlive()
                || thread.isInterrupted()) {
            thread = new Thread(task);
        }
        return thread;
    }

    synchronized public Thread thread() {
        return thread;
    }

    @Override
    public void render() {
        if (System.currentTimeMillis() - time >= 1000) {
            fps = 1000.0d * count / (System.currentTimeMillis() - time) ;
            time = System.currentTimeMillis();
            count = 0;
        } else {
            count++;
        }
        try {
            ThreadedNBodyRender.super.render();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
            paint.setTextSize(40);
            paint.setColor(Color.BLACK);
            canvas.drawText(String.format("%3.2f FPS", fps),
                    20, 60, paint);
            BlockingQueue<Collection<Body>> buffer = buffer();
            int size = buffer.size();
            canvas.drawText(String.format("buffer size: %d", size), 20, (float) (height - 60), paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    protected void draw(Canvas canvas, Collection<Body> bodies) {
        canvas.drawColor(backgroundColor);
        bodies.forEach(body -> draw(canvas, body));
    }

    protected void draw(Canvas canvas, Body body) {
        float x = Math.round(body.sx * scale);
        float y = Math.round(body.sy * scale);
        float r = Math.round(12);
        paint.setStrokeWidth(4);

        // draw body
        paint.setColor(body.color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, paint);
        canvas.drawText(body.id, x + r + 20, y + 10, paint);

        // draw path
        Path path = paths.computeIfPresent(body, (b, p) -> {
            p.lineTo(x, y);
            return p;
        });
        paths.compute(body, new BiFunction<Body, Path, Path>() {
            @Override
            public Path apply(Body body, Path path) {
                if (path == null) {
                    path = new Path();
                    path.moveTo(x, y);
                    path.close();
                    return path;
                }
                path.lineTo(x, y);
                return path;
            }
        });
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
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

    public void init(SurfaceHolder holder, BlockingQueue<Collection<Body>> buffer) {
        this.holder = holder;
        this.buffer = buffer;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

}
