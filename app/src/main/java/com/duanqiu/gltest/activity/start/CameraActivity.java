package com.duanqiu.gltest.activity.start;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.start.CameraRenderer;

public class CameraActivity extends BaseCameraActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new CameraRenderer(this);
    }
}
