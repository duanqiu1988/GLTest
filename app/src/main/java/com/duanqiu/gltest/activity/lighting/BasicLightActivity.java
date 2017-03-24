package com.duanqiu.gltest.activity.lighting;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.lighting.BasicLightRenderer;

public class BasicLightActivity extends BaseCameraActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new BasicLightRenderer(this);
    }
}
