package com.duanqiu.gltest.util;

import java.util.Comparator;

/**
 * Created by jjduan on 3/17/17.
 */

public class Vector2 implements Comparator<Vector2> {
    public float x;
    public float y;
    private float[] floats = new float[2];

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(float v) {
        this.x = v;
        this.y = v;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static boolean equals(Vector2 vectorA, Vector2 vectorB) {
        return (vectorA.x == vectorB.x) && (vectorA.y == vectorB.y);
    }

    public static Vector2 addition(Vector2 vectorA, Vector2 vectorB) {
        return new Vector2(vectorA.x + vectorB.x, vectorA.y + vectorB.y);
    }

    public static Vector2 subtraction(Vector2 vectorA, Vector2 vectorB) {
        return new Vector2(vectorA.x - vectorB.x, vectorA.y - vectorB.y);
    }

    public static float dotProduct(Vector2 vectorA, Vector2 vectorB) {
        return vectorA.x * vectorB.x + vectorA.y * vectorB.y;
    }

    public static Vector2 scale(Vector2 vector, float scale) {
        return new Vector2(vector.x * scale, vector.y * scale);
    }

    @Override
    public int compare(Vector2 o1, Vector2 o2) {
        float result = magnitude(o1) - magnitude(o2);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vector2)) {
            return false;
        }

        return equals(this, (Vector2) obj);
    }

    public Vector2 addition(Vector2 vectorB) {
        x += vectorB.x;
        y += vectorB.y;
        return this;
    }

    public Vector2 addition(float offset) {
        x += offset;
        y += offset;
        return this;
    }

    public Vector2 subtraction(Vector2 vectorB) {
        x -= vectorB.x;
        y -= vectorB.y;
        return this;
    }


    public Vector2 scale(float scale) {
        x *= scale;
        y *= scale;
        return this;
    }

    public Vector2 wrap(Vector2 a) {
        x = a.x;
        y = a.y;
        return this;
    }


    public static float magnitude(Vector2 vector) {
        return (float) Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
    }

    public static Vector2 normalize(Vector2 vector) {
        float mag = magnitude(vector);
        if (mag != 0) {
            vector.x /= mag;
            vector.y /= mag;

        }

        return vector;
    }

    public float[] floatValue() {
        floats[0] = x;
        floats[1] = y;
        return floats;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static Vector2 newInstance(Vector2 vector3) {
        return new Vector2(vector3.x, vector3.y);
    }
}
