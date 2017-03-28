package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.GLUtil;

import javax.microedition.khronos.opengles.GL10;

public class SpotLightRenderer extends BaseLightingRenderer {
    private int woodTexture;
    private int steelTexture;

    private float[] vertices = {
            // Positions          // Normals           // Texture Coords
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    public SpotLightRenderer(Context context) {
        super(context);
    }

    @Override
    protected int getVertexShader() {
        return R.raw.spot_light_vert;
    }

    @Override
    protected int getFragmentShader() {
        return R.raw.spot_light_frag;
    }

    @Override
    protected float[] getVertices() {
        return vertices;
    }

    @Override
    protected void drawObject(GL10 gl) {
        // draw VAO
        shader.use();
        GLES30.glUniform3f(shader.getUniformLocation("light.position"), lightPos.x, lightPos.y, lightPos.z);
        GLES30.glUniform3f(shader.getUniformLocation("light.direction"), mCamera.front.x,  mCamera.front.y,  mCamera.front.z);
        GLES30.glUniform1f(shader.getUniformLocation("light.cutOff"), (float) Math.cos(Math.toRadians(12.5f)));
        GLES30.glUniform3f(shader.getUniformLocation("viewPos"), mCamera.postion.x, mCamera.postion.y, mCamera.postion.z);

        GLES30.glUniform3f(shader.getUniformLocation("light.ambient"), 0.2f, 0.2f, 0.2f);
        GLES30.glUniform3f(shader.getUniformLocation("light.diffuse"), 0.5f, 0.5f, 0.5f);
        GLES30.glUniform3f(shader.getUniformLocation("light.specular"), 1.0f, 1.0f, 1.0f);
        GLES30.glUniform1f(shader.getUniformLocation("light.constant"), 1.0f);
        GLES30.glUniform1f(shader.getUniformLocation("light.linear"), 0.09f);
        GLES30.glUniform1f(shader.getUniformLocation("light.quadratic"), 0.032f);

        GLES30.glUniform1f(shader.getUniformLocation("material.shininess"), 64.0f);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, woodTexture);
        GLES30.glUniform1i(shader.getUniformLocation("material.diffuse"), 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, steelTexture);
        GLES30.glUniform1i(shader.getUniformLocation("material.specular"), 1);

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        for (int i = 0; i < 10; i++) {
            float[] mMMatrix = getUnitMatrix4f();

            float angle = i * 20;
            Matrix.setRotateM(mMMatrix, 0, angle, 1.0f, 0.3f, 0.5f);
            Matrix.translateM(mMMatrix, 0, cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);

            GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }
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
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 0);
        int normalHandle = shader.getAttribLocation("normal");
        GLES30.glVertexAttribPointer(normalHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4);
        int texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(normalHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        GLES30.glBindVertexArray(0);

        // bind lambVAO
        lambVAO = vaos[1];
        GLES30.glBindVertexArray(lambVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        positionHandle = lambShader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindVertexArray(0);

        woodTexture = GLUtil.bindTexture2D(mContext, R.raw.container2);
        steelTexture = GLUtil.bindTexture2D(mContext, R.raw.container2_specular);
    }
}
