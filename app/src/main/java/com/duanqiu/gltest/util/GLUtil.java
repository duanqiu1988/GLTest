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

        texImage2D(context.getResources().openRawResource(resId));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
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

        texImage2D(context.getResources().openRawResource(resId));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return texture;
    }
}
