package com.duanqiu.gltest.util;

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
import java.util.List;

/**
 * Created by 俊杰 on 2017/3/14.
 */

public class GLUtil {
    public static final String TAG = GLUtil.class.getSimpleName();

    public static void checkGlError(String tag, String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(tag, op + ": glError " + error);
            throw new RuntimeException(op + ": glError: " + error);
        }
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

    public static void texImage2D(int type, InputStream inputStream) {
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

        GLUtils.texImage2D(type, 0, bm, 0);
        bm.recycle();
    }

    public static int bindTexture2D(Context context, @RawRes int resId) {
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

        texImage2D(GLES30.GL_TEXTURE_2D, context.getResources().openRawResource(resId));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return texture;
    }

    public static int bindCubeTexture(Context context, List<Integer> resIds) {
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        int texture = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, texture);

        int size = resIds.size();
        for (int i = 0; i < size; i++) {
            texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, context.getResources().openRawResource(resIds.get(i)));
        }

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE);

        return texture;
    }

    public static int bindTexture2D(Context context, @RawRes int resId, int wrapMode) {
        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        int texture = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);

        // wrap parameter
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, wrapMode);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, wrapMode);

        // filter parameter
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        texImage2D(GLES30.GL_TEXTURE_2D, context.getResources().openRawResource(resId));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return texture;
    }

    private static float[] tempMat3 = new float[9];
    private static float[] tempMat4 = new float[16];

    public static float[] mat3(float[] mat4) {
        tempMat3[0] = mat4[0];
        tempMat3[1] = mat4[1];
        tempMat3[2] = mat4[2];
        tempMat3[3] = mat4[4];
        tempMat3[4] = mat4[5];
        tempMat3[5] = mat4[6];
        tempMat3[6] = mat4[8];
        tempMat3[7] = mat4[9];
        tempMat3[8] = mat4[10];
        return tempMat3;
    }

    public static float[] mat4(float[] mat3) {
        tempMat4[0] = mat3[0];
        tempMat4[1] = mat3[1];
        tempMat4[2] = mat3[2];
        tempMat4[3] = 0;
        tempMat4[4] = mat3[3];
        tempMat4[5] = mat3[4];
        tempMat4[6] = mat3[5];
        tempMat4[7] = 0;
        tempMat4[8] = mat3[6];
        tempMat4[9] = mat3[7];
        tempMat4[10] = mat3[8];
        tempMat4[11] = 0;
        tempMat4[12] = 0;
        tempMat4[13] = 0;
        tempMat4[14] = 0;
        tempMat4[15] = 0;
        return tempMat4;
    }
}
