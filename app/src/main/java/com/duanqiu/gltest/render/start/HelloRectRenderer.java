package com.duanqiu.gltest.render.start;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

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

public class HelloRectRenderer implements GLSurfaceView.Renderer {
    public static final String TAG = "HelloRectRenderer";
    private Shader shader;
    private int VAO;
    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private Context mContext;
    private final float[] vertexes = {
            0.5f, 0.5f, 0f,
            0.5f, -0.5f, 0f,
            -0.5f, -0.5f, 0f,
            -0.5f, 0.5f, 0f
    };

    private final int[] indexes = {
            0, 1, 3,
            1, 2, 3
    };

    private final String vertexShader =
            "precision mediump float;\n" +
                    "attribute vec3 position;\n" +
                    "void main() {\n" +
                    "    gl_Position = vec4(position, 1.0);\n" +
                    "}\n";

    private final String fragmentShader =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "   gl_FragColor = vec4(1.0, 0.5, 0.2, 1.0);\n" +
                    "}\n";

    public HelloRectRenderer(Context context) {
        mContext = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexes.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertexes).position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexes.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        indexBuffer.put(indexes).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shader = Shader.createShader(TAG, mContext, R.raw.hello_rect_vert, R.raw.hello_rect_frag);

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

        int positionHandle = shader.getAttribLocation("position");

        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }
}
