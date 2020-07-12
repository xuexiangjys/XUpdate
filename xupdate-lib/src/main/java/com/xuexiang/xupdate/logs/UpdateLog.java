/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xupdate.logs;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

/**
 * 版本更新日志打印
 *
 * @author xuexiang
 * @since 2018/6/29 下午7:58
 */
public final class UpdateLog {

    /**
     * Don't let anyone instantiate this class.
     */
    private UpdateLog() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    //==============常量================//
    /**
     * 默认tag
     */
    public final static String DEFAULT_LOG_TAG = "[XUpdate]";
    /**
     * 最大日志优先级【日志优先级为最大等级，所有日志都不打印】
     */
    private final static int MAX_LOG_PRIORITY = 10;
    /**
     * 最小日志优先级【日志优先级为最小等级，所有日志都打印】
     */
    private final static int MIN_LOG_PRIORITY = 0;

    //==============属性================//
    /**
     * 默认的日志记录为Logcat
     */
    private static ILogger sILogger = new LogcatLogger();

    private static String sTag = DEFAULT_LOG_TAG;
    /**
     * 是否是调试模式
     */
    private static boolean sIsDebug = false;
    /**
     * 日志打印优先级
     */
    private static int sLogPriority = MAX_LOG_PRIORITY;

    //==============属性设置================//

    /**
     * 设置日志记录者的接口
     *
     * @param logger
     */
    public static void setLogger(@NonNull ILogger logger) {
        sILogger = logger;
    }

    /**
     * 设置日志的tag
     *
     * @param tag
     */
    public static void setTag(String tag) {
        sTag = tag;
    }

    /**
     * 设置是否是调试模式
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority
     */
    public static void setPriority(int priority) {
        sLogPriority = priority;
    }

    //===================对外接口=======================//

    /**
     * 设置是否打开调试
     *
     * @param isDebug
     */
    public static void debug(boolean isDebug) {
        if (isDebug) {
            debug(DEFAULT_LOG_TAG);
        } else {
            debug("");
        }
    }

    /**
     * 设置调试模式
     *
     * @param tag
     */
    public static void debug(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            setDebug(true);
            setPriority(MIN_LOG_PRIORITY);
            setTag(tag);
        } else {
            setDebug(false);
            setPriority(MAX_LOG_PRIORITY);
            setTag("");
        }
    }

    //=============打印方法===============//

    /**
     * 打印任何（所有）信息
     *
     * @param msg
     */
    public static void v(String msg) {
        if (enableLog(Log.VERBOSE)) {
            sILogger.log(Log.VERBOSE, sTag, msg, null);
        }
    }

    /**
     * 打印任何（所有）信息
     *
     * @param tag
     * @param msg
     */
    public static void vTag(String tag, String msg) {
        if (enableLog(Log.VERBOSE)) {
            sILogger.log(Log.VERBOSE, tag, msg, null);
        }
    }

    /**
     * 打印调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (enableLog(Log.DEBUG)) {
            sILogger.log(Log.DEBUG, sTag, msg, null);
        }
    }

    /**
     * 打印调试信息
     *
     * @param tag
     * @param msg
     */
    public static void dTag(String tag, String msg) {
        if (enableLog(Log.DEBUG)) {
            sILogger.log(Log.DEBUG, tag, msg, null);
        }
    }

    /**
     * 打印提示性的信息
     *
     * @param msg
     */
    public static void i(String msg) {
        if (enableLog(Log.INFO)) {
            sILogger.log(Log.INFO, sTag, msg, null);
        }
    }

    /**
     * 打印提示性的信息
     *
     * @param tag
     * @param msg
     */
    public static void iTag(String tag, String msg) {
        if (enableLog(Log.INFO)) {
            sILogger.log(Log.INFO, tag, msg, null);
        }
    }

    /**
     * 打印warning警告信息
     *
     * @param msg
     */
    public static void w(String msg) {
        if (enableLog(Log.WARN)) {
            sILogger.log(Log.WARN, sTag, msg, null);
        }
    }

    /**
     * 打印warning警告信息
     *
     * @param tag
     * @param msg
     */
    public static void wTag(String tag, String msg) {
        if (enableLog(Log.WARN)) {
            sILogger.log(Log.WARN, tag, msg, null);
        }
    }

    /**
     * 打印出错信息
     *
     * @param msg
     */
    public static void e(String msg) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, msg, null);
        }
    }

    /**
     * 打印出错信息
     *
     * @param tag
     * @param msg
     */
    public static void eTag(String tag, String msg) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, msg, null);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param t
     */
    public static void e(Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, null, t);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag
     * @param t
     */
    public static void eTag(String tag, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, null, t);
        }
    }


    /**
     * 打印出错堆栈信息
     *
     * @param msg
     * @param t
     */
    public static void e(String msg, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, sTag, msg, t);
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag
     * @param msg
     * @param t
     */
    public static void eTag(String tag, String msg, Throwable t) {
        if (enableLog(Log.ERROR)) {
            sILogger.log(Log.ERROR, tag, msg, t);
        }
    }

    /**
     * 打印严重的错误信息
     *
     * @param msg
     */
    public static void wtf(String msg) {
        if (enableLog(Log.ASSERT)) {
            sILogger.log(Log.ASSERT, sTag, msg, null);
        }
    }

    /**
     * 打印严重的错误信息
     *
     * @param tag
     * @param msg
     */
    public static void wtfTag(String tag, String msg) {
        if (enableLog(Log.ASSERT)) {
            sILogger.log(Log.ASSERT, tag, msg, null);
        }
    }

    /**
     * 能否打印
     *
     * @param logPriority
     * @return
     */
    private static boolean enableLog(int logPriority) {
        return sILogger != null && sIsDebug && logPriority >= sLogPriority;
    }
}
