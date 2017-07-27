package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.LogUtil;
import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-27.
 */

public class Ball extends GameObject {
    public static final String TAG = Ball.class.getSimpleName();

    public Ball(Vector2 pos, Vector2 size, int sprite, Vector3 color, Vector2 velocity) {
        super(pos, size, sprite, color, velocity);
    }

    public void move(float dt) {
        LogUtil.d(TAG, String.format("vx: %.5f, vy: %.5f", velocity.x, velocity.y));
        position.x += dt * velocity.x;
        position.y += dt * velocity.y;
        if (position.x <= 0 || position.x >= 1 - size.x) {
            velocity.x *= -1;
            if (position.x <= 0) {
                position.x = 0;
            } else {
                position.x = 1 - size.x;
            }
        } else if (position.y <= 0 || position.y >= 1 - size.y) {
            velocity.y *= -1;
            if ((position.y <= 0)) {
                position.y = 0;
            } else {
                position.y = 1 - size.y;
            }
        }
    }
}
