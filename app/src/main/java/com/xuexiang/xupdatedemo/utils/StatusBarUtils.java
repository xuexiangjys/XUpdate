/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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
 *
 */

package com.xuexiang.xupdatedemo.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;

import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * 状态栏工具
 *
 * @author XUE
 * @since 2019/3/22 10:50
 */
public class StatusBarUtils {

    private StatusBarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    public static boolean isFullScreen(Activity activity) {
        boolean ret = false;
        try {
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            ret = (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }



    /**
     * 全屏
     *
     * @param activity 窗口
     */
    public static void fullScreen(Activity activity) {
        if (activity == null) {
            return;
        }
        fullScreen(activity.getWindow());
    }

    /**
     * 全屏
     *
     * @param window 窗口
     */
    public static void fullScreen(Window window) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT > HONEYCOMB && Build.VERSION.SDK_INT < KITKAT) { // lower api
            window.getDecorView().setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= KITKAT) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * 取消全屏
     *
     * @param activity           窗口
     * @param statusBarColor     状态栏的颜色
     * @param navigationBarColor 导航栏的颜色
     */
    public static void cancelFullScreen(Activity activity, @ColorInt int statusBarColor, @ColorInt int navigationBarColor) {
        if (activity == null) {
            return;
        }
        cancelFullScreen(activity.getWindow(), statusBarColor, navigationBarColor);
    }

    /**
     * 取消全屏
     *
     * @param window             窗口
     * @param statusBarColor     状态栏的颜色
     * @param navigationBarColor 导航栏的颜色
     */
    public static void cancelFullScreen(Window window, @ColorInt int statusBarColor, @ColorInt int navigationBarColor) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (statusBarColor != -1) {
                window.setStatusBarColor(statusBarColor);
            }
            if (navigationBarColor != -1) {
                window.setNavigationBarColor(navigationBarColor);
            }
        }
    }

    /**
     * 取消全屏
     *
     * @param activity 窗口
     */
    public static void cancelFullScreen(Activity activity) {
        if (activity == null) {
            return;
        }
        cancelFullScreen(activity.getWindow());
    }

    /**
     * 取消全屏
     *
     * @param window 窗口
     */
    public static void cancelFullScreen(Window window) {
        cancelFullScreen(window, -1, -1);
    }

}
