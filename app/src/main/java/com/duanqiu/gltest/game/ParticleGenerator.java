package com.duanqiu.gltest.game;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.util.Shader;
import com.duanqiu.gltest.util.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.opengl.GLES30.glBindVertexArray;

/**
 * Created by duanjunjie on 17-7-28.
 */

public class ParticleGenerator {
    public static final String TAG = ParticleGenerator.class.getSimpleName();
    private List<Particle> particles = new ArrayList<>();
    private int amount;
    private Shader shader;
    private int texture;
    private int VAO;
    private Random random;
    private int lastUsedParticle = 0;
    private float[] modelMatrix4 = new float[16];
    private Vector2 particleSize;
    private float offX;
    private float offY;

    public ParticleGenerator(Shader shader, int texture, int amount, Vector2 size) {
        this.shader = shader;
        this.texture = texture;
        this.amount = amount;
        this.particleSize = size;
        random = new Random();
        init();
    }


    void update(float dt, GameObject object) {

        for (int i = 0; i < 2; i++) {
            int unusedParticle = firstUnusedParticle();
            particles.get(unusedParticle).life = 1;
            particles.get(unusedParticle).color.a = 1;
        }

        if (object.velocity.x > 0) {
            offX = 0;
            if (object.velocity.y > 0) {
                offY = 0;
            } else {
                offY = particleSize.y;
            }
        } else {
            offX = particleSize.x;
            if (object.velocity.y > 0) {
                offY = 0;
            } else {
                offY = particleSize.y;
            }
        }

        for (Particle particle : particles) {
            particle.life -= dt * 0.01;
            int rx = random.nextInt(10);
            int ry = (int) (particleSize.y / particleSize.x * rx * random.nextFloat());
            particle.position.x = object.position.x + offX - object.velocity.x * dt / particle.life * rx;
            particle.position.y = object.position.y + offY - object.velocity.y * dt / particle.life * ry;

            if (particle.life > 0) {
                particle.color.a -= dt * 0.01;
                if (particle.color.a <= 0) {
                    particle.color.a = 0;
                }
            }
        }
    }

    void draw() {
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE);
        shader.use();
        for (Particle particle : particles) {
            if (particle.life > 0.0f) {
                Matrix.setIdentityM(modelMatrix4, 0);
                float x = 2 * particle.position.x - 1;
                float y = 2 * (1 - particle.position.y) - 1 - 2 * particle.size.y;

                Matrix.translateM(modelMatrix4, 0, x, y, 0);
                Matrix.scaleM(modelMatrix4, 0, 2 * particle.size.x, 2 * particle.size.y, 1);

                shader.setMat4("model", modelMatrix4);
                shader.setVec4("color", particle.color.floatValue());

                GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
                GLES30.glBindVertexArray(VAO);
                GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
                GLES30.glBindVertexArray(0);
            }
        }
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void init() {
        int vaos[] = new int[1];
        int vbos[] = new int[1];
        float[] particle_quad = {
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        FloatBuffer buffer = ByteBuffer.allocateDirect(particle_quad.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(particle_quad).position(0);

        GLES30.glGenVertexArrays(1, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);
        VAO = vaos[0];
        glBindVertexArray(VAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbos[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, particle_quad.length * 4, buffer, GLES30.GL_STATIC_DRAW);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 4, GLES30.GL_FLOAT, false, 4 * 4, 0);
        glBindVertexArray(0);

        for (int i = 0; i < amount; i++)
            particles.add(new Particle(new Vector2(0), new Vector2(particleSize.x, particleSize.y),
                    new Vector2(0), new Color(1), 0));
    }


    int firstUnusedParticle() {
        for (int i = lastUsedParticle; i < amount; i++) {
            if (particles.get(i).life <= 0.0f) {
                lastUsedParticle = i;
                return i;
            }
        }

        for (int i = 0; i < lastUsedParticle; i++) {
            if (particles.get(i).life <= 0.0f) {
                lastUsedParticle = i;
                return i;
            }
        }
        lastUsedParticle = 0;
        return 0;
    }
}
