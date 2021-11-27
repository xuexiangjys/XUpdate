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

package com.xuexiang.xupdate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 弹窗工具类
 *
 * @author xuexiang
 * @since 2021/11/16 1:19 PM
 */
public final class DialogUtils {

    private DialogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 显示窗口【同步窗口系统view的可见度, 解决全屏下显示窗口导致界面退出全屏的问题】
     *
     * @param activity      活动窗口
     * @param window        需要显示的窗口
     * @param iWindowShower 窗口显示接口
     * @return 是否执行成功
     */
    public static boolean showWindow(Activity activity, Window window, IWindowShower iWindowShower) {
        if (activity == null || window == null || iWindowShower == null) {
            return false;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        iWindowShower.show(window);
        syncSystemUiVisibility(activity, window);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        return true;
    }

    /**
     * 同步窗口的系统view的可见度【解决全屏下显示窗口导致界面退出全屏的问题】
     *
     * @param original 活动窗口
     * @param target   目标窗口
     * @return 是否执行成功
     */
    public static boolean syncSystemUiVisibility(Activity original, Window target) {
        if (original == null) {
            return false;
        }
        return syncSystemUiVisibility(original.getWindow(), target);
    }

    /**
     * 同步两个窗口的系统view的可见度【解决全屏下显示窗口导致界面退出全屏的问题】
     *
     * @param original 原始窗口
     * @param target   目标窗口
     * @return 是否执行成功
     */
    public static boolean syncSystemUiVisibility(Window original, Window target) {
        if (original == null || target == null) {
            return false;
        }
        target.getDecorView().setSystemUiVisibility(original.getDecorView().getSystemUiVisibility());
        return true;
    }

    /**
     * 窗口显示接口
     */
    public interface IWindowShower {
        /**
         * 显示窗口
         *
         * @param window 窗口
         */
        void show(Window window);
    }

    /**
     * 根据上下文获取Activity
     *
     * @param context 上下文
     * @return Activity
     */
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        }
        return null;
    }

    /**
     * 根据用户点击的坐标获取用户在窗口上触摸到的View，判断这个View是否是EditText来判断是否需要隐藏键盘
     *
     * @param window 窗口
     * @param event  用户点击事件
     * @return 是否需要隐藏键盘
     */
    public static boolean isShouldHideInput(Window window, MotionEvent event) {
        if (window == null || event == null) {
            return false;
        }
        if (!isSoftInputShow(window)) {
            return false;
        }
        if (!(window.getCurrentFocus() instanceof EditText)) {
            return false;
        }
        View decorView = window.getDecorView();
        if (decorView instanceof ViewGroup) {
            return findTouchEditText((ViewGroup) decorView, event) == null;
        }
        return false;
    }

    private static View findTouchEditText(ViewGroup viewGroup, MotionEvent event) {
        if (viewGroup == null) {
            return null;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child == null || !child.isShown()) {
                continue;
            }
            if (!isTouchView(child, event)) {
                continue;
            }
            if (child instanceof EditText) {
                return child;
            } else if (child instanceof ViewGroup) {
                return findTouchEditText((ViewGroup) child, event);
            }
        }
        return null;
    }

    /**
     * 判断view是否在触摸区域内
     *
     * @param view  view
     * @param event 点击事件
     * @return view是否在触摸区域内
     */
    private static boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.contains((int) event.getX(), (int) event.getY());
    }

    /**
     * 输入键盘是否在显示
     *
     * @param window 应用窗口
     */
    private static boolean isSoftInputShow(Window window) {
        if (window != null && window.getDecorView() instanceof ViewGroup) {
            return isSoftInputShow((ViewGroup) window.getDecorView());
        }
        return false;
    }

    /**
     * 输入键盘是否在显示
     *
     * @param rootView 根布局
     */
    private static boolean isSoftInputShow(ViewGroup rootView) {
        if (rootView == null) {
            return false;
        }
        int viewHeight = rootView.getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int space = viewHeight - rect.bottom - getNavigationBarHeight(rootView.getContext());
        return space > 0;
    }

    /**
     * 获取系统底部导航栏的高度
     *
     * @param context 上下文
     * @return 系统状态栏的高度
     */
    private static int getNavigationBarHeight(Context context) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(realDisplayMetrics);
        }
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        if (realHeight - displayHeight > 0) {
            return realHeight - displayHeight;
        }
        return Math.max(realWidth - displayWidth, 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static void hideSoftInput(final View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
