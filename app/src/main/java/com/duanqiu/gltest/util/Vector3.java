package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/17/17.
 */

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static boolean equals(Vector3 vectorA, Vector3 vectorB) {
        return (vectorA.x == vectorB.x) && (vectorA.y == vectorB.y) && (vectorA.z == vectorB.z);
    }

    public static Vector3 addition(Vector3 vectorA, Vector3 vectorB) {
        return new Vector3(vectorA.x + vectorB.x, vectorA.y + vectorB.y, vectorA.z + vectorB.z);
    }

    public static Vector3 subtraction(Vector3 vectorA, Vector3 vectorB) {
        return new Vector3(vectorA.x - vectorB.x, vectorA.y - vectorB.y, vectorA.z - vectorB.z);
    }

    public static float dotProduct(Vector3 vectorA, Vector3 vectorB) {
        return vectorA.x * vectorB.x + vectorA.y * vectorB.y + vectorA.z * vectorB.z;
    }

    public static Vector3 scale(Vector3 vector, float scale) {
        return new Vector3(vector.x * scale, vector.y * scale, vector.z * scale);
    }

    public static Vector3 crossProduct(Vector3 vectorA, Vector3 vectorB) {
        return new Vector3(vectorA.y * vectorB.z - vectorA.z * vectorB.y,
                vectorA.z * vectorB.x - vectorA.x * vectorB.z,
                vectorA.x * vectorB.y - vectorA.y * vectorB.x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vector3)) {
            return false;
        }

        return equals(this, (Vector3) obj);
    }

    public Vector3 addition(Vector3 vectorB) {
        x += vectorB.x;
        y += vectorB.y;
        z += vectorB.z;
        return this;
    }

    public Vector3 subtraction(Vector3 vectorB) {
        x -= vectorB.x;
        y -= vectorB.y;
        z -= vectorB.z;
        return this;
    }


    public Vector3 scale(float scale) {
        x *= scale;
        y *= scale;
        z *= scale;
        return this;
    }

    public Vector3 crossProduct(Vector3 vectorB) {
        float x = this.y * vectorB.z - this.z * vectorB.y;
        float y = this.z * vectorB.x - this.x * vectorB.z;
        float z = this.x * vectorB.y - this.y * vectorB.x;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public static float magnitude(Vector3 vector) {
        return (float) Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2) + Math.pow(vector.z, 2));
    }

    public static Vector3 normalize(Vector3 vector) {
        float mag = magnitude(vector);
        if (mag != 0) {
            vector.x /= mag;
            vector.y /= mag;
            vector.z /= mag;
        }

        return vector;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
