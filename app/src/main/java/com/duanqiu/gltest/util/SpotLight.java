package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/28/17.
 */

public class SpotLight extends PointLight {
    public Vector3 direction;
    public float cutOff;
    public float outerCutOff;

    public SpotLight(Vector3 ambient, Vector3 diffuse, Vector3 specular, Vector3 position, Vector3 direction, float constant, float linear, float quadratic, float cutOff, float outerCutOff) {
        super(ambient, diffuse, specular, position, constant, linear, quadratic);
        this.direction = direction;
        this.cutOff = cutOff;
        this.outerCutOff = outerCutOff;
    }
}
