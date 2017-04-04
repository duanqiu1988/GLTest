package com.duanqiu.gltest.activity.advanced;

import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.BaseCameraActivity;
import com.duanqiu.gltest.render.advanced.FboInversionRenderer;

public class FBOInversionActivity extends BaseCameraActivity {

    @Override
    protected GLSurfaceView.Renderer getRenderer() {
        return new FboInversionRenderer(this);
    }
}
