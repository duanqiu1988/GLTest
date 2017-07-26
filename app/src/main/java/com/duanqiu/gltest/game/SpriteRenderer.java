package com.duanqiu.gltest.game;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.util.LogUtil;
import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by duanjunjie on 17-7-26.
 */

public class SpriteRenderer {
    private Shader shader;
    private int quadVAO;
    private float[] modelMatrix4 = new float[16];

    public SpriteRenderer(Shader shader) {
        this.shader = shader;
        initRenderData();
    }

    public void drawSprite(int texture, Vector2 position, Vector2 size, float rotate, Vector3 color) {
        // Prepare transformations
        shader.use();

        LogUtil.d("render", "program " + shader.getProgram() + " texture " + texture + " vao " + quadVAO);
        Matrix.setIdentityM(modelMatrix4, 0);
        Matrix.scaleM(modelMatrix4, 0, size.x, size.y, 1);
        Matrix.translateM(modelMatrix4, 0, -0.5f * size.x, -0.5f * size.y, 0.0f);
        Matrix.rotateM(modelMatrix4, 0, rotate, 0, 0, 1);
        Matrix.translateM(modelMatrix4, 0, 0.5f * size.x, 0.5f * size.y, 0.0f);
        Matrix.translateM(modelMatrix4, 0, position.x, position.y, 0);

        shader.setMat4("model", modelMatrix4);

        // Render textured quad
        shader.setVec3("spriteColor", color.floatValue());

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);

        GLES30.glBindVertexArray(quadVAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
        GLES30.glBindVertexArray(0);
    }

    private void initRenderData() {
        // Configure VAO/VBO

        float[] vertices = {
                // Pos      // Tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        FloatBuffer buffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(vertices).position(0);

        int[] vaos = new int[1];
        int[] vbos = new int[1];
        GLES30.glGenVertexArrays(1, vaos, 0);
        quadVAO = vaos[0];
        GLES30.glGenBuffers(1, vbos, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindVertexArray(quadVAO);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 4, GLES30.GL_FLOAT, false, 4 * 4, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }
}
