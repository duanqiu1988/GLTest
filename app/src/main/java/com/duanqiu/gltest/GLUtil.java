package com.duanqiu.gltest;

import android.opengl.GLES30;
import android.util.Log;

/**
 * Created by 俊杰 on 2017/3/14.
 */

public class GLUtil {
    public static void checkGlError(String tag, String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(tag, op + ": glError " + error);
            throw new RuntimeException(op + ": glError: " + error);
        }
    }

    public static int loadShader(String tag, int shaderType, String shaderSource) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, shaderSource);
            GLES30.glCompileShader(shader);
            int[] success = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, success, 0);
            if (success[0] != GLES30.GL_TRUE) {
                Log.e(tag, "Chould not compile shader " + shaderType + ":");
                Log.e(tag, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String tag, String vertexShaderSource, String fragmentShaderSource) {
        int vertexShader = loadShader(tag, GLES30.GL_VERTEX_SHADER, vertexShaderSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(tag, GLES30.GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (fragmentShader == 0) {
            return 0;
        }

        int program;
        program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            checkGlError(tag, "attach vertex shader");
            GLES30.glAttachShader(program, fragmentShader);
            checkGlError(tag, "attach fragment shader");
            GLES30.glLinkProgram(program);
            int[] success = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, success, 0);
            if (success[0] != GLES30.GL_TRUE) {
                Log.e(tag, "Chould not link program:");
                Log.e(tag, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        return program;
    }
}
