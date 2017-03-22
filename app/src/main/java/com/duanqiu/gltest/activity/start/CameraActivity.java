package com.duanqiu.gltest.activity.start;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.glsurface.CameraSurfaceView;
import com.duanqiu.gltest.render.start.CameraRenderer;

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
