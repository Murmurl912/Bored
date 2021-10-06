package com.example.bored.ui.particle;

import android.view.SurfaceHolder;
import com.example.android_render.AndroidNBodyRender;
import com.example.body.BruteForceNBodySimulator;
import com.example.body.NBodyRender;
import com.example.body.NBodySimulator;

public class NBodySimulation {

    private NBodySimulator simulator;
    private AndroidNBodyRender render;

    public void start(SurfaceHolder holder) {
        simulator = new BruteForceNBodySimulator();
        render = new AndroidNBodyRender(simulator, holder);
        simulator.start();
        render.start();
        render.scale(20);
    }

    public void stop() {
        render.stop();
        simulator.stop();
    }

}
