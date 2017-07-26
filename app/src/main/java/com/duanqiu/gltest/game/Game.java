package com.duanqiu.gltest.game;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

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
    private SpriteRenderer renderer;
    private Shader shader;
    private float[] projMatrix = new float[16];

    public void init(Context context) {

        int texture = GLUtil.bindTexture2D(context, R.raw.awesomeface);
        block = new GameObject(new Vector2(0, 0), new Vector2(0.5f, 0.5f), texture, new Vector3(1, 1, 1), new Vector2(0, 0));
        shader = Shader.createShader("block", context, R.raw.sprite_vert, R.raw.sprite_frag);
        renderer = new SpriteRenderer(shader);
    }

    public void onSurfaceChanged(int width, int height) {
//        GLES30.glViewport(0, 0, width, height);
//        Matrix.setIdentityM(projMatrix, 0);
//        Matrix.orthoM(projMatrix, 0, 0, width, height, 0, -1, 1);


        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.setIdentityM(projMatrix, 0);
        Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        shader.use().setMat4("projection", projMatrix);
    }

    public void update() {

    }

    public void draw() {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        block.draw(renderer);
    }

    enum State {

    }
}
