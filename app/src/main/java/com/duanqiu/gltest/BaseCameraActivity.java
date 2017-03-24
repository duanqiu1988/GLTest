package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.glsurface.CameraSurfaceView;

/**
 * Created by jjduan on 3/16/17.
 */

public abstract class BaseCameraActivity extends BaseActivity {
    @Override
    protected GLSurfaceView getGLSurfaceView() {
        return new CameraSurfaceView(this);
    }
}
