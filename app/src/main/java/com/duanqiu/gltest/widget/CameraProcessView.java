package com.duanqiu.gltest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.LogUtil;

/**
 * Created by jjduan on 3/24/17.
 */

public class CameraProcessView extends TextView {
    private static final int NONE = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;
    private Camera.CameraProcessListener mKeyboardListener;
    private Camera.CameraProcessListener mMouseMovementListener;
    private Camera.CameraProcessListener mMouseScrollListener;
    private int pressArea = NONE;

    public CameraProcessView(Context context) {
        super(context);
    }

    public CameraProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressArea = getPressArea(event);
                LogUtil.d(getClass().getSimpleName(), String.valueOf(pressArea));
                break;
            case MotionEvent.ACTION_UP:
                pressArea = NONE;
                break;
        }

        return true;
    }

    private int getPressArea(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        float x = event.getX();
        float y = event.getY();
        if (x == 0 || x == width || y == 0 || y == width) {
            return NONE;
        }

        if (y / x < height / width) {
            if (y / (width - x) < height / width) {
                return UP;
            } else if ((height - y) / x < height / width) {
                return RIGHT;
            }
        } else {
            if (y / (width - x) < height / width) {
                return LEFT;
            } else if ((height - y) / x < height / width) {
                return DOWN;
            }
        }


        return NONE;
    }

    public void setKeyboardListener(Camera.CameraProcessListener mKeyboardListener) {
        this.mKeyboardListener = mKeyboardListener;
    }

    public void setMouseMovementListener(Camera.CameraProcessListener mMouseMovementListener) {
        this.mMouseMovementListener = mMouseMovementListener;
    }

    public void setMouseScrollListener(Camera.CameraProcessListener mMouseScrollListener) {
        this.mMouseScrollListener = mMouseScrollListener;
    }
}
