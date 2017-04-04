package com.duanqiu.gltest.render.advanced;

import android.content.Context;

import com.duanqiu.gltest.R;

/**
 * Created by 俊杰 on 2017/4/3.
 */

public class FboBlurRenderer extends FBORenderer {
    public FboBlurRenderer(Context context) {
        super(context);
    }

    @Override
    protected int getScreenFragmentShader() {
        return R.raw.fbo_screen_blur_frag;
    }
}
