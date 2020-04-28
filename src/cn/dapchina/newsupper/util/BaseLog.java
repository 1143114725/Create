package cn.dapchina.newsupper.util;

import android.util.Log;

/**
 * Created by EraJieZhang
 * on 16/8/30.
 * 日志管理类
 */

public class BaseLog {
    /**
     * 如不指定tag,默认tag为EraJie
     */
    public static final String LOG_TAG = "EraJieZhang";
    public static boolean DEBUG = true;

    public BaseLog() {
    }

    public static final void d(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void d(String tag, String log) {
        if (DEBUG)
            Log.d(tag, log);
    }


    public static final void e(String log) {
        if (DEBUG)
            Log.e(LOG_TAG,  log);
    }

    public static final void e(String tag, String log) {
        if (DEBUG)
            Log.e(tag, log);
    }

    public static final void i(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void i(String tag, String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void v(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void v(String tag, String log) {
        if (DEBUG)
            Log.v(tag, log);
    }

    public static final void w(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }

    public static final void w(String tag, String log) {
        if (DEBUG)
            Log.w(tag, log);
    }
}
