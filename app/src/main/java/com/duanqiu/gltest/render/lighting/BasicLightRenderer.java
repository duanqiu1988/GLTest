package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;

import javax.microedition.khronos.opengles.GL10;

public class BasicLightRenderer extends BaseLightingRenderer {
    public static final String TAG = "BasicLightRenderer";

    private float[] vertices = {
            // position          // normal
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f
    };

    public BasicLightRenderer(Context context) {
        super(context);
    }


    @Override
    protected int getVertexShader() {
        return R.raw.basic_light_vert;
    }

    @Override
    protected int getFragmentShader() {
        return R.raw.basic_light_frag;
    }

    @Override
    protected float[] getVertices() {
        return vertices;
    }

    @Override
    protected void drawFrame(GL10 gl) {
        // draw VAO
        shader.use();
        GLES30.glUniform3f(shader.getUniformLocation("objectColor"), 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(shader.getUniformLocation("lightColor"), 1.0f, 1.0f, 1.0f);
        GLES30.glUniform3f(shader.getUniformLocation("lightPos"), lightPos.x, lightPos.y, lightPos.z);

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
    protected void createVAO() {
        int[] vaos = new int[2];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(2, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);
        VAO = vaos[0];
        int vbo = vbos[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, getVertices().length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);

        // bind VAO
        GLES30.glBindVertexArray(VAO);
        int positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 6 * 4, 0);
        int normalHandle = shader.getAttribLocation("normal");
        GLES30.glVertexAttribPointer(normalHandle, 3, GLES30.GL_FLOAT, false, 6 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(normalHandle);
        GLES30.glBindVertexArray(0);

        // bind lambVAO
        lambVAO = vaos[1];
        GLES30.glBindVertexArray(lambVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        positionHandle = lambShader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 6 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindVertexArray(0);
    }
}
