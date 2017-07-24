package com.duanqiu.gltest.render.advanced;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.GL_UNIFORM_BUFFER;

/**
 * Created by duanjunjie on 17-7-21.
 */

public class AdvancedGLSLUniformBlockRenderer extends BaseCameraRenderer {
    private static final String TAG = AdvancedGLSLUniformBlockRenderer.class.getSimpleName();
    private FloatBuffer cubeBuffer;
    private int uboMatrix;
    private int cubeVAO;
    private Shader shaderRed;
    private Shader shaderGreen;
    private Shader shaderBlue;
    private Shader shaderYellow;

    float[] cubeVertices = {
            // positions
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,

            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,

            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,

            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,

            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
    };

    public AdvancedGLSLUniformBlockRenderer(Context context) {
        super(context);
    }

    @Override
    protected void prepareVertexBuffer() {
        cubeBuffer = ByteBuffer.allocateDirect(cubeVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeBuffer.put(cubeVertices).position(0);

        shaderRed = Shader.createShader(TAG, mContext, R.raw.advanced_glsl_uniform_block_vert,
                R.raw.advanced_glsl_uniform_block_red_frag);

        shaderGreen = Shader.createShader(TAG, mContext, R.raw.advanced_glsl_uniform_block_vert,
                R.raw.advanced_glsl_uniform_block_green_frag);

        shaderBlue = Shader.createShader(TAG, mContext, R.raw.advanced_glsl_uniform_block_vert,
                R.raw.advanced_glsl_uniform_block_blue_frag);

        shaderYellow = Shader.createShader(TAG, mContext, R.raw.advanced_glsl_uniform_block_vert,
                R.raw.advanced_glsl_uniform_block_yellow_frag);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        // store the projection matrix (we only do this once now) (note: we're not using zoom anymore by changing the FoV)
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, uboMatrix);
        GLES30.glBufferSubData(GL_UNIFORM_BUFFER, 0, MATRIX4_SIZE, FloatBuffer.wrap(mProjMatrix));
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    protected void drawFrame(GL10 gl) {
        // set the view and projection matrix in the uniform block - we only have to do this once per loop iteration.
        mCamera.setLookAtM(mVMatrix);
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, uboMatrix);
        GLES30.glBufferSubData(GL_UNIFORM_BUFFER, MATRIX4_SIZE, MATRIX4_SIZE, FloatBuffer.wrap(mVMatrix));
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, 0);

        // draw 4 cubes
        // RED
        GLES30.glBindVertexArray(cubeVAO);
        shaderRed.use();
        getIdentityM();
        Matrix.translateM(mModelMatrix, 0, -0.75f, 0.75f, 0.0f);// move top-left
        shaderRed.setMat4("model", mModelMatrix);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        // GREEN
        shaderGreen.use();
        getIdentityM();
        Matrix.translateM(mModelMatrix, 0, 0.75f, 0.75f, 0.0f); // move top-right
        shaderGreen.setMat4("model", mModelMatrix);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        // YELLOW
        shaderYellow.use();
        getIdentityM();
        Matrix.translateM(mModelMatrix, 0, -0.75f, -0.75f, 0.0f); // move bottom-left
        shaderYellow.setMat4("model", mModelMatrix);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        // BLUE
        shaderBlue.use();
        getIdentityM();
        Matrix.translateM(mModelMatrix, 0, 0.75f, -0.75f, 0.0f); // move bottom-right
        shaderBlue.setMat4("model", mModelMatrix);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
    }

    @Override
    protected void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];
        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);

        cubeVAO = vaos[0];
        int vbo = vbos[0];
        GLES30.glBindVertexArray(cubeVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, cubeVertices.length * 4, cubeBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);


        // configure a uniform buffer object
        // ---------------------------------
        // first. We get the relevant block indices
        int uniformBlockIndexRed = GLES30.glGetUniformBlockIndex(shaderRed.getProgram(), "Matrices");
        int uniformBlockIndexGreen = GLES30.glGetUniformBlockIndex(shaderGreen.getProgram(), "Matrices");
        int uniformBlockIndexBlue = GLES30.glGetUniformBlockIndex(shaderBlue.getProgram(), "Matrices");
        int uniformBlockIndexYellow = GLES30.glGetUniformBlockIndex(shaderYellow.getProgram(), "Matrices");
        // then we link each shader's uniform block to this uniform binding point
        GLES30.glUniformBlockBinding(shaderRed.getProgram(), uniformBlockIndexRed, 0);
        GLES30.glUniformBlockBinding(shaderGreen.getProgram(), uniformBlockIndexGreen, 0);
        GLES30.glUniformBlockBinding(shaderBlue.getProgram(), uniformBlockIndexBlue, 0);
        GLES30.glUniformBlockBinding(shaderYellow.getProgram(), uniformBlockIndexYellow, 0);
        // Now actually create the buffer
        int[] uboMatrices = new int[1];
        GLES30.glGenBuffers(1, uboMatrices, 0);
        uboMatrix = uboMatrices[0];
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, uboMatrix);
        GLES30.glBufferData(GL_UNIFORM_BUFFER, 2 * MATRIX4_SIZE, null, GLES30.GL_STATIC_DRAW);
        GLES30.glBindBuffer(GL_UNIFORM_BUFFER, 0);
        // define the range of the buffer that links to a uniform binding point
        GLES30.glBindBufferRange(GL_UNIFORM_BUFFER, 0, uboMatrix, 0, 2 * MATRIX4_SIZE);
    }
}
