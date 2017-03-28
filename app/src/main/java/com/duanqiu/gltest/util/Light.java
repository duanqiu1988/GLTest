package com.duanqiu.gltest.util;

/**
 * Created by jjduan on 3/28/17.
 */

public class Light {
    public Vector3 ambient;
    public Vector3 diffuse;
    public Vector3 specular;

    public Light(Vector3 ambient, Vector3 diffuse, Vector3 specular) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }
}
