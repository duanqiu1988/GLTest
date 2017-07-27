package com.duanqiu.gltest.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.duanqiu.gltest.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class GamePlayer extends FrameLayout implements GLSurfaceView.Renderer {
    public static final String TAG = GamePlayer.class.getSimpleName();
    private GLSurfaceView glSurfaceView;
    private Game mGame;
    GestureDetector detector;
    OnGestureAdapter gestureAdapter;

    {
        this.gestureAdapter = new OnGestureAdapter() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                Log.d(TAG, "distanceX " + distanceX + ", distanceY " + distanceY);
                if (mGame != null) {
                    mGame.movePaddle(Math.abs(distanceX), distanceX < 0);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mGame.onTap();
                return super.onSingleTapUp(e);
            }
        };
    }

    public GamePlayer(@NonNull Context context) {
        super(context);
        init();
    }

    public GamePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        detector = new GestureDetector(getContext(), gestureAdapter);
    }

    @Override
    protected void onFinishInflate() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface);
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(this);
    }

    public void setGame(Game game) {
        mGame = game;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return detector.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mGame.init(getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mGame.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mGame.update(1);
        mGame.draw();
    }
}
