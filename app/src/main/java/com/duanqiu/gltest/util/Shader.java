package com.duanqiu.gltest.util;

import android.content.Context;
import android.opengl.GLES30;
import android.support.annotation.RawRes;
import android.util.Log;

import static com.duanqiu.gltest.util.GLUtil.checkGlError;
import static com.duanqiu.gltest.util.GLUtil.readRawTextFile;

/**
 * Created by jjduan on 3/23/17.
 */

public class Shader {
    private int program;
    private String tag;

    private Shader(String tag, Context context, @RawRes int vertex, @RawRes int frag) {
        this.tag = tag;
        program = createProgram(readRawTextFile(context, vertex), readRawTextFile(context, frag));
    }

    private Shader(String tag, String vertexShaderSource, String fragmentShaderSource) {
        this.tag = tag;
        program = createProgram(vertexShaderSource, fragmentShaderSource);
    }

    public static Shader createShader(String tag, Context context, @RawRes int vertex, @RawRes int frag) {
        return new Shader(tag, context, vertex, frag);
    }

    public static Shader createShader(String tag, String vertexShaderSource, String fragmentShaderSource) {
        return new Shader(tag, vertexShaderSource, fragmentShaderSource);
    }

    public Shader use() {
        GLES30.glUseProgram(program);
        GLUtil.checkGlError(tag, "glUseProgram");
        return this;
    }

    public int getProgram() {
        return program;
    }

    private int loadShader(int shaderType, String shaderSource) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, shaderSource);
            GLES30.glCompileShader(shader);
            int[] success = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, success, 0);
            if (success[0] != GLES30.GL_TRUE) {
                Log.e(tag, "Could not compile shader " + shaderType + ":");
                Log.e(tag, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexShaderSource, String fragmentShaderSource) {
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderSource);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderSource);
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
                Log.e(tag, "Could not link program:");
                Log.e(tag, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        LogUtil.i(tag, "create program: " + program);
        return program;
    }

    public int getAttribLocation(String attr) {
        int handle = GLES30.glGetAttribLocation(program, attr);
        if (handle == -1) {
            throw new RuntimeException("Could not find attribute location for " + attr);
        }

        return handle;
    }

    public int getUniformLocation(String uniform) {
        int handle = GLES30.glGetUniformLocation(program, uniform);
        if (handle == -1) {
            throw new RuntimeException("Could not find uniform location for " + uniform);
        }

        return handle;
    }

    public void setMat4(String uniform, float[] mat4) {
        GLES30.glUniformMatrix4fv(getUniformLocation(uniform), 1, false, mat4, 0);
    }

    public void setVec3(String uniform, float[] vec3) {
        GLES30.glUniform3fv(getUniformLocation(uniform), 1, vec3, 0);
    }
}
