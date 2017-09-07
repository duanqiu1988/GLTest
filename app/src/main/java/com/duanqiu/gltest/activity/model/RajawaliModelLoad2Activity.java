package com.duanqiu.gltest.activity.model;

import android.os.Bundle;

import com.duanqiu.gltest.RajawaliBaseCameraActivity;
import com.duanqiu.gltest.render.RajawaliBaseRenderer;
import com.duanqiu.gltest.render.model.RajawaliLoadModelRenderer2;

public class RajawaliModelLoad2Activity extends RajawaliBaseCameraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected RajawaliBaseRenderer getRenderer() {
        return new RajawaliLoadModelRenderer2(this);
    }
}
