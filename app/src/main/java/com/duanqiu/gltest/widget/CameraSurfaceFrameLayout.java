package com.duanqiu.gltest.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.Camera;

/**
 * Created by jjduan on 3/24/17.
 */

public class CameraSurfaceFrameLayout extends FrameLayout {
    private GLSurfaceView glSurfaceView;
    private LinearLayout cameraLayout;
    private CameraProcessView keyboard;
    private CameraProcessView mouseMovement;
    private CameraProcessView mouseScroll;

    public CameraSurfaceFrameLayout(Context context) {
        super(context);
    }

    public CameraSurfaceFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface);
        glSurfaceView.setEGLContextClientVersion(3);
        cameraLayout = (LinearLayout) findViewById(R.id.camera_layout);
        keyboard = (CameraProcessView) findViewById(R.id.keyboard);
        mouseMovement = (CameraProcessView) findViewById(R.id.mouse_movement);
        mouseScroll = (CameraProcessView) findViewById(R.id.mouse_scroll);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        glSurfaceView.setRenderer(renderer);
        Camera.CameraProcessListener listener = (Camera.CameraProcessListener) renderer;
        keyboard.setKeyboardListener(listener);
        mouseMovement.setMouseMovementListener(listener);
        mouseScroll.setMouseScrollListener(listener);
    }

    public void onResume() {
        glSurfaceView.onResume();
    }

    public void onPause() {
        glSurfaceView.onPause();
    }
}
