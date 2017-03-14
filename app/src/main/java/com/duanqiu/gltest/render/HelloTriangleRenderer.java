package com.duanqiu.gltest.render;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
    private int program;
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
        program = createProgram(vertextShader, fragmentShader);
        if (program == 0) {
            return;
        }

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
        GLES30.glUseProgram(program);
        checkGlError("glUseProgram");

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
        GLES30.glBindVertexArray(0);
    }

    private int loadShader(int shaderType, String shaderSource) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, shaderSource);
            GLES30.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] != GLES30.GL_TRUE) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    private int createProgram(String vertextShaderSource, String fragmentShaderSource) {
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertextShaderSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (fragmentShader == 0) {
            return 0;
        }

        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            checkGlError("attach vertex shader");
            GLES30.glAttachShader(program, fragmentShader);
            checkGlError("attach fragment shader");
            GLES30.glLinkProgram(program);

            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e(TAG, "Chould not link program:");
                Log.e(TAG, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        return program;
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

        int positionHandle = GLES30.glGetAttribLocation(program, "position");
        if (positionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for position");
        }
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * FLOAT_SIZE_BYTES, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
