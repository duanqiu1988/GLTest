package com.duanqiu.gltest.activity.start;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.glsurface.TextureSurfaceView;
import com.duanqiu.gltest.render.start.TextureRenderer;

public class TextureActivity extends BaseActivity {

    @Override
    protected GLSurfaceView getGLSurfaceView() {
        return new TextureSurfaceView(this);
    }

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new TextureRenderer(this);
    }
}
