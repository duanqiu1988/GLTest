package com.duanqiu.gltest.activity.lighting;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.lighting.LightMapRenderer;

public class LightMapActivity extends BaseCameraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new LightMapRenderer(this);
    }
}
