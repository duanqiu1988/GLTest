package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duanqiu.gltest.render.TransformationRenderer;

public class TransformationActivity extends AppCompatActivity {
    GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(new TransformationRenderer(this));
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
