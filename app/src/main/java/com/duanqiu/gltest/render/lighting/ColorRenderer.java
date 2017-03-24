package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.glsurface.CameraSurfaceView;
import com.duanqiu.gltest.util.Camera;
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

public class ColorRenderer implements GLSurfaceView.Renderer, CameraSurfaceView.OnGestureListener {
    public static final String TAG = "ColorRenderer";
    private Shader lightShader;
    private Shader lambShader;
    private Camera mCamera;
    private int VAO;
    private int lambVAO;
    private FloatBuffer vertexBuffer;
    private Context mContext;

    float[] vertices = {
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

    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];

    public ColorRenderer(Context context) {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        mContext = context;
        mCamera = new Camera(new Vector3(0f, 0f, 3f));
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        lightShader = Shader.createShader(TAG, mContext, R.raw.lighting_vert, R.raw.lighting_frag);
        lambShader = Shader.createShader(TAG, mContext, R.raw.lighting_vert, R.raw.lamb_frag);
        createVAO();
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
//        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1f, 100);
        Matrix.perspectiveM(mProjMatrix, 0, 45f, ratio, 0.01f, 100);
    }

    private long currentFrame = 0;
    private long lastFrame = 0;
    private float deltaTime = 0.0f;

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        currentFrame = SystemClock.uptimeMillis();
        deltaTime = (currentFrame - lastFrame) / 1000f;
        lastFrame = currentFrame;

        // draw VAO
        lightShader.use();
        GLES30.glUniform3f(lightShader.getUniformLocation("objectColor"), 1.0f, 0.5f, 0.31f);
        GLES30.glUniform3f(lightShader.getUniformLocation("lightColor"), 1.0f, 0.5f, 1f);

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(lightShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(lightShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        float[] mMMatrix = {
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        };
        
        GLES30.glUniformMatrix4fv(lightShader.getUniformLocation("model"), 1, false, mMMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        GLES30.glBindVertexArray(0);

        // draw lambVAO
        lambShader.use();
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        GLES30.glBindVertexArray(lambVAO);
        float[] mMMatrix2 = {
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        };
        Matrix.translateM(mMMatrix2, 0, 1.2f, 1.0f, 2.0f);
        Matrix.scaleM(mMMatrix2, 0, 0.2f, 0.2f, 0.2f);
        GLES30.glUniformMatrix4fv(lightShader.getUniformLocation("model"), 1, false, mMMatrix2, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        GLES30.glBindVertexArray(0);
    }

    private void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);
        VAO = vaos[0];
        int vbo = vbos[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);

        // bind VAO
        GLES30.glBindVertexArray(VAO);
        int positionHandle = lightShader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glBindVertexArray(0);

        // bind lambVAO
        vaos = new int[1];
        GLES30.glGenVertexArrays(1, vaos, 0);
        lambVAO = vaos[0];
        GLES30.glBindVertexArray(lambVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        positionHandle = lambShader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindVertexArray(0);
    }

    @Override
    public void onX(boolean left) {
        if (left) {
            mCamera.processKeyboard(Camera.CameraMovement.LEFT, deltaTime);
        } else {
            mCamera.processKeyboard(Camera.CameraMovement.RIGHT, deltaTime);
        }
    }

    @Override
    public void onY(boolean top) {
        if (top) {
            mCamera.processMouseMovement(0, deltaTime * 300, true);
        } else {
            mCamera.processMouseMovement(0, -deltaTime * 300, true);
        }
    }

    @Override
    public void onZ(boolean pinchIn) {
        if (pinchIn) {
            mCamera.processKeyboard(Camera.CameraMovement.BACKWARD, deltaTime);
        } else {
            mCamera.processKeyboard(Camera.CameraMovement.FORWARD, deltaTime);
        }
    }
}
