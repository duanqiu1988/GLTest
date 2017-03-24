package com.duanqiu.gltest.activity.lighting;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.glsurface.CameraSurfaceView;
import com.duanqiu.gltest.render.lighting.ColorRenderer;

public class ColorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GLSurfaceView getGLSurfaceView() {
        return new CameraSurfaceView(this);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new ColorRenderer(this);
    }
}
