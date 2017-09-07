package com.duanqiu.gltest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duanqiu.gltest.render.RajawaliBaseRenderer;
import com.duanqiu.gltest.widget.RajawaliCameraSurfaceFrameLayout;

/**
 * Created by jjduan on 3/16/17.
 */

public abstract class RajawaliBaseCameraActivity extends AppCompatActivity {
    private RajawaliCameraSurfaceFrameLayout cameraSurfaceFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_rajawali_camera);
        cameraSurfaceFrameLayout = (RajawaliCameraSurfaceFrameLayout) findViewById(R.id.camera_surface_layout);
        cameraSurfaceFrameLayout.setRenderer(getRenderer());
        setTitle(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraSurfaceFrameLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSurfaceFrameLayout.onPause();
    }

    protected abstract RajawaliBaseRenderer getRenderer();
}
