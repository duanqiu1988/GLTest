package com.duanqiu.gltest.render.advanced;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DepthTestRenderer extends BaseCameraRenderer {
    public static final String TAG = "DepthTestRenderer";
    protected FloatBuffer cubeBuffer;
    protected FloatBuffer planeBuffer;
    protected Shader shader;
    protected int cubeVAO;
    protected int planeVAO;
    private int cubeTexture;
    private int planeTexture;

    private float[] cubeVertices = {
            // Positions       // Texture Coords
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    };

    private float[] planeVertices = {
            5.0f, -0.5f, 5.0f, 2.0f, 0.0f,
            -5.0f, -0.5f, 5.0f, 0.0f, 0.0f,
            -5.0f, -0.5f, -5.0f, 0.0f, 2.0f,

            5.0f, -0.5f, 5.0f, 2.0f, 0.0f,
            -5.0f, -0.5f, -5.0f, 0.0f, 2.0f,
            5.0f, -0.5f, -5.0f, 2.0f, 2.0f
    };

    public DepthTestRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initCamera() {
        mCamera = new Camera(new Vector3(0, 1, 3));
    }

    @Override
    protected void prepareVertexBuffer() {
        cubeBuffer = ByteBuffer.allocateDirect(cubeVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeBuffer.put(cubeVertices).position(0);

        planeBuffer = ByteBuffer.allocateDirect(planeVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        planeBuffer.put(planeVertices).position(0);
    }

    @Override
    protected void drawFrame(GL10 gl) {
        shader.use();
        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        // draw cube
        GLES30.glBindVertexArray(cubeVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, cubeTexture);
        // first cube
        float[] mMMatrix = getIdentityM();
        Matrix.translateM(mMMatrix, 0, -1.0f, 0.0f, -1.0f);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        // second cube
        mMMatrix = getIdentityM();
        Matrix.translateM(mMMatrix, 0, 2.0f, 0.0f, 0.0f);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        // draw plane
        GLES30.glBindVertexArray(planeVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, planeTexture);
        mMMatrix = getIdentityM();
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
        GLES30.glBindVertexArray(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(getClass().getSimpleName(), mContext, getVertexShader(), getFragmentShader());
        super.onSurfaceCreated(gl, config);
        GLES30.glDepthFunc(GLES30.GL_LESS);
    }

    protected int getVertexShader() {
        return R.raw.depth_test_vert;
    }

    protected int getFragmentShader() {
        return R.raw.depth_test_frag;
    }

    @Override
    protected void createVAO() {
        int[] vaos = new int[2];
        int[] vbos = new int[2];

        GLES30.glGenVertexArrays(2, vaos, 0);
        GLES30.glGenBuffers(2, vbos, 0);

        // bind cubeVAO
        cubeVAO = vaos[0];
        int vbo = vbos[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, cubeVertices.length * 4, cubeBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindVertexArray(cubeVAO);
        int positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        int texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        GLES30.glBindVertexArray(0);

        // bind planeVAO
        planeVAO = vaos[1];
        vbo = vbos[1];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, planeVertices.length * 4, planeBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindVertexArray(planeVAO);
        positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        GLES30.glBindVertexArray(0);

        cubeTexture = GLUtil.bindTexture2D(mContext, R.raw.cube);
        planeTexture = GLUtil.bindTexture2D(mContext, R.raw.metal);
    }
}
