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

package com.xuexiang.xupdate.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.xuexiang.xupdate.R;

/**
 * 基类Dialog
 * 触摸Dialog屏幕以外的区域，dialog消失同时隐藏键盘
 *
 * @author xuexiang
 * @since 2018/7/24 上午9:34
 */
public abstract class BaseDialog extends AppCompatDialog {
    private View mContentView;

    public BaseDialog(Context context, int layoutId) {
        this(context, R.style.XUpdate_Dialog, layoutId);
    }

    public BaseDialog(Context context, View contentView) {
        this(context, R.style.XUpdate_Dialog, contentView);
    }

    public BaseDialog(Context context) {
        super(context, R.style.XUpdate_Dialog);
    }

    public BaseDialog(Context context, int theme, int layoutId) {
        super(context, theme);
        init(layoutId);

    }

    public BaseDialog(Context context, int theme, View contentView) {
        super(context, theme);
        init(contentView);
    }

    private void init(int layoutId) {
        View view = getLayoutInflater().inflate(layoutId, null);
        init(view);
    }

    private void init(View view) {
        setContentView(view);
        mContentView = view;
        setCanceledOnTouchOutside(true);

        initViews();
        initListeners();
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        return mContentView.findViewById(id);
    }

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 初始化监听器
     */
    protected abstract void initListeners();

    /**
     * 设置弹窗的宽和高
     *
     * @param width
     * @param height
     */
    protected BaseDialog setDialogSize(int width, int height) {
        // 获取对话框当前的参数值
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams p = getWindow().getAttributes();
            p.width = width;
            p.height = height;
            window.setAttributes(p);
        }
        return this;
    }

    protected String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    protected Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideInput(getWindow(), ev)) {
                hideSoftInput(getCurrentFocus());
            }
        }
        return super.onTouchEvent(ev);
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
    public static boolean isSoftInputShow(Window window) {
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
    public static boolean isSoftInputShow(ViewGroup rootView) {
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
    public static int getNavigationBarHeight(Context context) {
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
    private static void hideSoftInput(final View view) {
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
