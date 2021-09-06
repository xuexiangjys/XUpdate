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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.PromptEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IPrompterProxy;
import com.xuexiang.xupdate.utils.ColorUtils;
import com.xuexiang.xupdate.utils.DrawableUtils;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;

import static com.xuexiang.xupdate.widget.UpdateDialogFragment.REQUEST_CODE_REQUEST_PERMISSIONS;

/**
 * 版本更新弹窗
 *
 * @author xuexiang
 * @since 2018/7/24 上午9:29
 */
public class UpdateDialog extends BaseDialog implements View.OnClickListener, IDownloadEventHandler {

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
     * 后台更新
     */
    private Button mBtnBackgroundUpdate;
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
    private IPrompterProxy mIPrompterProxy;
    /**
     * 提示器参数信息
     */
    private PromptEntity mPromptEntity;

    /**
     * 获取更新提示
     *
     * @param updateEntity  更新信息
     * @param prompterProxy 更新代理
     * @param promptEntity  提示器参数信息
     * @return 更新提示
     */
    public static UpdateDialog newInstance(@NonNull Context context, @NonNull UpdateEntity updateEntity, @NonNull IPrompterProxy prompterProxy, PromptEntity promptEntity) {
        UpdateDialog dialog = new UpdateDialog(context);
        dialog.setIPrompterProxy(prompterProxy)
                .setUpdateEntity(updateEntity)
                .setPromptEntity(promptEntity);
        dialog.initTheme(promptEntity.getThemeColor(), promptEntity.getTopResId(), promptEntity.getButtonTextColor(), promptEntity.getWidthRatio(), promptEntity.getHeightRatio());
        return dialog;
    }

    private UpdateDialog(Context context) {
        super(context, R.layout.xupdate_dialog_update);
    }

    public UpdateDialog setPromptEntity(PromptEntity promptEntity) {
        mPromptEntity = promptEntity;
        return this;
    }

    @Override
    protected void initViews() {
        // 顶部图片
        mIvTop = findViewById(R.id.iv_top);
        // 标题
        mTvTitle = findViewById(R.id.tv_title);
        // 提示内容
        mTvUpdateInfo = findViewById(R.id.tv_update_info);
        // 更新按钮
        mBtnUpdate = findViewById(R.id.btn_update);
        // 后台更新按钮
        mBtnBackgroundUpdate = findViewById(R.id.btn_background_update);
        // 忽略
        mTvIgnore = findViewById(R.id.tv_ignore);
        // 进度条
        mNumberProgressBar = findViewById(R.id.npb_progress);

        // 关闭按钮+线 的整个布局
        mLlClose = findViewById(R.id.ll_close);
        // 关闭按钮
        mIvClose = findViewById(R.id.iv_close);
    }

