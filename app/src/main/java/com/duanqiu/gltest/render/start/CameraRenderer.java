package com.duanqiu.gltest.render.start;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRenderer extends BaseCameraRenderer {
    public static final String TAG = "CameraRenderer";
    protected Shader shader;
    private int VAO;
    private int texture;
    private int texture2;
    private FloatBuffer vertexBuffer;
    private float mix = 0.2f;


    float[] vertices = {
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

    float[][] cubePositions =

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

    public CameraRenderer(Context context) {
        super(context);
    }

    @Override
    protected void prepareVertexBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(getClass().getSimpleName(), mContext, getVertexShader(), getFragmentShader());
        super.onSurfaceCreated(gl, config);
    }

    protected int getVertexShader() {
        return R.raw.coordinate_vert;
    }

    protected int getFragmentShader() {
        return R.raw.coordinate_frag;
    }

    @Override
    protected void drawFrame(GL10 gl) {
        shader.use();

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
        GLES30.glUniform1i(shader.getUniformLocation("outTexture"), 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture2);
        GLES30.glUniform1i(shader.getUniformLocation("outTexture2"), 1);

        GLES30.glUniform1f(shader.getUniformLocation("mix"), mix);

        long time = SystemClock.uptimeMillis() % 4000L;

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        GLES30.glBindVertexArray(VAO);

        for (int i = 0; i < 10; i++) {
            float[] mMMatrix = new float[16];

            float angle = i * 20;
            if (i >= 5 && i < 9) {
                angle = 0.090f * ((int) time);
            }
            Matrix.setRotateM(mMMatrix, 0, angle, cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);
            Matrix.translateM(mMMatrix, 0, cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);

            GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }

        GLES30.glBindVertexArray(0);
    }

    @Override
    protected void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);

        VAO = vaos[0];
        GLES30.glBindVertexArray(VAO);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vaos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);


        int positionHandle = shader.getAttribLocation("position");
        int coordHandle = shader.getAttribLocation("texCoord");

        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glVertexAttribPointer(coordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);
        GLES30.glEnableVertexAttribArray(coordHandle);


        // texture
        texture = GLUtil.bindTexture2D(mContext, R.raw.container);
        texture2 = GLUtil.bindTexture2D(mContext, R.raw.awesomeface);
    }

    public void setMix(float mix) {
        this.mix = mix;
    }
}
