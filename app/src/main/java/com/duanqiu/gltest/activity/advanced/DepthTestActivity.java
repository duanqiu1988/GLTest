package com.duanqiu.gltest.activity.advanced;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.advanced.DepthTestRenderer;

public class DepthTestActivity extends BaseCameraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new DepthTestRenderer(this);
    }
}
