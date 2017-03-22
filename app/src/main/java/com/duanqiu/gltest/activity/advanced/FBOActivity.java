package com.duanqiu.gltest.activity.advanced;

import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

import com.duanqiu.gltest.BaseActivity;
import com.duanqiu.gltest.render.advanced.FBORenderer;

public class FBOActivity extends BaseActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        FBORenderer renderer = new FBORenderer(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        renderer.setScreenSize(metrics.widthPixels, metrics.heightPixels);
        return renderer;
    }
}
