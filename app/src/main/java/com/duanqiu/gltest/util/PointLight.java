package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/28/17.
 */

public class PointLight extends Light {
    public Vector3 position;
    public float constant;
    public float linear;
    public float quadratic;

    public PointLight(Vector3 ambient, Vector3 diffuse, Vector3 specular, Vector3 position, float constant, float linear, float quadratic) {
        super(ambient, diffuse, specular);
        this.position = position;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }
}
