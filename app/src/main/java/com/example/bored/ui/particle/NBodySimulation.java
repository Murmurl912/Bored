package com.example.bored.ui.particle;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import com.example.android_render.AndroidNBodyRender;
import com.example.body.simluator.SimpleNBodySimulator;
import com.example.body.simluator.ThreadedNBodySimulator;
import com.example.body.universe.SolarSystem;

public class NBodySimulation implements SurfaceHolder.Callback2,
        GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    public static final String TAG = NBodySimulation.class.getSimpleName();

    private View gesture;
    private GestureDetector detector;
    private ThreadedNBodySimulator simulator;
    private AndroidNBodyRender render;

    public void start(View gesture, SurfaceHolder holder) {
        this.gesture = gesture;
        simulator = new SimpleNBodySimulator();
        SolarSystem.solar(simulator.bodies());

        render = new AndroidNBodyRender(simulator, holder);
        simulator.start();
        render.scale(1.5e-9);
        if (holder.getSurface().isValid()) {
            surfaceCreated(holder);
        }
        holder.addCallback(this);
        detector = new GestureDetector(gesture.getContext(), this);
    }

    public void stop() {
        render.stop();
        simulator.stop();
    }

    @Override
    public void surfaceRedrawNeeded(@NonNull SurfaceHolder holder) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d(TAG, "render thread started");
        render.start();
        gesture.setOnTouchListener((v, event) ->
                detector.onTouchEvent(event));
        gesture.setOnGenericMotionListener((v, event) ->
                detector.onGenericMotionEvent(event));
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d(TAG, "render thread stopped");
        render.stop();
        gesture.setOnTouchListener(null);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        return false;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


}
