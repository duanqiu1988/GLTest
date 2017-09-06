package com.duanqiu.gltest.render.model;

import android.content.Context;
import android.opengl.Matrix;

import com.duanqiu.gltest.render.BaseCameraRenderer;
import com.duanqiu.gltest.render.ObjectRenderer;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by duanjunjie on 17-9-6.
 */

public class ModelRenderer extends BaseCameraRenderer {
    private ObjectRenderer mVirtualObject = new ObjectRenderer();
    private ObjectRenderer mVirtualObject2 = new ObjectRenderer();

    public ModelRenderer(Context context) {
        super(context);
    }

    @Override
    protected void prepareVertexBuffer() {
        try {
            mVirtualObject.createOnGlThread(mContext, "andy.obj", "andy.png");
            mVirtualObject.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);

            mVirtualObject2.createOnGlThread(mContext, "andy.obj", "andy.png");
            mVirtualObject2.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void drawFrame(GL10 gl) {
        mCamera.setLookAtM(mVMatrix);
        mVirtualObject.updateModelMatrix(getIdentityM(), 5);
        mVirtualObject.draw(mVMatrix, mProjMatrix, 1);

        Matrix.translateM(mModelMatrix, 0, -1.5f, 0.0f, -0.48f);
        mVirtualObject2.updateModelMatrix(mModelMatrix, 5);
        mVirtualObject2.draw(mVMatrix, mProjMatrix, 1);
    }

    @Override
    protected void createVAO() {

    }
}
