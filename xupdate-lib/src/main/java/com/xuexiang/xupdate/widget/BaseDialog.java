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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate.utils.DialogUtils;

/**
 * 基类Dialog
 * 触摸Dialog屏幕以外的区域，dialog消失同时隐藏键盘
 *
 * @author xuexiang
 * @since 2018/7/24 上午9:34
 */
public abstract class BaseDialog extends AppCompatDialog {

    private View mContentView;

    /**
     * 是否同步系统控制器显示状态，默认false【状态栏、三键导航栏等】
     */
    private boolean mIsSyncSystemUiVisibility;

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
     * @param width  宽
     * @param height 高
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

    /**
     * 设置是否同步系统控制器显示状态
     *
     * @param isSyncSystemUiVisibility 是否同步系统控制器显示状态
     * @return this
     */
    public BaseDialog setIsSyncSystemUiVisibility(boolean isSyncSystemUiVisibility) {
        mIsSyncSystemUiVisibility = isSyncSystemUiVisibility;
        return this;
    }

    /**
     * 显示加载
     */
    @Override
    public void show() {
        showIfSync(mIsSyncSystemUiVisibility);
    }

    /**
     * 显示弹窗，是否同步系统控制器显示状态
     *
     * @param isSyncSystemUiVisibility 是否同步系统控制器显示状态
     */
    public void showIfSync(boolean isSyncSystemUiVisibility) {
        if (isSyncSystemUiVisibility) {
            boolean isHandled = DialogUtils.showWindow(DialogUtils.findActivity(getContext()), getWindow(), new DialogUtils.IWindowShower() {
                @Override
                public void show(Window window) {
                    performShow();
                }
            });
            if (!isHandled) {
                performShow();
            }
        } else {
            performShow();
        }
    }

    /**
     * 真正执行显示的方法
     */
    protected void performShow() {
        super.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (DialogUtils.isShouldHideInput(getWindow(), ev)) {
                DialogUtils.hideSoftInput(getCurrentFocus());
            }
        }
        return super.onTouchEvent(ev);
    }


}
