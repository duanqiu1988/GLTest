package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.render.Coordinate2Renderer;

public class Coordinate2Activity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new Coordinate2Renderer(this);
    }
}
