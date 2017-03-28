package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/28/17.
 */

public class LightFactory {
    public static Light newLight(Light light) {
        return new Light(light.ambient, light.diffuse, light.specular);
    }

    public static DirectionalLight newDirectionalLight(DirectionalLight light) {
        return new DirectionalLight(light.ambient, light.diffuse, light.specular, light.direction);
    }

    public static PointLight newPointLight(PointLight light) {
        return new PointLight(light.ambient, light.diffuse, light.specular,
                light.position, light.constant, light.linear, light.quadratic);
    }

    public static SpotLight newSpotLight(SpotLight light) {
        return new SpotLight(light.ambient, light.diffuse, light.specular,
                light.position, light.direction,
                light.constant, light.linear, light.quadratic,
                light.cutOff, light.outerCutOff);
    }
}
