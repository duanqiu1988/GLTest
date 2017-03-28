package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/28/17.
 */

public class DirectionalLight extends Light {
    public Vector3 direction;

    public DirectionalLight(Vector3 ambient, Vector3 diffuse, Vector3 specular, Vector3 direction) {
        super(ambient, diffuse, specular);
        this.direction = direction;
    }
}
