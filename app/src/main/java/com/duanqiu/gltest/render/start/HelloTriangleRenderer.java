package com.duanqiu.gltest.render.start;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.util.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jjduan on 3/14/17.
 */

public class HelloTriangleRenderer implements GLSurfaceView.Renderer {
    public static final String TAG = "HelloTriangleRenderer";
    private static final int FLOAT_SIZE_BYTES = 4;
    private FloatBuffer triangleVertices;
    private Shader shader;
    private int VAO;
    private final float[] triangle = {
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0f, 0.5f, 0f
    };

    private final String vertextShader =
            "attribute vec3 position; \n" +
                    "void main() { \n" +
                    "gl_Position = vec4(position, 1.0); \n" +
                    "}\n";

    private final String fragmentShader =
            "void main() {\n" +
                    "    gl_FragColor = vec4(1f, 0.5f, 0.2f, 1f);\n" +
                    "}\n";

    public HelloTriangleRenderer() {
        triangleVertices = ByteBuffer.allocateDirect(triangle.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        triangleVertices.put(triangle).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(TAG, vertextShader, fragmentShader);

        createVAO();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shader.use();

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
        GLES30.glBindVertexArray(0);
    }

    private void createVAO() {
        int[] arrays = new int[1];
        int[] buffers = new int[1];
        int buffer;
        GLES30.glGenVertexArrays(1, arrays, 0);
        GLES30.glGenBuffers(1, buffers, 0);

        VAO = arrays[0];
        buffer = buffers[0];
        GLES30.glBindVertexArray(VAO);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, buffer);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, triangle.length * FLOAT_SIZE_BYTES, triangleVertices, GLES30.GL_STATIC_DRAW);

        int positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * FLOAT_SIZE_BYTES, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }
}
