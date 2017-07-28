package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.Vector2;

/**
 * Created by duanjunjie on 17-7-28.
 */

public class Particle {
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 size;
    protected Color color;
    protected float life;

    public Particle(Vector2 position, Vector2 size, Vector2 velocity, Color color, float life) {
        this.position = position;
        this.size = size;
        this.velocity = velocity;
        this.color = color;
        this.life = life;
    }
}
