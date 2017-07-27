package com.duanqiu.gltest.game;

import com.duanqiu.gltest.util.Vector2;
import com.duanqiu.gltest.util.Vector3;

/**
 * Created by duanjunjie on 17-7-27.
 */

public class Block extends GameObject {
    protected boolean isSolid;
    protected boolean destroyed;

    public Block(Vector2 pos, Vector2 size, int sprite, Vector3 color, boolean isSolid) {
        super(pos, size, sprite, color, null);
        this.isSolid = isSolid;
        destroyed = false;
    }
}