    @Override
    protected void initListeners() {
        mBtnUpdate.setOnClickListener(this);
        mBtnBackgroundUpdate.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvIgnore.setOnClickListener(this);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    //====================生命周期============================//

    @Override
    public void show() {
        _XUpdate.setIsShowUpdatePrompter(true);
        super.show();
    }

    @Override
    public void dismiss() {
        _XUpdate.setIsShowUpdatePrompter(false);
        clearIPrompterProxy();
        super.dismiss();
    }

    private void clearIPrompterProxy() {
        if (mIPrompterProxy != null) {
            mIPrompterProxy.recycle();
            mIPrompterProxy = null;
        }
    }
    //====================UI构建============================//

    public UpdateDialog setUpdateEntity(UpdateEntity updateEntity) {
        mUpdateEntity = updateEntity;
        initUpdateInfo(mUpdateEntity);
        return this;
    }

    /**
     * 初始化更新信息
     *
     * @param updateEntity 版本更新信息
     */
    private void initUpdateInfo(UpdateEntity updateEntity) {
        // 弹出对话框
        final String newVersion = updateEntity.getVersionName();
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(getContext(), updateEntity);
        // 更新内容
        mTvUpdateInfo.setText(updateInfo);
        mTvTitle.setText(String.format(getString(R.string.xupdate_lab_ready_update), newVersion));

        // 刷新升级按钮显示
        refreshUpdateButton();

        // 强制更新,不显示关闭按钮
        if (updateEntity.isForce()) {
            mLlClose.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme(@ColorInt int themeColor, @DrawableRes int topResId, @ColorInt int buttonTextColor, float widthRatio, float heightRatio) {
        if (themeColor == -1) {
            themeColor = ColorUtils.getColor(getContext(), R.color.xupdate_default_theme_color);
        }
        if (topResId == -1) {
            topResId = R.drawable.xupdate_bg_app_top;
        }
        if (buttonTextColor == 0) {
            buttonTextColor = ColorUtils.isColorDark(themeColor) ? Color.WHITE : Color.BLACK;
        }
        setDialogTheme(themeColor, topResId, buttonTextColor, widthRatio, heightRatio);
    }

    /**
     * 设置⏏弹窗主题
     *
     * @param themeColor      主色
     * @param topResId        图片
     * @param buttonTextColor 按钮文字颜色
     * @param widthRatio      宽和屏幕的比例
     * @param heightRatio     高和屏幕的比例
     */
    private void setDialogTheme(int themeColor, int topResId, int buttonTextColor, float widthRatio, float heightRatio) {
        mIvTop.setImageResource(topResId);
        DrawableUtils.setBackgroundCompat(mBtnUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, getContext()), themeColor));
        DrawableUtils.setBackgroundCompat(mBtnBackgroundUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, getContext()), themeColor));
        mNumberProgressBar.setProgressTextColor(themeColor);
        mNumberProgressBar.setReachedBarColor(themeColor);
        mBtnUpdate.setTextColor(buttonTextColor);
        mBtnBackgroundUpdate.setTextColor(buttonTextColor);

        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        if (widthRatio > 0 && widthRatio < 1) {
            lp.width = (int) (displayMetrics.widthPixels * widthRatio);
        }
        if (heightRatio > 0 && heightRatio < 1) {
            lp.height = (int) (displayMetrics.heightPixels * heightRatio);
        }
        window.setAttributes(lp);
    }

    //====================更新功能============================//

    public UpdateDialog setIPrompterProxy(IPrompterProxy prompterProxy) {
        mIPrompterProxy = prompterProxy;
        return this;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        //点击版本升级按钮【下载apk】
        if (i == R.id.btn_update) {
            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!UpdateUtils.isPrivateApkCacheDir(mUpdateEntity) && flag != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_REQUEST_PERMISSIONS);
            } else {
                installApp();
            }
        } else if (i == R.id.btn_background_update) {
            //点击后台更新按钮
            mIPrompterProxy.backgroundDownload();
            dismiss();
        } else if (i == R.id.iv_close) {
            //点击关闭按钮
            mIPrompterProxy.cancelDownload();
            dismiss();
        } else if (i == R.id.tv_ignore) {
            //点击忽略按钮
            UpdateUtils.saveIgnoreVersion(getContext(), mUpdateEntity.getVersionName());
            dismiss();
        }
    }

    private void installApp() {
        if (UpdateUtils.isApkDownloaded(mUpdateEntity)) {
            onInstallApk();
            //安装完自杀
            //如果上次是强制更新，但是用户在下载完，强制杀掉后台，重新启动app后，则会走到这一步，所以要进行强制更新的判断。
            if (!mUpdateEntity.isForce()) {
                dismiss();
            } else {
                showInstallButton();
            }
        } else {
            if (mIPrompterProxy != null) {
                mIPrompterProxy.startDownload(mUpdateEntity, new WeakFileDownloadListener(this));
            }
            //忽略版本在点击更新按钮后隐藏
            if (mUpdateEntity.isIgnorable()) {
                mTvIgnore.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void handleStart() {
        if (isShowing()) {
            doStart();
        }
    }

    private void doStart() {
        mNumberProgressBar.setVisibility(View.VISIBLE);
        mNumberProgressBar.setProgress(0);
        mBtnUpdate.setVisibility(View.GONE);
        if (mPromptEntity.isSupportBackgroundUpdate()) {
            mBtnBackgroundUpdate.setVisibility(View.VISIBLE);
        } else {
            mBtnBackgroundUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    public void handleProgress(float progress) {
        if (isShowing()) {
            if (mNumberProgressBar.getVisibility() == View.GONE) {
                doStart();
            }
            mNumberProgressBar.setProgress(Math.round(progress * 100));
            mNumberProgressBar.setMax(100);
        }
    }

    @Override
    public boolean handleCompleted(File file) {
        if (isShowing()) {
            mBtnBackgroundUpdate.setVisibility(View.GONE);
            if (mUpdateEntity.isForce()) {
                showInstallButton();
            } else {
                dismiss();
            }
        }
        // 返回true，自动进行apk安装
        return true;
    }

    @Override
    public void handleError(Throwable throwable) {
        if (isShowing()) {
            if (mPromptEntity.isIgnoreDownloadError()) {
                refreshUpdateButton();
            } else {
                dismiss();
            }
        }
    }

    /**
     * 刷新升级按钮显示
     */
    private void refreshUpdateButton() {
        if (UpdateUtils.isApkDownloaded(mUpdateEntity)) {
            showInstallButton();
        } else {
            showUpdateButton();
        }
        mTvIgnore.setVisibility(mUpdateEntity.isIgnorable() ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示安装的按钮
     */
    private void showInstallButton() {
        mNumberProgressBar.setVisibility(View.GONE);
        mBtnBackgroundUpdate.setVisibility(View.GONE);
        mBtnUpdate.setText(R.string.xupdate_lab_install);
        mBtnUpdate.setVisibility(View.VISIBLE);
        mBtnUpdate.setOnClickListener(this);
    }

    /**
     * 显示升级的按钮
     */
    private void showUpdateButton() {
        mNumberProgressBar.setVisibility(View.GONE);
        mBtnBackgroundUpdate.setVisibility(View.GONE);
        mBtnUpdate.setText(R.string.xupdate_lab_update);
        mBtnUpdate.setVisibility(View.VISIBLE);
        mBtnUpdate.setOnClickListener(this);
    }

    private void onInstallApk() {
        _XUpdate.startInstallApk(getContext(), UpdateUtils.getApkFileByUpdateEntity(mUpdateEntity), mUpdateEntity.getDownLoadEntity());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        _XUpdate.setIsShowUpdatePrompter(true);
    }

    @Override
    public void onDetachedFromWindow() {
        _XUpdate.setIsShowUpdatePrompter(false);
        super.onDetachedFromWindow();
    }


}
