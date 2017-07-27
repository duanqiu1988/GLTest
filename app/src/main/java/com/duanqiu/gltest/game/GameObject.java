package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class GameObject {
    protected Vector2 position;
    protected Vector2 size;
    protected Vector2 velocity;
    protected Vector3 color;
    protected float rotation;
    // Render state
    protected int sprite;

    public GameObject(Vector2 pos, Vector2 size, int sprite, Vector3 color, Vector2 velocity) {
        if (pos == null) {
            this.position = new Vector2(0);
        } else {
            this.position = pos;
        }

        if (size == null) {
            this.size = new Vector2(1);
        } else {
            this.size = size;
        }

        this.sprite = sprite;

        if (color == null) {
            this.color = new Vector3(1);
        } else {
            this.color = color;
        }

        if (velocity == null) {
            this.velocity = new Vector2(0);
        } else {
            this.velocity = velocity;
        }
    }

    // Draw sprite
    public void draw(SpriteRenderer renderer) {
        renderer.drawSprite(sprite, position, size, rotation, color);
    }
}
