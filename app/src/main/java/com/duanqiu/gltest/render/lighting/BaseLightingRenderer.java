package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.support.annotation.RawRes;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 俊杰 on 2017/3/14.
 */

public abstract class BaseLightingRenderer extends BaseCameraRenderer {
    public static final String TAG = "BaseLightingRenderer";
    protected Shader shader;
    protected Shader lambShader;
    protected int VAO;
    protected int lambVAO;
    protected FloatBuffer vertexBuffer;
    protected Vector3 lightPos = new Vector3(1.2f, 1.0f, 2.0f);

    public BaseLightingRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initCamera() {
        mCamera = new Camera(new Vector3(1f, 1.5f, 3));
        mCamera.processKeyboard(Camera.CameraMovement.BACKWARD, 0.5f);
        mCamera.processMouseMovement(-30, 0f, true);
        mCamera.processMouseMovement(0.0f, -45, true);
    }

    @Override
    protected void prepareVertexBuffer() {
        float[] vertices = getVertices();
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(getClass().getSimpleName(), mContext, getVertexShader(), getFragmentShader());
        lambShader = Shader.createShader(getClass().getSimpleName(), mContext, R.raw.lighting_vert, R.raw.lamb_frag);
        super.onSurfaceCreated(gl, config);
    }

    @Override
    protected void drawFrame(GL10 gl) {
        // draw VAO
        shader.use();
        GLES30.glUniform3f(shader.getUniformLocation("objectColor"), 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(shader.getUniformLocation("lightColor"), 1.0f, 1.0f, 1.0f);

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        float[] mMMatrix = getUnitMatrix4f();
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        // draw lambVAO
        lambShader.use();
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        mMMatrix = getUnitMatrix4f();
        Matrix.translateM(mMMatrix, 0, lightPos.x, lightPos.y, lightPos.z);
        Matrix.scaleM(mMMatrix, 0, 0.2f, 0.2f, 0.2f);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);

        GLES30.glBindVertexArray(lambVAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        GLES30.glBindVertexArray(0);
    }

    @Override
    protected void clearBackground() {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }

    protected abstract
    @RawRes
    int getVertexShader();

    protected abstract
    @RawRes
    int getFragmentShader();

    protected abstract float[] getVertices();
}
