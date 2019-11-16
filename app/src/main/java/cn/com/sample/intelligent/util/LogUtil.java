package cn.com.sample.intelligent.util;

import android.util.Log;

/**
 * Description ：Log工具类
 * creator ：但子龙
 * Create time : 2018/1/25
 */

public class LogUtil {

    public static boolean VERBOSE = false;
    public static boolean DEBUG = true;
    public static boolean ERROR = true;


    public static void setLoggable(boolean debug) {
        DEBUG = debug;
    }

    public static boolean isLoggable() {
        return DEBUG;
    }

    /**
     * Send a {@link #VERBOSE} log message.
     */
    public static int v(String tag, String format, Object... args) {
        if (isLoggable()) {
            return Log.v(tag, String.format(format, args));
        }
        return 0;
    }

    /**
     * Send a {@link #VERBOSE} log message with Throwable.
     */
    public static int v(String tag, Throwable tr, String format, Object... args) {
        if (isLoggable()) {
            return Log.v(tag, String.format(format, args), tr);
        }
        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message.
     */
    public static int d(String tag, String format, Object... args) {
        if (isLoggable()) {
            return Log.d(tag, String.format(format, args));
        }
        return 0;
    }

    /**
     * Send a {@link #DEBUG} log message with Throwable.
     */
    public static int d(String tag, Throwable tr, String format, Object... args) {
        if (isLoggable()) {
            return Log.d(tag, String.format(format, args), tr);
        }
        return 0;
    }

    /**
     * Send a {@link #ERROR} log message.
     */
    public static int e(String tag, String format, Object... args) {
        if (isLoggable()) {
            return Log.e(tag, String.format(format, args));
        }
        return 0;
    }

    /**
     * Send a {@link #ERROR} log message with Throwable.
     */
    public static int e(String tag, Throwable tr, String format, Object... args) {
        if (isLoggable()) {
            return Log.e(tag, String.format(format, args), tr);
        }
        return 0;
    }
}
