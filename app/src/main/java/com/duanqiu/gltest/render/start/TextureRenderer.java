package com.duanqiu.gltest.render.start;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 俊杰 on 2017/3/14.
 */

public class TextureRenderer implements GLSurfaceView.Renderer {
    public static final String TAG = "TextureRenderer";
    private Shader shader;
    private int VAO;
    private int texture;
    private int texture2;
    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private Context mContext;
    private float mix = 0.2f;
    private final float[] vertexes = {
            // Positions         Colors     Texture Coordinates
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,    // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,  // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // top left
    };

    private final int[] indexes = {
            0, 1, 3,
            1, 2, 3
    };

    public TextureRenderer(Context context) {
        vertexBuffer = ByteBuffer.allocateDirect(vertexes.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertexes).position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexes.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        indexBuffer.put(indexes).position(0);

        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(TAG, mContext, R.raw.texture_vert, R.raw.texture_frag);

        createVAO();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        shader.use();

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
        GLES30.glUniform1i(shader.getUniformLocation("outTexture"), 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture2);
        GLES30.glUniform1i(shader.getUniformLocation("outTexture2"), 1);

        GLES30.glUniform1f(shader.getUniformLocation("_mix"), mix);

        GLES30.glBindVertexArray(VAO);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0);
        GLES30.glBindVertexArray(0);
    }

    private void createVAO() {
        int[] vaos = new int[1];
        int[] vbos = new int[1];
        int[] ebos = new int[1];

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);
        GLES30.glGenBuffers(1, ebos, 0);

        VAO = vaos[0];
        GLES30.glBindVertexArray(VAO);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vaos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexes.length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, ebos[0]);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexes.length * 4, indexBuffer, GLES30.GL_STATIC_DRAW);

//        int positionHandle = shader.getAttribLocation("position");
//        int colorHandle = shader.getAttribLocation("color");
//        int coordHandle = shader.getAttribLocation("texCoord");

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 8 * 4, 0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4);
        GLES30.glEnableVertexAttribArray(2);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);

        // texture
        texture = GLUtil.bindTexture2D(mContext, R.raw.container);
        texture2 = GLUtil.bindTexture2D(mContext, R.raw.awesomeface);
    }

    public void setMix(float mix) {
        this.mix = mix;
    }
}
