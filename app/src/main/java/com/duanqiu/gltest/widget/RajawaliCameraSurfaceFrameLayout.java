package com.duanqiu.gltest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.RajawaliBaseRenderer;

import org.rajawali3d.surface.RajawaliTextureView;

/**
 * Created by jjduan on 3/24/17.
 */

public class RajawaliCameraSurfaceFrameLayout extends FrameLayout {
    private RajawaliTextureView glSurfaceView;
    private LinearLayout cameraLayout;
    private CameraProcessView keyboard;
    private CameraProcessView mouseMovement;
    private CameraProcessView mouseScroll;

    public RajawaliCameraSurfaceFrameLayout(Context context) {
        super(context);
    }

    public RajawaliCameraSurfaceFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        glSurfaceView = (RajawaliTextureView) findViewById(R.id.rajawali_surface);
        cameraLayout = (LinearLayout) findViewById(R.id.camera_layout);
        keyboard = (CameraProcessView) findViewById(R.id.keyboard);
        mouseMovement = (CameraProcessView) findViewById(R.id.mouse_movement);
        mouseScroll = (CameraProcessView) findViewById(R.id.mouse_scroll);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        glSurfaceView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        cameraLayout.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (width / 3.0f + 0.5f), MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        glSurfaceView.layout(left, top, right, bottom);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        cameraLayout.layout(left, height - width / 3, right, bottom);
    }

    public void setRenderer(RajawaliBaseRenderer renderer) {
        glSurfaceView.setSurfaceRenderer(renderer);
        keyboard.setKeyboardListener(renderer);
        mouseMovement.setMouseMovementListener(renderer);
        mouseScroll.setMouseScrollListener(renderer);
    }

    public void onResume() {
        glSurfaceView.onResume();
    }

    public void onPause() {
        glSurfaceView.onPause();
    }
}
