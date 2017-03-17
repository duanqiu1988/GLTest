package com.duanqiu.gltest.util;

import android.util.Log;

/**
 * Created by jjduan on 3/17/17.
 */

public class LogUtil {
    public static final String PREFIX = "junjie_";

    public static void v(String tag, String msg) {
        Log.v(PREFIX + tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(PREFIX + tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(PREFIX + tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(PREFIX + tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(PREFIX + tag, msg);
    }
}
