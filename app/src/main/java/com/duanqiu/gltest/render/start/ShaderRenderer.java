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

public class ShaderRenderer implements GLSurfaceView.Renderer {
    public static final String TAG = "HelloTriangleRenderer";
    private static final int FLOAT_SIZE_BYTES = 4;
    private FloatBuffer triangleVertices;
    private FloatBuffer triangleVertices2;
    private Shader shader;
    private Shader shader2;
    private int VAO;
    private int VAO2;
    private final float[] triangle = {
            -0.5f, 0.5f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 0f, 1f, 0f,
            -1f, 0f, 0f, 0f, 0f, 1f
    };

    private final float[] triangle2 = {
            0.5f, 0.5f, 0f,
            1f, 0f, 0f,
            0f, 0f, 0f
    };

    private final String vertextShader =
            "precision mediump float;\n" +
            "attribute vec3 position; \n" +
                    "uniform float yOff;\n" +
                    "attribute vec3 color;\n" +
                    "varying vec3 outColor;\n" +
                    "void main() { \n" +
                    "    gl_Position = vec4(position.x, position.y + yOff, position.z, 1.0); \n" +
                    "    outColor = color;\n" +
                    "}\n";

    private final String fragmentShader =
            "precision mediump float;\n" +
            "varying vec3 outColor;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(outColor, 1.0);\n" +
                    "}\n";

    private final String vertextShader2 =
            "precision mediump float;\n" +
            "attribute vec3 position; \n" +
                    "void main() { \n" +
                    "    gl_Position = vec4(position, 1.0); \n" +
                    "}\n";

    private final String fragmentShader2 =
            "precision mediump float;\n" +
            "uniform vec4 color;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = color;\n" +
                    "}\n";

    public ShaderRenderer() {
        triangleVertices = ByteBuffer.allocateDirect(triangle.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        triangleVertices.put(triangle).position(0);

        triangleVertices2 = ByteBuffer.allocateDirect(triangle2.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        triangleVertices2.put(triangle2).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(TAG, vertextShader, fragmentShader);

        shader2 = Shader.createShader(TAG, vertextShader2, fragmentShader2);

        createVAO();
        createVAO2();
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

        long time = System.currentTimeMillis() / 200;
        float yOff = (float) ((Math.sin(time) / 2) - 0.5f);

        GLES30.glUniform1f(shader.getUniformLocation("yOff"), yOff);

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
        GLES30.glBindVertexArray(0);

        shader2.use();

        float greenValue = (float) ((Math.sin(time) / 2) + 0.5f);
        int vertexColorLocation = shader2.getUniformLocation("color");
        GLES30.glUniform4f(vertexColorLocation, 0f, greenValue, 0f, 1f);

        GLES30.glBindVertexArray(VAO2);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);

        GLES30.glBindVertexArray(0);
    }

    private void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];
        int vbo;
        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);

        VAO = vaos[0];
        vbo = vbos[0];
        GLES30.glBindVertexArray(VAO);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, triangle.length * FLOAT_SIZE_BYTES, triangleVertices, GLES30.GL_STATIC_DRAW);

        int positionHandle = shader.getAttribLocation("position");
        int colorHandle = shader.getAttribLocation("color");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 6 * FLOAT_SIZE_BYTES, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glVertexAttribPointer(colorHandle, 3, GLES30.GL_FLOAT, false, 6 * FLOAT_SIZE_BYTES, 3 * FLOAT_SIZE_BYTES);
        GLES30.glEnableVertexAttribArray(colorHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }

    private void createVAO2() {
        int[] arrays = new int[1];
        int[] buffers = new int[1];
        int buffer;
        GLES30.glGenVertexArrays(1, arrays, 0);
        GLES30.glGenBuffers(1, buffers, 0);

        VAO2 = arrays[0];
        buffer = buffers[0];
        GLES30.glBindVertexArray(VAO2);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, buffer);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, triangle2.length * FLOAT_SIZE_BYTES, triangleVertices2, GLES30.GL_STATIC_DRAW);

        int positionHandle = shader2.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * FLOAT_SIZE_BYTES, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }
}
