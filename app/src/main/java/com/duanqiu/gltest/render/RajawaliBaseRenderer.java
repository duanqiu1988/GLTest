package com.duanqiu.gltest.render;

import android.content.Context;
import android.view.MotionEvent;

import com.duanqiu.gltest.util.Camera;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by duanjunjie on 17-9-6.
 */

public abstract class RajawaliBaseRenderer extends RajawaliRenderer implements Camera.CameraProcessListener {
    public static final String TAG = RajawaliBaseRenderer.class.getSimpleName();

    public RajawaliBaseRenderer(Context context) {
        super(context);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void processKeyboard(Camera.CameraMovement direction) {
        if (direction == Camera.CameraMovement.FORWARD) {
            getCurrentCamera().moveForward(-0.1);
        } else if (direction == Camera.CameraMovement.BACKWARD) {
            getCurrentCamera().moveForward(0.1);
        } else if (direction == Camera.CameraMovement.LEFT) {
            getCurrentCamera().moveRight(-0.1);
        } else if (direction == Camera.CameraMovement.RIGHT) {
            getCurrentCamera().moveRight(0.1);
        } else if (direction == Camera.CameraMovement.UP) {
            getCurrentCamera().moveUp(0.1);
        } else if (direction == Camera.CameraMovement.DOWN) {
            getCurrentCamera().moveUp(-0.1);
        }
    }

    @Override
    public void processMouseMovement(float right, float up) {
        getCurrentCamera().rotateAround(Vector3.Y, right * 0.005, true);
        getCurrentCamera().rotateAround(Vector3.X, -up * 0.005, true);
    }

    @Override
    public void processMouseScroll(float up) {
        double zoom = getCurrentCamera().getFieldOfView();
        if (zoom >= 1.0f && zoom <= 45.0f) {
            zoom -= up / 100;
        }

        if (zoom <= 1.0f) {
            zoom = 1.0f;
        }

        if (zoom > 45.0f) {
            zoom = 45.0f;
        }
        getCurrentCamera().setFieldOfView(zoom);
    }
}
