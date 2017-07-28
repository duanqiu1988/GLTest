package com.duanqiu.gltest.game;

/**
 * Created by duanjunjie on 17-7-28.
 */

public class Color {
    protected float r;
    protected float g;
    protected float b;
    protected float a;
    private float[] floats = new float[4];

    public Color(float v) {
        this(v, v, v);
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float[] floatValue() {
        floats[0] = r;
        floats[1] = g;
        floats[2] = b;
        floats[3] = a;
        return floats;
    }
}
