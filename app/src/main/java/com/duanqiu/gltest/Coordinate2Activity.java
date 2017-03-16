package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.render.CoordinateRenderer2;

public class Coordinate2Activity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new CoordinateRenderer2(this);
    }
}
