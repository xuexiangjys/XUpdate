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

package com.xuexiang.xupdate.proxy.impl;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.utils.ColorUtils;
import com.xuexiang.xupdate.utils.DrawableUtils;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xupdate.widget.NumberProgressBar;

import java.io.File;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.PROMPT_SHOW;

/**
 * 版本更新提示器【DialogFragment实现】
 *
 * @author xuexiang
 * @since 2018/7/2 上午11:40
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public final static String KEY_UPDATE_ENTITY = "key_update_entity";
    public final static String KEY_UPDATE_THEME_COLOR = "key_update_theme_color";
    public final static String KEY_UPDATE_TOP_PICTURE = "key_update_top_picture";
    /**
     * 标志当前更新提示是否已显示
     */
    public static boolean sIsShow = false;
    //======顶部========//
    /**
     * 顶部图片
     */
    private ImageView mIvTop;
    /**
     * 标题
     */
    private TextView mTvTitle;
    //======更新内容========//
    /**
     * 版本更新内容
     */
    private TextView mTvUpdateInfo;
    /**
     * 版本更新
     */
    private Button mBtnUpdate;
    /**
     * 忽略版本
     */
    private TextView mTvIgnore;
    /**
     * 进度条
     */
    private NumberProgressBar mNumberProgressBar;
    //======底部========//
    /**
     * 底部关闭
     */
    private LinearLayout mLlClose;
    private ImageView mIvClose;

    //======更新信息========//
    /**
     * 更新信息
     */
    private UpdateEntity mUpdateEntity;
    /**
     * 更新代理
     */
    private IUpdateProxy mIUpdateProxy;

    /**
     * 获取更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param themeColor   主题颜色
     * @param topResId     title背景图片
     * @return
     */
    public static UpdateDialogFragment newInstance(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @ColorInt int themeColor, @DrawableRes int topResId) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_UPDATE_ENTITY, updateEntity);
        if (themeColor != 0) {
            args.putInt(KEY_UPDATE_THEME_COLOR, themeColor);
        }
        if (topResId != 0) {
            args.putInt(KEY_UPDATE_TOP_PICTURE, topResId);
        }
        fragment.setArguments(args);
        fragment.setIUpdateProxy(updateProxy);
        return fragment;
    }

    /**
     * 设置更新代理
     *
     * @param updateProxy
     * @return
     */
    public UpdateDialogFragment setIUpdateProxy(IUpdateProxy updateProxy) {
        mIUpdateProxy = updateProxy;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sIsShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XUpdate_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    private void initDialog() {
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //如果是强制更新的话，就禁用返回键
                return keyCode == KeyEvent.KEYCODE_BACK && mUpdateEntity != null && mUpdateEntity.isForce();
            }
        });

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            lp.height = (int) (displayMetrics.heightPixels * 0.8f);
            window.setAttributes(lp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xupdate_dialog_app, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //顶部图片
        mIvTop = view.findViewById(R.id.iv_top);
        //标题
        mTvTitle = view.findViewById(R.id.tv_title);
        //提示内容
        mTvUpdateInfo = view.findViewById(R.id.tv_update_info);
        //更新按钮
        mBtnUpdate = view.findViewById(R.id.btn_update);
        //忽略
        mTvIgnore = view.findViewById(R.id.tv_ignore);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb_progress);

        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            final int themeColor = bundle.getInt(KEY_UPDATE_THEME_COLOR, -1);
            final int topResId = bundle.getInt(KEY_UPDATE_TOP_PICTURE, -1);
            //设置主题色
            initTheme(themeColor, topResId);
            mUpdateEntity = (UpdateEntity) bundle.getSerializable(KEY_UPDATE_ENTITY);
            if (mUpdateEntity != null) {
                //弹出对话框
                final String newVersion = mUpdateEntity.getVersionName();
                String targetSize = Formatter.formatShortFileSize(getContext(), mUpdateEntity.getSize() * 1024);
                final String updateContent = mUpdateEntity.getUpdateContent();

                String updateInfo = "";
                if (!TextUtils.isEmpty(targetSize)) {
                    updateInfo = "新版本大小：" + targetSize + "\n\n";
                }
                if (!TextUtils.isEmpty(updateContent)) {
                    updateInfo += updateContent;
                }

                //更新内容
                mTvUpdateInfo.setText(updateInfo);
                mTvTitle.setText(String.format("是否升级到%s版本？", newVersion));

                //强制更新,不显示关闭按钮
                if (mUpdateEntity.isForce()) {
                    mLlClose.setVisibility(View.GONE);
                } else {
                    //不是强制更新时，才生效
                    if (mUpdateEntity.isIgnorable()) {
                        mTvIgnore.setVisibility(View.VISIBLE);
                    }
                }
                initListeners();
            }
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme(@ColorInt int themeColor, @DrawableRes int topResId) {
        if (themeColor == -1) {
            themeColor = ColorUtils.getColor(getContext(), R.color.xupdate_default_theme_color);
        }
        if (topResId == -1) {
            topResId = R.drawable.xupdate_bg_app_top;
        }
        setDialogTheme(themeColor, topResId);
    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mIvTop.setImageResource(topResId);
        mBtnUpdate.setBackgroundDrawable(DrawableUtils.getDrawable(UpdateUtils.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mBtnUpdate.setTextColor(ColorUtils.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initListeners() {
        mBtnUpdate.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvIgnore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_update) { //点击版本升级按钮【下载apk】

        } else if (i == R.id.iv_close) { //点击关闭按钮

        } else if (i == R.id.tv_ignore) { //点击忽略按钮
            UpdateUtils.saveIgnoreVersion(getActivity(), mUpdateEntity.getVersionName());
            dismiss();
        }
    }

    /**
     * 显示安装的按钮
     *
     * @param apkFile
     */
    private void showInstallButton(final File apkFile) {
        mNumberProgressBar.setVisibility(View.GONE);
        mBtnUpdate.setText("安装");
        mBtnUpdate.setVisibility(View.VISIBLE);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUpdate.onInstallApk(getActivity(), apkFile, mUpdateEntity);
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            XUpdate.onUpdateError(PROMPT_SHOW, e.getMessage());
        }
    }

    /**
     * 显示更新提示
     *
     * @param manager
     */
    public void show(FragmentManager manager) {
        show(manager, "update_dialog");
    }

    @Override
    public void onDestroyView() {
        sIsShow = false;
        super.onDestroyView();
    }
}

