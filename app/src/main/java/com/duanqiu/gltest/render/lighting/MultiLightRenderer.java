package com.duanqiu.gltest.render.lighting;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.duanqiu.gltest.R;
import com.duanqiu.gltest.util.DirectionalLight;
import com.duanqiu.gltest.util.GLUtil;
import com.duanqiu.gltest.util.PointLight;
import com.duanqiu.gltest.util.SpotLight;
import com.duanqiu.gltest.util.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class MultiLightRenderer extends BaseLightingRenderer {
    private int woodTexture;
    private int steelTexture;
    private DirectionalLight dirLight;
    private PointLight[] pointLights;
    private SpotLight spotLight;

    private float[] vertices = {
            // Positions          // Normals           // Texture Coords
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    private Vector3[] pointLightPosition = {
            new Vector3(0.7f, 0.2f, 2.0f),
            new Vector3(2.3f, -3.3f, -4.0f),
            new Vector3(-4.0f, 2.0f, -12.0f),
            new Vector3(0.0f, 0.0f, -3.0f)
    };

    private Vector3[] pointLightColors = {
            new Vector3(1.0f, 0.6f, 0.0f),
            new Vector3(1.0f, 0.0f, 0.0f),
            new Vector3(1.0f, 1.0f, 0.0f),
            new Vector3(0.2f, 0.2f, 1.0f)
    };

    public MultiLightRenderer(Context context) {
        super(context);

        dirLight = new DirectionalLight(new Vector3(0.3f, 0.24f, 0.14f), new Vector3(0.7f, 0.42f, 0.26f),
                new Vector3(0.5f, 0.5f, 0.5f), new Vector3(-0.2f, -1.0f, -0.3f));
        pointLights = new PointLight[4];
        pointLights[0] = new PointLight(pointLightColors[0].scale(0.1f), pointLightColors[0], pointLightColors[0],
                pointLightPosition[0], 1.0f, 0.09f, 0.32f);
        pointLights[1] = new PointLight(pointLightColors[1].scale(0.1f), pointLightColors[1], pointLightColors[1],
                pointLightPosition[1], 1.0f, 0.09f, 0.32f);
        pointLights[2] = new PointLight(pointLightColors[2].scale(0.1f), pointLightColors[2], pointLightColors[2],
                pointLightPosition[2], 1.0f, 0.09f, 0.32f);
        pointLights[3] = new PointLight(pointLightColors[3].scale(0.1f), pointLightColors[3], pointLightColors[3],
                pointLightPosition[3], 1.0f, 0.09f, 0.32f);
        spotLight = new SpotLight(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.8f, 0.8f, 0.0f), new Vector3(0.8f, 0.8f, 0.0f),
                mCamera.postion, mCamera.front, 1.0f, 0.09f, 0.32f,
                (float) Math.cos(Math.toRadians(12.5f)), (float) Math.cos(Math.toRadians(13.0f)));
    }

    @Override
    protected int getVertexShader() {
        return R.raw.multi_light_vert;
    }

    @Override
    protected int getFragmentShader() {
        return R.raw.multi_light_frag;
    }

    @Override
    protected float[] getVertices() {
        return vertices;
    }

    @Override
    protected void drawObject(GL10 gl) {
        // draw VAO
        shader.use();

        // viewPos
        GLES30.glUniform3f(shader.getUniformLocation("viewPos"), mCamera.postion.x, mCamera.postion.y, mCamera.postion.z);

        // Directional Light
        GLES30.glUniform3f(shader.getUniformLocation("dirLight.ambient"), dirLight.ambient.x, dirLight.ambient.y, dirLight.ambient.z);
        GLES30.glUniform3f(shader.getUniformLocation("dirLight.diffuse"), dirLight.diffuse.x, dirLight.diffuse.y, dirLight.diffuse.z);
        GLES30.glUniform3f(shader.getUniformLocation("dirLight.specular"), dirLight.specular.x, dirLight.specular.y, dirLight.specular.z);
        GLES30.glUniform3f(shader.getUniformLocation("dirLight.direction"), dirLight.direction.x, dirLight.direction.y, dirLight.direction.z);

        // Point Light
        for (int i = 0; i < pointLights.length; i++) {
            StringBuilder location = new StringBuilder("pointLights[");
            location.append(i).append("].");
            GLES30.glUniform3f(shader.getUniformLocation(location.toString() + "ambient"), pointLights[i].ambient.x, pointLights[i].ambient.y, pointLights[i].ambient.z);
            GLES30.glUniform3f(shader.getUniformLocation(location.toString() + "diffuse"), pointLights[i].diffuse.x, pointLights[i].diffuse.y, pointLights[i].diffuse.z);
            GLES30.glUniform3f(shader.getUniformLocation(location.toString() + "specular"), pointLights[i].specular.x, pointLights[i].specular.y, pointLights[i].specular.z);
            GLES30.glUniform3f(shader.getUniformLocation(location.toString() + "position"), pointLights[i].position.x, pointLights[i].position.y, pointLights[i].position.z);
            GLES30.glUniform1f(shader.getUniformLocation(location.toString() + "constant"), pointLights[i].constant);
            GLES30.glUniform1f(shader.getUniformLocation(location.toString() + "linear"), pointLights[i].linear);
            GLES30.glUniform1f(shader.getUniformLocation(location.toString() + "quadratic"), pointLights[i].quadratic);
        }

        // Spot Light
        GLES30.glUniform3f(shader.getUniformLocation("spotLight.ambient"), spotLight.ambient.x, spotLight.ambient.y, spotLight.ambient.z);
        GLES30.glUniform3f(shader.getUniformLocation("spotLight.diffuse"), spotLight.diffuse.x, spotLight.diffuse.y, spotLight.diffuse.z);
        GLES30.glUniform3f(shader.getUniformLocation("spotLight.specular"), spotLight.specular.x, spotLight.specular.y, spotLight.specular.z);
        GLES30.glUniform3f(shader.getUniformLocation("spotLight.direction"), spotLight.direction.x, spotLight.direction.y, spotLight.direction.z);
        GLES30.glUniform3f(shader.getUniformLocation("spotLight.position"), spotLight.position.x, spotLight.position.y, spotLight.position.z);

        GLES30.glUniform1f(shader.getUniformLocation("spotLight.cutOff"), spotLight.cutOff);
        GLES30.glUniform1f(shader.getUniformLocation("spotLight.outerCutOff"), spotLight.outerCutOff);
        GLES30.glUniform1f(shader.getUniformLocation("spotLight.constant"), spotLight.constant);
        GLES30.glUniform1f(shader.getUniformLocation("spotLight.linear"), spotLight.linear);
        GLES30.glUniform1f(shader.getUniformLocation("spotLight.quadratic"), spotLight.quadratic);


        // Material
        GLES30.glUniform1f(shader.getUniformLocation("material.shininess"), 64.0f);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, woodTexture);
        GLES30.glUniform1i(shader.getUniformLocation("material.diffuse"), 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, steelTexture);
        GLES30.glUniform1i(shader.getUniformLocation("material.specular"), 1);

        mCamera.setLookAtM(mVMatrix);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);

        GLES30.glBindVertexArray(VAO);
        for (int i = 0; i < 10; i++) {
            float[] mMMatrix = getUnitMatrix4f();

            float angle = i * 20;
            Matrix.setRotateM(mMMatrix, 0, angle, 1.0f, 0.3f, 0.5f);
            Matrix.translateM(mMMatrix, 0, cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);

            GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }
    }

    @Override
    protected void drawLamb(GL10 gl) {
        // draw lambVAO
        lambShader.use();
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("view"), 1, false, mVMatrix, 0);
        GLES30.glUniformMatrix4fv(lambShader.getUniformLocation("projection"), 1, false, mProjMatrix, 0);
        GLES30.glBindVertexArray(lambVAO);

        for (int i = 0; i < 4;i++){
            float[] mMMatrix = getUnitMatrix4f();
            Matrix.translateM(mMMatrix, 0, pointLights[i].position.x, pointLights[i].position.y, pointLights[i].position.z);
            Matrix.scaleM(mMMatrix, 0, 0.2f, 0.2f, 0.2f);
            GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, mMMatrix, 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }
        GLES30.glBindVertexArray(0);
    }

    @Override
    protected void createVAO() {
        int[] vaos = new int[2];
        int[] vbos = new int[1];

        GLES30.glGenVertexArrays(2, vaos, 0);
        GLES30.glGenBuffers(1, vbos, 0);
        VAO = vaos[0];
        int vbo = vbos[0];

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, getVertices().length * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);

        // bind VAO
        GLES30.glBindVertexArray(VAO);
        int positionHandle = shader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 0);
        int normalHandle = shader.getAttribLocation("normal");
        GLES30.glVertexAttribPointer(normalHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4);
        int texCoordHandle = shader.getAttribLocation("texCoord");
        GLES30.glVertexAttribPointer(texCoordHandle, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4);

        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(normalHandle);
        GLES30.glEnableVertexAttribArray(texCoordHandle);
        GLES30.glBindVertexArray(0);

        // bind lambVAO
        lambVAO = vaos[1];
        GLES30.glBindVertexArray(lambVAO);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo);
        positionHandle = lambShader.getAttribLocation("position");
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 8 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindVertexArray(0);

        woodTexture = GLUtil.bindTexture2D(mContext, R.raw.container2);
        steelTexture = GLUtil.bindTexture2D(mContext, R.raw.container2_specular);
    }
}
