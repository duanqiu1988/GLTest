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
    private float paddleVelocityX = 0.0005f;
    private List<GameLevel> levels;
    private int level;
    // ball
    private Ball ball;
    private int texBall;
    private float ballRadius = 0.03f;
    private float ballVelocityX = 0.007f;
    private float ballVelocityY = 0.017f;

    public void init(Context context) {
        texBackground = GLUtil.bindTexture2D(context, R.raw.background);
        texPaddle = GLUtil.bindTexture2D(context, R.raw.paddle);
        texBall = GLUtil.bindTexture2D(context, R.raw.awesomeface);
        paddle = new Paddle(new Vector2(0.0f, 1 - paddleH), new Vector2(paddleW, paddleH),
                texPaddle, new Vector3(1, 1, 1), new Vector2(paddleVelocityX, 0));
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
        level = 0;
        mState = State.PAUSE;
    }

    public void onSurfaceChanged(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        float r = (float) width / height;
        ball = new Ball(new Vector2(0.5f, 1 - paddleH - 2 * ballRadius), new Vector2(ballRadius * 2 / r, ballRadius * 2),
                texBall, new Vector3(1, 1, 1), new Vector2(ballVelocityX, ballVelocityY));
    }

    public void movePaddle(float offset, boolean right) {
        if (right) {
            paddle.moveRight(offset);
        } else {
            paddle.moveLeft(offset);
        }
    }

    public void update(float dt) {
        if (mState == State.ACTIVE) {
            ball.move(dt);
        }
    }

    public void onTap() {
        if (mState == State.PAUSE) {
            mState = State.ACTIVE;
        } else if (mState == State.ACTIVE) {
            mState = State.PAUSE;
        }
    }

    public void draw() {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        // draw background
        renderer.drawSprite(texBackground, positionBackground, sizeBackground, 0, colorBackground);
        // draw blocks
        levels.get(level).draw(renderer);
        // draw ball
        ball.draw(renderer);
        // draw paddle
        paddle.draw(renderer);
    }

    enum State {
        PAUSE,
        ACTIVE,
    }
}
