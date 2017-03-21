package com.duanqiu.gltest;

import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

import com.duanqiu.gltest.render.FBORenderer;

public class FBOActivity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        FBORenderer renderer = new FBORenderer(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        renderer.setScreenSize(metrics.widthPixels, metrics.heightPixels);
        return renderer;
    }
}
