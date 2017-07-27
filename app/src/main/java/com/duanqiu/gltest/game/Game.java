package com.duanqiu.gltest.game;

import android.content.Context;
import android.opengl.GLES30;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class Game {
    private State mState;
    private Paddle paddle;
    private SpriteRenderer renderer;
    private Shader shader;
    private int texBackground;
    private int texPaddle;
    private Vector2 positionBackground;
    private Vector2 sizeBackground;
    private Vector3 colorBackground;
    private float paddleH = 0.05f;
    private float paddleW = 0.15f;
    private float paddleV = 0.0005f;
    private List<GameLevel> levels;
    private int level;

    public void init(Context context) {
        texBackground = GLUtil.bindTexture2D(context, R.raw.background);
        texPaddle = GLUtil.bindTexture2D(context, R.raw.paddle);
        paddle = new Paddle(new Vector2(0.0f, 1 - paddleH), new Vector2(paddleW, paddleH),
                texPaddle, new Vector3(1, 1, 1), new Vector2(paddleV, 0));
        shader = Shader.createShader("block", context, R.raw.sprite_vert, R.raw.sprite_frag);
        renderer = new SpriteRenderer(shader);
        positionBackground = new Vector2(0, 0);
        sizeBackground = new Vector2(1, 1);
        colorBackground = new Vector3(1, 1, 1);
        levels = new ArrayList<>();
        levels.add(new GameLevel(context, R.raw.level_1));
        levels.add(new GameLevel(context, R.raw.level_2));
        levels.add(new GameLevel(context, R.raw.level_3));
        levels.add(new GameLevel(context, R.raw.level_4));
        level = 2;
    }

    public void onSurfaceChanged(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    public void movePaddle(float offset, boolean right) {
        if (right) {
            paddle.moveRight(offset);
        } else {
            paddle.moveLeft(offset);
        }
    }

    public void update() {

    }

    public void draw() {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        // draw background
        renderer.drawSprite(texBackground, positionBackground, sizeBackground, 0, colorBackground);
        // draw blocks
        levels.get(level).draw(renderer);
        // draw paddle
        paddle.draw(renderer);
    }

    enum State {

    }
}
