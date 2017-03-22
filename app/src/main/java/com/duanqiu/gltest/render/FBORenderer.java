package com.duanqiu.gltest.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.LogUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 俊杰 on 2017/3/14.
 */

public class FBORenderer implements GLSurfaceView.Renderer {
    public static final String TAG = "FBORenderer";
    private int program;
    private int screenProgram;
    private int VAO;
    private int screenVAO;
    private int texture;
    private int texture2;
    private FloatBuffer vertexBuffer;
    private FloatBuffer screenVertexBuffer;
    private Context mContext;
    private float mix = 0.2f;
    private int FBO;
    private int textureColorBuffer;
    private int mScreenWidth;
    private int mScreenHeight;
    private final float[] vertexes = {
            // Positions     Texture Coordinates
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,  // bottom left
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,    // top left
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f   // top right
    };

    private final float[] screenVertexes = {
            // Positions     Texture Coordinates
            -1f, -1f, 0.0f, 0.0f, 0.0f,  // bottom left
            -1f, 1f, 0.0f, 0.0f, 1.0f,    // top left
            1f, -1f, 0.0f, 1.0f, 0.0f,   // bottom right
            1f, 1f, 0.0f, 1.0f, 1.0f   // top right
    };

    private float[] mProjMatrix = new float[16];
    private float[] mMMatrix = new float[16];
    private float[] mVMatrix = new float[16];

    public FBORenderer(Context context) {
        vertexBuffer = ByteBuffer.allocateDirect(vertexes.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertexes).position(0);

        screenVertexBuffer = ByteBuffer.allocateDirect(screenVertexes.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        screenVertexBuffer.put(screenVertexes).position(0);

        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.d(TAG, "onSurfaceCreated");
        program = GLUtil.createProgram(TAG, mContext, R.raw.fbo_vert, R.raw.fbo_frag);
        screenProgram = GLUtil.createProgram(TAG, mContext, R.raw.fbo_screen_vert, R.raw.fbo_screen_frag);
        if (program == 0 || screenProgram == 0) {
            return;
        }


        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        LogUtil.d(TAG, "onSurfaceChanged width: " + width+", height: "+height+", screenWidth: "+mScreenWidth+", screenHeight: " +mScreenHeight);
        createVAO();
        createScreenVAO();
        createFBO();
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        /********************************** draw fbo **********************************/
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, FBO);
        GLES30.glClearColor(1f, 0.5f, 0.1f, 0.1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUseProgram(program);
        GLUtil.checkGlError(TAG, "glUseProgram");

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "outTexture"), 0);

//        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture2);
//        GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "outTexture2"), 1);

        Matrix.setRotateM(mMMatrix, 0, 45, 1, 0, 0);

        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "model"), 1, false, mMMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "projection"), 1, false, mProjMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glBindVertexArray(0);

        /********************************** draw screen from fbo ************************************/

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glClearColor(1f, 1f, 1f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUseProgram(screenProgram);
        GLUtil.checkGlError(TAG, "glUseScreenProgram");
        GLES30.glBindVertexArray(screenVAO);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureColorBuffer);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glBindVertexArray(0);
    }

    private void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);

        VAO = vaos[0];
        GLES30.glBindVertexArray(VAO);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vaos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexes.length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);


        int positionHandle = GLUtil.getAttribLocation(program, "position");
        int coordHandle = GLUtil.getAttribLocation(program, "texCoord");

        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glVertexAttribPointer(coordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);
        GLES30.glEnableVertexAttribArray(coordHandle);

        GLES30.glBindVertexArray(0);


        // texture
        texture = GLUtil.bindTexture2D(mContext, R.raw.container);
        texture2 = GLUtil.bindTexture2D(mContext, R.raw.awesomeface);
        LogUtil.d(TAG, "texture: " + texture + ", texture2: " + texture2);
    }

    private void createScreenVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);

        screenVAO = vaos[0];
        GLES30.glBindVertexArray(screenVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, screenVertexes.length * 4, screenVertexBuffer, GLES30.GL_STATIC_DRAW);

        int positionHandle = GLES30.glGetAttribLocation(screenProgram, "position");
        int coordHandle = GLES30.glGetAttribLocation(screenProgram, "texCoord");

        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 5 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(coordHandle, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4);
        GLES30.glEnableVertexAttribArray(coordHandle);

        GLES30.glBindVertexArray(0);
    }

    private void createFBO() {
        int[] fbos = new int[1];
        GLES30.glGenFramebuffers(1, fbos, 0);
        FBO = fbos[0];
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, FBO);
        textureColorBuffer = generateAttachmentTexture();
        LogUtil.d(TAG, "textureColorBuffer: " + textureColorBuffer);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, textureColorBuffer, 0);

        int[] rbos = new int[1];
        GLES30.glGenRenderbuffers(1, rbos, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rbos[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH24_STENCIL8, mScreenWidth, mScreenHeight);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0);
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_STENCIL_ATTACHMENT, GLES30.GL_RENDERBUFFER, rbos[0]);
        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("FrameBuffer is not complete");
        }

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    private int generateAttachmentTexture() {
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, mScreenWidth, mScreenHeight,
                0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE, null);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLUtil.checkGlError(TAG, "generate attachment texture");
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return textures[0];
    }

    public void setMix(float mix) {
        this.mix = mix;
    }

    public void setScreenSize(int w, int h) {
        mScreenWidth = w;
        mScreenHeight = h;
        LogUtil.d(TAG, "w: " + w + ", h: " + h);
    }
}
