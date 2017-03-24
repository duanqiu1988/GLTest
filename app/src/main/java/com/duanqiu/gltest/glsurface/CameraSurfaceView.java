package com.duanqiu.gltest.glsurface;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

/**
 * Created by jjduan on 3/17/17.
 */

public class CameraSurfaceView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "CameraSurfaceView";
    private OnGestureListener gestureListener;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1;
    private float x;
    private float y;
    private boolean pressed = false;
    private boolean onScale = false;
    private final int touchSlop;

    public CameraSurfaceView(Context context) {
        super(context);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                pressed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pressed && !onScale) {

                    float absX = Math.abs(event.getX() - x);
                    float absY = Math.abs(event.getY() - y);

                    if (absX < touchSlop && absY < touchSlop) {
                        break;
                    }

                    if (absX > absY) {
                        if (event.getX() > x) {
                            gestureListener.onX(false);
                        } else {
                            gestureListener.onX(true);
                        }
                    } else {
                        if (event.getY() > y) {
                            gestureListener.onY(false);
                        } else {
                            gestureListener.onY(true);
                        }
                    }
                }
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                break;
        }
        return true;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        gestureListener = (OnGestureListener) renderer;
        super.setRenderer(renderer);
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        gestureListener.onZ(detector.getScaleFactor() < scale);
        scale = scaleGestureDetector.getScaleFactor();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        onScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        onScale = false;
    }

    public interface OnGestureListener {
        void onX(boolean left);

        void onY(boolean top);

        void onZ(boolean pinchIn);
    }
}
