package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.glsurface.TextureSurfaceView;
import com.duanqiu.gltest.render.TextureRenderer;

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
