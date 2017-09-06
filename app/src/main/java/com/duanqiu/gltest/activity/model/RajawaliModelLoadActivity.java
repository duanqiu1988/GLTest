package com.duanqiu.gltest.activity.model;

import android.app.Activity;
import android.os.Bundle;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.model.RajawaliLoadModelRenderer;

import org.rajawali3d.surface.RajawaliTextureView;

public class RajawaliModelLoadActivity extends Activity {
    private RajawaliTextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rajawali_model_load);
        textureView = (RajawaliTextureView) findViewById(R.id.rajwali_surface);
        textureView.setSurfaceRenderer(new RajawaliLoadModelRenderer(this));
    }
}
