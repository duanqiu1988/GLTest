package com.duanqiu.gltest.game;

import android.content.Context;
import android.opengl.GLES30;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class Game {
    private State mState;
    private GameObject block;
    private GameObject paddle;
    private SpriteRenderer renderer;
    private Shader shader;

    public void init(Context context) {

        int texture = GLUtil.bindTexture2D(context, R.raw.awesomeface);
        int paddleTex = GLUtil.bindTexture2D(context, R.raw.paddle);
        block = new GameObject(new Vector2(0.25f, 0.1f), new Vector2(0.25f, 0.1f), texture, new Vector3(0.2f, 0.6f, 1), new Vector2(0, 0));
        paddle = new GameObject(new Vector2(0.25f, 0.75f), new Vector2(0.25f, 0.05f), paddleTex, new Vector3(1, 1, 1), new Vector2(0, 0));
        shader = Shader.createShader("block", context, R.raw.sprite_vert, R.raw.sprite_frag);
        renderer = new SpriteRenderer(shader);
    }

    public void onSurfaceChanged(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    public void update() {

    }

    public void draw() {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        block.draw(renderer);
        paddle.draw(renderer);
    }

    enum State {

    }
}
