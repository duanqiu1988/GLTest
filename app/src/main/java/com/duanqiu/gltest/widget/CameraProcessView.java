package com.duanqiu.gltest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.LogUtil;

/**
 * Created by jjduan on 3/24/17.
 */

public class CameraProcessView extends TextView {
    private static final String TAG = "CameraProcessView";
    private static final int NONE = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;
    private Camera.CameraProcessListener mKeyboardListener;
    private Camera.CameraProcessListener mMouseMovementListener;
    private Camera.CameraProcessListener mMouseScrollListener;
    private int pressArea = NONE;
    private static final int INTERVAL = 20;
    private int count = 0;
    private Drawable bgDrawable = new ColorDrawable(Color.parseColor("#22000000"));
    Paint paint;
    private Runnable presseRunnable = new Runnable() {
        @Override
        public void run() {
            if (pressArea != NONE) {
                switch (pressArea) {
                    case LEFT:
                        if (mKeyboardListener != null) {
                            mKeyboardListener.processKeyboard(Camera.CameraMovement.LEFT);
                        }
                        if (mMouseMovementListener != null) {
                            mMouseMovementListener.processMouseMovement(-(++count * INTERVAL), 0);
                        }
                        break;
                    case UP:
                        if (mKeyboardListener != null) {
                            mKeyboardListener.processKeyboard(Camera.CameraMovement.FORWARD);
                        }
                        if (mMouseMovementListener != null) {
                            mMouseMovementListener.processMouseMovement(0, ++count * INTERVAL);
                        }
                        if (mMouseScrollListener != null) {
                            mMouseScrollListener.processMouseScroll(++count * INTERVAL);
                        }
                        break;
                    case RIGHT:
                        if (mKeyboardListener != null) {
                            mKeyboardListener.processKeyboard(Camera.CameraMovement.RIGHT);
                        }
                        if (mMouseMovementListener != null) {
                            mMouseMovementListener.processMouseMovement(++count * INTERVAL, 0);
                        }
                        break;
                    case DOWN:
                        if (mKeyboardListener != null) {
                            mKeyboardListener.processKeyboard(Camera.CameraMovement.BACKWARD);
                        }
                        if (mMouseMovementListener != null) {
                            mMouseMovementListener.processMouseMovement(0, -++count * INTERVAL);
                        }
                        if (mMouseScrollListener != null) {
                            mMouseScrollListener.processMouseScroll(-++count * INTERVAL);
                        }
                        break;
                }

                postDelayed(this, INTERVAL);
            }
        }
    };

    public CameraProcessView(Context context) {
        super(context);
        initPaint();
    }

    public CameraProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        setWillNotDraw(false);
    }

    Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        LogUtil.d(TAG, "w: " + width + ", h:" + height);

        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        path.reset();

        switch (pressArea) {
            case LEFT:
                path.moveTo(0, 0);
                path.lineTo(width / 2, width / 2);
                path.lineTo(0, height);
                path.lineTo(0, 0);
                break;
            case UP:
                path.moveTo(0, 0);
                path.lineTo(width / 2, width / 2);
                path.lineTo(width, 0);
                path.lineTo(0, 0);
                break;
            case RIGHT:
                path.moveTo(width, 0);
                path.lineTo(width / 2, width / 2);
                path.lineTo(width, height);
                path.lineTo(width, 0);
                break;
            case DOWN:
                path.moveTo(0, height);
                path.lineTo(width / 2, width / 2);
                path.lineTo(width, height);
                path.lineTo(0, height);
                break;
        }
        canvas.drawPath(path, paint);

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(width, height);
        canvas.drawPath(path, paint);

        path.reset();
        path.moveTo(width, 0);
        path.lineTo(0, height);
        canvas.drawPath(path, paint);

        bgDrawable.draw(canvas);

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressArea = getPressArea(event);
                LogUtil.d(getClass().getSimpleName(), String.valueOf(pressArea));
                post(presseRunnable);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pressArea = NONE;
                count = 0;
                break;
        }

        invalidate();
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
