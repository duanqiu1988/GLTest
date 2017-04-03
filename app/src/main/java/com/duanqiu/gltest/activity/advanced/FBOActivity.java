package com.duanqiu.gltest.activity.advanced;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.advanced.FBORenderer;

public class FBOActivity extends BaseCameraActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new FBORenderer(this);
    }
}
