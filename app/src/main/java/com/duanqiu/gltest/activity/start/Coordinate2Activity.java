package com.duanqiu.gltest.activity.start;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.render.start.Coordinate2Renderer;

public class Coordinate2Activity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new Coordinate2Renderer(this);
    }
}
