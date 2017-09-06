package com.duanqiu.gltest.render.model;

import android.content.Context;
import android.view.MotionEvent;

import com.duanqiu.gltest.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;


/**
 * Created by duanjunjie on 17-9-6.
 */

public class RajawaliLoadModelRenderer extends RajawaliRenderer {
    private PointLight mLight;
    private Object3D mObjectGroup;
    private Animation3D mCameraAnim, mLightAnim;

    public RajawaliLoadModelRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        mLight = new PointLight();
        mLight.setPosition(0, 0, 4);
        mLight.setPower(10);

        getCurrentScene().addLight(mLight);
        getCurrentCamera().setZ(16);

        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
                mTextureManager, R.raw.nanosuit_obj);
        try {
            objParser.parse();
            mObjectGroup = objParser.getParsedObject();
            mObjectGroup.moveUp(-10);
            getCurrentScene().addChild(mObjectGroup);
            getCurrentScene().setBackgroundColor(0.2f, 0.3f, 0.3f, 1f);

            mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
            mCameraAnim.setDurationMilliseconds(8000);
            mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
            mCameraAnim.setTransformable3D(mObjectGroup);
        } catch (ParsingException e) {
            e.printStackTrace();
        }

        mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
                new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
                360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

        mLightAnim.setDurationMilliseconds(3000);
        mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mLightAnim.setTransformable3D(mLight);

        getCurrentScene().registerAnimation(mCameraAnim);
        getCurrentScene().registerAnimation(mLightAnim);

        mCameraAnim.play();
        mLightAnim.play();
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
