package com.duanqiu.gltest.glsurface;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.duanqiu.gltest.render.TextureRenderer;

/**
 * Created by jjduan on 3/15/17.
 */

public class TextureSurfaceView extends GLSurfaceView {
    public static final String TAG = "TextureSurfaceView";

    private TextureRenderer mRenderer;
    private boolean down;
    private float mix = 0.2f;

    public TextureSurfaceView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                down = false;
                break;
        }

        post(mixRunnable);
        return down;
    }

    private Runnable mixRunnable = new Runnable() {
        @Override
        public void run() {
            if (down) {
                mix += 0.1f;
                if (mix >= 1) {
                    mix = 0f;
                }

                mRenderer.setMix(mix);
                postDelayed(mixRunnable, 300);
            }
        }
    };

    @Override
    public void setRenderer(Renderer renderer) {
        mRenderer = (TextureRenderer) renderer;
        super.setRenderer(renderer);
    }
}
