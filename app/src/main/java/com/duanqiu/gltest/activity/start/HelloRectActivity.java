package com.duanqiu.gltest.activity.start;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.render.start.HelloRectRenderer;

public class HelloRectActivity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new HelloRectRenderer(this);
    }
}
