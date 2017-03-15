package com.duanqiu.gltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                Log.e(tag, "Could not compile shader " + shaderType + ":");
                Log.e(tag, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static String readRawTextFile(Context context, @RawRes int resId) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resId)));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return builder.toString();
    }


    public static int createProgram(String tag, Context context, @RawRes int vertex, @RawRes int frag) {
        return createProgram(tag, readRawTextFile(context, vertex), readRawTextFile(context, frag));
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
                Log.e(tag, "Could not link program:");
                Log.e(tag, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        return program;
    }

    public static int getAttribLocation(int program, String attr) {
        int handle = GLES30.glGetAttribLocation(program, attr);
        if (handle == -1) {
            throw new RuntimeException("Could not find attribute location for " + attr);
        }

        return handle;
    }

    public static void texImage2D(InputStream inputStream) {
        Bitmap bm;
        try {
            bm = BitmapFactory.decodeStream(inputStream);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bm, 0);
        bm.recycle();
    }

    public static int bindTexture2D(Context context, @RawRes int resId){
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        int texture = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);

        // wrap parameter
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

        // filter parameter
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        texImage2D(context.getResources().openRawResource(resId));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return texture;
    }
}
