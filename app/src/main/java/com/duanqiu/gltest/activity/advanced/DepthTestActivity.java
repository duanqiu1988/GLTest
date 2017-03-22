package com.duanqiu.gltest.activity.advanced;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.render.advanced.DepthTestRenderer;

public class DepthTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new DepthTestRenderer(this);
    }
}
