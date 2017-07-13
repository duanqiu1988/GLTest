package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class MaterialRenderer extends BaseLightingRenderer {
    public static final String TAG = "MaterialRenderer";

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

    public MaterialRenderer(Context context) {
        super(context);
    }


    @Override
    protected int getVertexShader() {
        return R.raw.material_vert;
    }

    @Override
    protected int getFragmentShader() {
        return R.raw.material_frag;
    }

    @Override
    protected float[] getVertices() {
        return vertices;
    }

    Vector3 lightColor = new Vector3();
    Vector3 lightAmbient;
    Vector3 lightDiffuse;

    @Override
    protected void drawObject(GL10 gl) {
        // draw VAO
        shader.use();
        GLES30.glUniform3f(shader.getUniformLocation("light.position"), lightPos.x, lightPos.y, lightPos.z);
        GLES30.glUniform3f(shader.getUniformLocation("viewPos"), mCamera.position.x, mCamera.position.y, mCamera.position.z);

        double radian = getRadian();
        lightColor.x = (float) Math.sin(radian * 2.0f);
        lightColor.y = (float) Math.sin(radian * 0.7f);
        lightColor.z = (float) Math.sin(radian * 1.3f);

        lightDiffuse = lightColor.scale(0.5f); // Decrease the influence
        lightAmbient = lightDiffuse.scale(0.2f); // Low influence
        GLES30.glUniform3f(shader.getUniformLocation("light.ambient"), lightAmbient.x, lightAmbient.y, lightAmbient.z);
        GLES30.glUniform3f(shader.getUniformLocation("light.diffuse"), lightDiffuse.x, lightDiffuse.y, lightDiffuse.z);
        GLES30.glUniform3f(shader.getUniformLocation("light.specular"), 1.0f, 1.0f, 1.0f);
        // Set material properties
        GLES30.glUniform3f(shader.getUniformLocation("material.ambient"), 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(shader.getUniformLocation("material.diffuse"), 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(shader.getUniformLocation("material.specular"), 0.5f, 0.5f, 0.5f); // Specular doesn't have full effect on this object's material
        GLES30.glUniform1f(shader.getUniformLocation("material.shininess"), 64.0f);

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        float[] mMMatrix = getIdentityM();
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
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
