package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.render.HelloTriangleRenderer;

public class HelloTriangleActivity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new HelloTriangleRenderer();
    }
}
