package com.duanqiu.gltest.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.duanqiu.gltest.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class GamePlayer extends FrameLayout implements GLSurfaceView.Renderer {
    private GLSurfaceView glSurfaceView;
    private Game mGame;

    public GamePlayer(@NonNull Context context) {
        super(context);
    }

    public GamePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mGame.init(getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mGame.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mGame.update();
        mGame.draw();
    }
}
