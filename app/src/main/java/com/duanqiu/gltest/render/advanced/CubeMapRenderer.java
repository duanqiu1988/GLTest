package com.duanqiu.gltest.render.advanced;

import android.content.Context;
import android.opengl.GLES30;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by duanjunjie on 17-7-13.
 */

public class CubeMapRenderer extends BaseCameraRenderer {
    private static final String TAG = CubeMapRenderer.class.getSimpleName();
    private FloatBuffer cubeBuffer;
    private FloatBuffer skyBoxBuffer;
    private Shader cubeShader;
    private Shader skyBoxShader;
    private int cubeVAO;
    private int skyBoxVAO;
    //    private int cubeTexture;
    private int skyBoxTexture;

    float[] cubeVertices = {
            // positions          // normals
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
    float[] skyBoxVertices = {
            // positions
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    };


    public CubeMapRenderer(Context context) {
        super(context);
    }

    @Override
    protected void prepareVertexBuffer() {
        cubeBuffer = ByteBuffer.allocateDirect(cubeVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubeBuffer.put(cubeVertices).position(0);

        skyBoxBuffer = ByteBuffer.allocateDirect(skyBoxVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        skyBoxBuffer.put(skyBoxVertices).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        cubeShader = Shader.createShader(getClass().getSimpleName(), mContext, R.raw.cube_map_vert, R.raw.cube_map_frag);
        skyBoxShader = Shader.createShader(getClass().getSimpleName(), mContext, R.raw.sky_box_vert, R.raw.sky_box_frag);
        super.onSurfaceCreated(gl, config);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    protected void clearBackground() {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    protected void drawFrame(GL10 gl) {
        mCamera.setLookAtM(mVMatrix);

        cubeShader.use();
        // draw cube
        GLES30.glBindVertexArray(cubeVAO);
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, cubeTexture);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, skyBoxTexture);
        getIdentityM();
        GLES30.glUniformMatrix4fv(cubeShader.getUniformLocation("model"), 1, false, mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(cubeShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(cubeShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        GLES30.glUniform3f(cubeShader.getUniformLocation("cameraPos"), mCamera.position.x, mCamera.position.y, mCamera.position.z);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        // draw skyBox
        GLES30.glDepthMask(false);
        skyBoxShader.use();
        GLES30.glBindVertexArray(skyBoxVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, skyBoxTexture);
        GLES30.glUniformMatrix4fv(skyBoxShader.getUniformLocation("view"), 1, false, GLUtil.mat4(GLUtil.mat3(mVMatrix)), 0);
        GLES30.glUniformMatrix4fv(skyBoxShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        GLES30.glBindVertexArray(0);
        GLES30.glDepthMask(true);
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

        GLES30.glBindVertexArray(cubeVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, cubeVertices.length * 4, cubeBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 6 * 4, 0);
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 6 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glBindVertexArray(0);

        // bind planeVAO
        skyBoxVAO = vaos[1];
        vbo = vbos[1];
        GLES30.glBindVertexArray(skyBoxVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, skyBoxVertices.length * 4, skyBoxBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glBindVertexArray(0);

//        cubeTexture = GLUtil.bindTexture2D(mContext, R.raw.cube);
        skyBoxTexture = GLUtil.bindCubeTexture(mContext,
                Arrays.asList(R.raw.right, R.raw.left,
                        R.raw.top, R.raw.bottom,
                        R.raw.back, R.raw.front));
    }
}
