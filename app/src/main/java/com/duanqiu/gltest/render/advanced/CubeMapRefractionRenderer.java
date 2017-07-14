package com.duanqiu.gltest.render.advanced;

import android.content.Context;

import com.duanqiu.gltest.R;

/**
 * Created by duanjunjie on 17-7-14.
 */

public class CubeMapRefractionRenderer extends CubeMapRenderer {
    public CubeMapRefractionRenderer(Context context) {
        super(context);
    }

    @Override
    protected int getCubeVertexShader() {
        return R.raw.cube_map_vert;
    }

    @Override
    protected int getCubeFragmentShader() {
        return R.raw.cube_map_refraction_frag;
    }
}
