package com.duanqiu.gltest.render.advanced;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.util.Camera;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.LogUtil;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES30.glBindVertexArray;

public abstract class FBORenderer extends BaseCameraRenderer {
    public static final String TAG = "FBORenderer";
    protected FloatBuffer cubeBuffer;
    protected FloatBuffer planeBuffer;
    private FloatBuffer screenVertexBuffer;
    protected Shader shader;
    private Shader screenShader;
    protected int FBO;
    protected int cubeVAO;
    protected int planeVAO;
    private int screenVAO;
    private int cubeTexture;
    private int planeTexture;
    private int textureColorBuffer;

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

    float[] screenVertexes = {   // Vertex attributes for a quad that fills the entire screen in Normalized Device Coordinates.
            // Positions   // TexCoords
            -1.0f, 1.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f,
            1.0f, -1.0f, 1.0f, 0.0f,

            -1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f
    };

    public FBORenderer(Context context) {
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

        screenVertexBuffer = ByteBuffer.allocateDirect(screenVertexes.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        screenVertexBuffer.put(screenVertexes).position(0);
    }

    @Override
    protected void clearBackground() {
//        super.clearBackground();
    }

    @Override
    protected void drawFrame(GL10 gl) {
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        shader.use();
        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        // draw plane
        glBindVertexArray(planeVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, planeTexture);
        float[] mMMatrix = getIdentityM();
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
        // draw cube
        glBindVertexArray(cubeVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, cubeTexture);
        // first cube
        mMMatrix = getIdentityM();
        Matrix.translateM(mMMatrix, 0, -1.0f, 0.0f, -1.0f);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        // second cube
        mMMatrix = getIdentityM();
        Matrix.translateM(mMMatrix, 0, 2.0f, 0.0f, 0.0f);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

//         draw screen
        GLES30.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);

        screenShader.use();
        glBindVertexArray(screenVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureColorBuffer);    // Use the color attachment texture as the texture of the quad plane
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(getClass().getSimpleName(), mContext, getVertexShader(), getFragmentShader());
        screenShader = Shader.createShader(TAG, mContext, R.raw.fbo_screen_vert, getScreenFragmentShader());
        super.onSurfaceCreated(gl, config);
        GLES30.glDepthFunc(GLES30.GL_LESS);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        createFBO();
    }

    protected int getVertexShader() {
        return R.raw.depth_test_vert;
    }

    protected int getFragmentShader() {
        return R.raw.depth_test_frag;
    }

    protected abstract int getScreenFragmentShader();

    @Override
    protected void createVAO() {
        int[] vaos = new int[3];
        int[] vbos = new int[3];

        GLES30.glGenVertexArrays(3, vaos, 0);
        GLES30.glGenBuffers(3, vbos, 0);

        // bind cubeVAO
        cubeVAO = vaos[0];
        int vbo = vbos[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, cubeVertices.length * 4, cubeBuffer, GLES30.GL_STATIC_DRAW);

        glBindVertexArray(cubeVAO);
        int positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        int texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        glBindVertexArray(0);

        // bind planeVAO
        planeVAO = vaos[1];
        vbo = vbos[1];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, planeVertices.length * 4, planeBuffer, GLES30.GL_STATIC_DRAW);

        glBindVertexArray(planeVAO);
        positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        glBindVertexArray(0);

        // bind screenVAO
        screenVAO = vaos[2];
        vbo = vaos[2];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, screenVertexes.length * 4, screenVertexBuffer, GLES30.GL_STATIC_DRAW);

        glBindVertexArray(screenVAO);
        positionHandle = screenShader.getAttribLocation("position");
        texCoordHandle = screenShader.getAttribLocation("texCoord");

        GLES30.glVertexAttribPointer(positionHandle, 2, GLES30.GL_FLOAT, false, 4 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 4 * 4, 2 * 4);
        GLES30.glEnableVertexAttribArray(texCoordHandle);

        glBindVertexArray(0);

        cubeTexture = GLUtil.bindTexture2D(mContext, R.raw.container);
        planeTexture = GLUtil.bindTexture2D(mContext, R.raw.metal);
    }

    private void createFBO() {
        int[] fbos = new int[1];
        GLES30.glGenFramebuffers(1, fbos, 0);
        FBO = fbos[0];
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        textureColorBuffer = generateAttachmentTexture();
        LogUtil.d(TAG, "textureColorBuffer: " + textureColorBuffer);
        GLES30.glFramebufferTexture2D(GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, textureColorBuffer, 0);

        int[] rbos = new int[1];
        GLES30.glGenRenderbuffers(1, rbos, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbos[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH24_STENCIL8, mScreenWidth, mScreenHeight);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0);
        GLES30.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GLES30.GL_DEPTH_STENCIL_ATTACHMENT, GLES30.GL_RENDERBUFFER, rbos[0]);

        if (GLES30.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("FrameBuffer is not complete");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private int generateAttachmentTexture() {
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
        LogUtil.d(TAG, "w: " + mScreenWidth + ", h: " + mScreenHeight);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, mScreenWidth, mScreenHeight,
                0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE, null);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLUtil.checkGlError(TAG, "generate attachment texture");
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return textures[0];
    }
}
