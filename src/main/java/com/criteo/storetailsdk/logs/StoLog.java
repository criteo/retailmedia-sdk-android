package com.criteo.storetailsdk.logs;

import android.util.Log;

/**
 * Created by mikhailpogorelov on 31/08/2017.
 */

public class StoLog {

    private static boolean isDebug = false;

    /**
     * Debug log
     *
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    /**
     * Information log
     *
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    /**
     * Warning log
     *
     * @param tag
     * @param message
     */
    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    /**
     * Logs even in release version for tracing errors
     *
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        Log.e(tag, message);
    }


    public static void enableLogs() {
        isDebug = true;
    }

    public static void disableLogs() {
        isDebug = false;
    }

}