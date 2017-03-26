package com.duanqiu.gltest.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.Vector3;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseCameraRenderer implements GLSurfaceView.Renderer, Camera.CameraProcessListener {
    public static final String TAG = "BaseCameraRenderer";
    protected Context mContext;
    protected float[] mProjMatrix = new float[16];
    protected float[] mVMatrix = new float[16];
    protected Camera mCamera;
    protected int mScreenWidth;
    protected int mScreenHeight;

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
        mScreenWidth = width;
        mScreenHeight = height;
        perspectiveM();
    }

    protected void perspectiveM() {
        float ratio = (float) mScreenWidth / mScreenHeight;
        Matrix.perspectiveM(mProjMatrix, 0, mCamera.zoom, ratio, 0.01f, 100);
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
        perspectiveM();
        drawFrame(gl);
    }

    protected abstract void createVAO();

    protected int getTime(){
        return (int) (SystemClock.uptimeMillis() % 4000L * 0.09f);
    }

    @Override
    public void processKeyboard(Camera.CameraMovement direction) {
        mCamera.processKeyboard(direction, deltaTime);
    }

    @Override
    public void processMouseMovement(float right, float up) {
        mCamera.processMouseMovement(right, up, true);
    }

    @Override
    public void processMouseScroll(float up) {
        mCamera.processMouseScroll(up / 100);
    }
}
