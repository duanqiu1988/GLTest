package com.duanqiu.gltest.util;

import android.opengl.Matrix;

/**
 * Created by jjduan on 3/23/17.
 */

public class Camera {
    public static final String TAG = "Camera";

    public static final float YAW = -90.0f;
    public static final float PITCH = 0f;
    public static final float SPEED = 3.0f;
    public static final float SENSITIVTY = 0.25f;
    public static final float ZOOM = 45.0f;

    // Camera Attribute
    public Vector3 postion;
    public Vector3 front;
    public Vector3 up;
    public Vector3 right;
    public Vector3 worldUp;
    // Eular Angles
    public float yaw = YAW;
    public float pitch = PITCH;
    // Camera options
    public float movementSpeed = SPEED;
    public float movementSensitivty = SENSITIVTY;
    public float zoom = ZOOM;

    public Camera() {
        postion = new Vector3(0f, 0f, 0f);
        worldUp = new Vector3(0f, 1f, 0f);
        updateCameraVectors();
    }

    public Camera(Vector3 postion, Vector3 worldUp) {
        this.postion = postion;
        this.worldUp = worldUp;
        updateCameraVectors();
    }

    public Camera(Vector3 postion, Vector3 worldUp, float yaw, float pitch) {
        this.postion = postion;
        this.worldUp = worldUp;
        this.yaw = yaw;
        this.pitch = pitch;
        updateCameraVectors();
    }

    public void setLookAtM(float[] viewMatrix) {
        Vector3 center = Vector3.addition(postion, front);
        Matrix.setLookAtM(viewMatrix, 0,
                postion.x, postion.y, postion.z  // camera position
                , center.x, center.y, center.z,  // camera target
                up.x, up.y, up.z);  // camera up
    }

    public void processKeyboard(CameraMovement direction, float deltaTime) {
        float velocity = deltaTime * movementSpeed;
        if (direction == CameraMovement.FORWARD) {
            postion.addition(Vector3.scale(front, velocity));
        }

        if (direction == CameraMovement.BACKWARD) {
            postion.subtraction(Vector3.scale(front, velocity));
        }

        if (direction == CameraMovement.LEFT) {
            postion.subtraction(Vector3.scale(right, velocity));
        }

        if (direction == CameraMovement.RIGHT) {
            postion.addition(Vector3.scale(right, velocity));
        }
    }

    public void processMouseMovement(float xOff, float yOff, boolean constrainPitch) {
        xOff *= movementSensitivty;
        yOff *= movementSensitivty;
        yaw += xOff;
        pitch += yOff;
        if (constrainPitch) {
            if (pitch >= 89.0f) {
                pitch = 89.0f;
            }

            if (pitch <= -89.0f) {
                pitch = -89.0f;
            }
        }
        updateCameraVectors();
    }

    public void processMouseScroll(float yOff) {
        if (zoom >= 1.0f && zoom <= 45.0f) {
            zoom -= yOff;
        }

        if (zoom <= 1.0f) {
            zoom = 1.0f;
        }

        if (zoom > 45.0f) {
            zoom = 45.0f;
        }
    }

    private void updateCameraVectors() {
        Vector3 front = new Vector3();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        this.front = Vector3.normalize(front);
        this.right = Vector3.normalize(Vector3.crossProduct(this.front, this.worldUp));
        this.up = Vector3.normalize(Vector3.crossProduct(this.right, this.front));
        LogUtil.d(TAG, this.toString());
    }

    @Override
    public String toString() {
        return "postion" + postion + ", front" + front + ", up" + up + ", yaw(" + yaw + "), pitch(" + pitch + ")";
    }

    public enum CameraMovement {
        FORWARD, BACKWARD, RIGHT, LEFT
    }
}
