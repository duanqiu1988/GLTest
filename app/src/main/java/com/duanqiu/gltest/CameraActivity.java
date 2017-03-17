package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.glsurface.CameraSurfaceView;
import com.duanqiu.gltest.render.CameraRenderer;

public class CameraActivity extends BaseActivity {

    @Override
    protected GLSurfaceView getGLSurfaceView() {
        return new CameraSurfaceView(this);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new CameraRenderer(this);
    }
}
