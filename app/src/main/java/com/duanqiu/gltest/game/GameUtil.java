package com.duanqiu.gltest.game;

/**
 * Created by duanjunjie on 17-7-27.
 */

public class GameUtil {
    public static boolean collid(GameObject o1, GameObject o2) {
        if (aInb(o1.position.x, o2.position.x, o2.position.x + o2.size.x)
                || aInb(o2.position.x, o1.position.x, o1.position.x + o1.size.x)) {
            if (aInb(o1.position.y, o2.position.y, o2.position.y + o2.size.y)
                    || aInb(o2.position.y, o1.position.y, o1.position.y + o1.size.y)) {
                return true;
            }
        }

        return false;
    }

    public static boolean aInb(float x, float x1, float x2) {
        return x >= x1 && x <= x2;
    }
}
