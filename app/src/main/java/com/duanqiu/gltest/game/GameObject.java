package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class GameObject {

    Vector2 position;
    Vector2 size;
    Vector2 velocity;
    Vector3 color;
    float rotation;
    boolean isSolid;
    boolean destroyed;
    // Render state
    int sprite;

    public GameObject(Vector2 pos, Vector2 size, int sprite, Vector3 color, Vector2 velocity) {
        if (pos == null) {
            this.position = new Vector2(0, 0);
        } else {
            this.position = pos;
        }

        if (size == null) {
            this.size = new Vector2(1, 1);
        } else {
            this.size = size;
        }

        this.sprite = sprite;

        if (color == null) {
            this.color = new Vector3(1, 1, 1);
        } else {
            this.color = color;
        }

        if (velocity == null) {
            this.velocity = new Vector2(0, 0);
        } else {
            this.velocity = velocity;
        }
    }

    // Draw sprite
    public void draw(SpriteRenderer renderer) {
        renderer.drawSprite(sprite, position, size, rotation, color);
    }
}
