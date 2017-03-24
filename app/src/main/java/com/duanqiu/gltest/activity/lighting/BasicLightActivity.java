package com.duanqiu.gltest.activity.lighting;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.render.lighting.BasicLightRenderer;

public class BasicLightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new BasicLightRenderer(this);
    }
}
