package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.render.CoordinateRenderer;

public class CoordinateActivity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new CoordinateRenderer(this);
    }
}
