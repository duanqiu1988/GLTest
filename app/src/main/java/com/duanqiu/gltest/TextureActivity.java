package com.duanqiu.gltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duanqiu.gltest.glsurface.TextureSurfaceView;
import com.duanqiu.gltest.render.TextureRenderer;

public class TextureActivity extends AppCompatActivity {
    TextureSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new TextureSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(new TextureRenderer(this));
        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
