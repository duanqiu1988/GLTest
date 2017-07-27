package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-27.
 */

public class Paddle extends GameObject {
    private float maxX;

    public Paddle(Vector2 pos, Vector2 size, int sprite, Vector3 color, Vector2 velocity) {
        super(pos, size, sprite, color, velocity);
        maxX = 1 - size.x;
    }

    public void moveRight(float offSet) {
        position.x += offSet * velocity.x;
        if (position.x >= maxX) {
            position.x = maxX;
        }
    }

    public void moveLeft(float offSet) {
        position.x -= offSet * velocity.x;
        if (position.x <= 0) {
            position.x = 0;
        }
    }

    public void checkCollision(Ball ball) {
        if (GameUtil.collide(this, ball)) {
            ball.velocity.y *= -1;
        }
    }
}
