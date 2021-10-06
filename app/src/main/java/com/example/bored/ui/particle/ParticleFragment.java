package com.example.bored.ui.particle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.body.Body;
import com.example.body.NBodySimulator;
import com.example.bored.databinding.FragmentParticleBinding;


public class ParticleFragment extends Fragment {

    public static final String TAG = ParticleFragment.class.getSimpleName();

    private FragmentParticleBinding binding;
    private NBodySimulation simulation;

    public ParticleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentParticleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.play.setOnClickListener(v -> {
            binding.play.setEnabled(false);
            binding.pause.setEnabled(true);
            simulation = new NBodySimulation();
            simulation.start(binding.surfaceView.getHolder());
        });
        binding.pause.setOnClickListener(v -> {
            binding.play.setEnabled(true);
            binding.pause.setEnabled(false);
            simulation.stop();
        });
    }
}