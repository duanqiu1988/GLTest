package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.support.annotation.RawRes;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
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

    protected float[][] cubePositions =

            {
                    {
                            0.0f, 0.0f, 0.0f
                    },
                    {
                            2.0f, 5.0f, -15.0f
                    },
                    {
                            -1.5f, -2.2f, -2.5f
                    },
                    {
                            -3.8f, -2.0f, -12.3f
                    },
                    {
                            2.4f, -0.4f, -3.5f
                    },
                    {
                            -1.7f, 3.0f, -7.5f
                    },
                    {
                            1.3f, -2.0f, -2.5f
                    },
                    {
                            1.5f, 2.0f, -2.5f
                    },
                    {
                            1.5f, 0.2f, -1.5f
                    },
                    {
                            -1.3f, 1.0f, -1.5f
                    }
            };

    public BaseLightingRenderer(Context context) {
        super(context);
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
        drawObject(gl);
        drawLamb(gl);
    }

    protected abstract void drawObject(GL10 gl);

    protected void drawLamb(GL10 gl) {
        // draw lambVAO
        lambShader.use();
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        float[] mMMatrix = getUnitMatrix4f();
        Matrix.translateM(mMMatrix, 0, lightPos.x, lightPos.y, lightPos.z);
        Matrix.scaleM(mMMatrix, 0, 0.2f, 0.2f, 0.2f);
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("model"), 1, false, mMMatrix, 0);

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
