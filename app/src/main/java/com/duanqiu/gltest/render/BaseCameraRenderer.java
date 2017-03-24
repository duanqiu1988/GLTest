package com.duanqiu.gltest.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.duanqiu.gltest.glsurface.CameraSurfaceView;
import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.Vector3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseCameraRenderer implements GLSurfaceView.Renderer, CameraSurfaceView.OnGestureListener {
    public static final String TAG = "BaseCameraRenderer";
    protected Context mContext;
    protected float[] mProjMatrix = new float[16];
    protected float[] mVMatrix = new float[16];
    protected Camera mCamera;

    protected long currentFrame = 0;
    protected long lastFrame = 0;
    protected float deltaTime = 0.0f;

    public static final float[] unitMatrix4f = {
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    };

    public BaseCameraRenderer(Context context) {
        mContext = context;
        initCamera();
    }

    protected void initCamera() {
        mCamera = new Camera(new Vector3(0, 0, 3));
    }

    protected abstract void prepareVertexBuffer();

    public float[] getUnitMatrix4f() {
        return unitMatrix4f.clone();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        prepareVertexBuffer();
        createVAO();
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.perspectiveM(mProjMatrix, 0, 45f, ratio, 0.01f, 100);
    }

    protected void clearBackground() {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }

    protected abstract void drawFrame(GL10 gl);

    @Override
    public void onDrawFrame(GL10 gl) {
        clearBackground();
        currentFrame = SystemClock.uptimeMillis();
        deltaTime = (currentFrame - lastFrame) / 1000f;
        lastFrame = currentFrame;

        drawFrame(gl);
    }

    protected abstract void createVAO();

    @Override
    public void onX(boolean left) {
        if (left) {
            mCamera.processKeyboard(Camera.CameraMovement.LEFT, deltaTime);
        } else {
            mCamera.processKeyboard(Camera.CameraMovement.RIGHT, deltaTime);
        }
    }

    @Override
    public void onY(boolean top) {
        if (top) {
            mCamera.processMouseMovement(0, deltaTime * 300, true);
        } else {
            mCamera.processMouseMovement(0, -deltaTime * 300, true);
        }
    }

    @Override
    public void onZ(boolean pinchIn) {
        if (pinchIn) {
            mCamera.processKeyboard(Camera.CameraMovement.BACKWARD, deltaTime);
        } else {
            mCamera.processKeyboard(Camera.CameraMovement.FORWARD, deltaTime);
        }
    }
}
