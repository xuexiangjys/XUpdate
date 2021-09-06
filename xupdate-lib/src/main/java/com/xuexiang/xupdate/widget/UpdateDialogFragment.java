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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.PromptEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IPrompterProxy;
import com.xuexiang.xupdate.utils.ColorUtils;
import com.xuexiang.xupdate.utils.DrawableUtils;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.DOWNLOAD_PERMISSION_DENIED;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.PROMPT_UNKNOWN;

/**
 * 版本更新提示器【DialogFragment实现】
 *
 * @author xuexiang
 * @since 2018/7/2 上午11:40
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener, IDownloadEventHandler {
    public final static String KEY_UPDATE_ENTITY = "key_update_entity";
    public final static String KEY_UPDATE_PROMPT_ENTITY = "key_update_prompt_entity";

    public final static int REQUEST_CODE_REQUEST_PERMISSIONS = 111;

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
    private static IPrompterProxy sIPrompterProxy;
    /**
     * 提示器参数信息
     */
    private PromptEntity mPromptEntity;
    /**
     * 当前屏幕方向
     */
    private int mCurrentOrientation;

    /**
     * 获取更新提示
     *
     * @param fragmentManager fragment管理者
     * @param updateEntity    更新信息
     * @param prompterProxy   更新代理
     * @param promptEntity    提示器参数信息
     */
    public static void show(@NonNull FragmentManager fragmentManager, @NonNull UpdateEntity updateEntity, @NonNull IPrompterProxy prompterProxy, @NonNull PromptEntity promptEntity) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_UPDATE_ENTITY, updateEntity);
        args.putParcelable(KEY_UPDATE_PROMPT_ENTITY, promptEntity);
        fragment.setArguments(args);
        setsIPrompterProxy(prompterProxy);
        fragment.show(fragmentManager);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _XUpdate.setIsShowUpdatePrompter(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.XUpdate_Fragment_Dialog);
        mCurrentOrientation = getResources().getConfiguration().orientation;

    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    private void initDialog() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // 如果是强制更新的话，就禁用返回键
                return keyCode == KeyEvent.KEYCODE_BACK && mUpdateEntity != null && mUpdateEntity.isForce();
            }
        });
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        PromptEntity promptEntity = getPromptEntity();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (promptEntity.getWidthRatio() > 0 && promptEntity.getWidthRatio() < 1) {
            lp.width = (int) (displayMetrics.widthPixels * promptEntity.getWidthRatio());
        }
        if (promptEntity.getHeightRatio() > 0 && promptEntity.getHeightRatio() < 1) {
            lp.height = (int) (displayMetrics.heightPixels * promptEntity.getHeightRatio());
        }
        window.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xupdate_layout_update_prompter, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        // 顶部图片
        mIvTop = view.findViewById(R.id.iv_top);
        // 标题
        mTvTitle = view.findViewById(R.id.tv_title);
        // 提示内容
        mTvUpdateInfo = view.findViewById(R.id.tv_update_info);
        // 更新按钮
        mBtnUpdate = view.findViewById(R.id.btn_update);
        // 后台更新按钮
        mBtnBackgroundUpdate = view.findViewById(R.id.btn_background_update);
        // 忽略
        mTvIgnore = view.findViewById(R.id.tv_ignore);
        // 进度条
        mNumberProgressBar = view.findViewById(R.id.npb_progress);

        // 关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        // 关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mPromptEntity = bundle.getParcelable(KEY_UPDATE_PROMPT_ENTITY);
        // 设置主题色
        if (mPromptEntity == null) {
            // 如果不存在就使用默认的
            mPromptEntity = new PromptEntity();
        }
        initTheme(mPromptEntity.getThemeColor(), mPromptEntity.getTopResId(), mPromptEntity.getButtonTextColor());
        mUpdateEntity = bundle.getParcelable(KEY_UPDATE_ENTITY);
        if (mUpdateEntity != null) {
            initUpdateInfo(mUpdateEntity);
            initListeners();
        }
    }

    /**
     * @return 版本更新提示器参数信息
     */
    private PromptEntity getPromptEntity() {
        // 先从bundle中去取
        if (mPromptEntity == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mPromptEntity = bundle.getParcelable(KEY_UPDATE_PROMPT_ENTITY);
            }
        }
        // 如果还不存在就使用默认的
        if (mPromptEntity == null) {
            mPromptEntity = new PromptEntity();
        }
        return mPromptEntity;
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
    private void initTheme(@ColorInt int themeColor, @DrawableRes int topResId, @ColorInt int buttonTextColor) {
        if (themeColor == -1) {
            themeColor = ColorUtils.getColor(getContext(), R.color.xupdate_default_theme_color);
        }
        if (topResId == -1) {
            topResId = R.drawable.xupdate_bg_app_top;
        }
        if (buttonTextColor == 0) {
            buttonTextColor = ColorUtils.isColorDark(themeColor) ? Color.WHITE : Color.BLACK;
        }
        setDialogTheme(themeColor, topResId, buttonTextColor);
    }

    /**
     * 设置
     *
     * @param themeColor 主题色
     * @param topResId   图片
     */
    private void setDialogTheme(int themeColor, int topResId, int buttonTextColor) {
        mIvTop.setImageResource(topResId);
        DrawableUtils.setBackgroundCompat(mBtnUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, getContext()), themeColor));
        DrawableUtils.setBackgroundCompat(mBtnBackgroundUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, getContext()), themeColor));
        mNumberProgressBar.setProgressTextColor(themeColor);
        mNumberProgressBar.setReachedBarColor(themeColor);
        mBtnUpdate.setTextColor(buttonTextColor);
        mBtnBackgroundUpdate.setTextColor(buttonTextColor);
    }

    private void initListeners() {
        mBtnUpdate.setOnClickListener(this);
        mBtnBackgroundUpdate.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvIgnore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        // 点击版本升级按钮【下载apk】
        if (i == R.id.btn_update) {
            // 权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!UpdateUtils.isPrivateApkCacheDir(mUpdateEntity) && flag != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_REQUEST_PERMISSIONS);
            } else {
                installApp();
            }
        } else if (i == R.id.btn_background_update) {
            // 点击后台更新按钮
            if (sIPrompterProxy != null) {
                sIPrompterProxy.backgroundDownload();
            }
            dismissDialog();
        } else if (i == R.id.iv_close) {
            // 点击关闭按钮
            if (sIPrompterProxy != null) {
                sIPrompterProxy.cancelDownload();
            }
            dismissDialog();
        } else if (i == R.id.tv_ignore) {
            // 点击忽略按钮
            UpdateUtils.saveIgnoreVersion(getActivity(), mUpdateEntity.getVersionName());
            dismissDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 升级
                installApp();
            } else {
                _XUpdate.onUpdateError(DOWNLOAD_PERMISSION_DENIED);
                dismissDialog();
            }
        }

    }

    private void installApp() {
        if (UpdateUtils.isApkDownloaded(mUpdateEntity)) {
            onInstallApk();
            // 安装完自杀
            // 如果上次是强制更新，但是用户在下载完，强制杀掉后台，重新启动app后，则会走到这一步，所以要进行强制更新的判断。
            if (!mUpdateEntity.isForce()) {
                dismissDialog();
            } else {
                showInstallButton();
            }
        } else {
            if (sIPrompterProxy != null) {
                sIPrompterProxy.startDownload(mUpdateEntity, new WeakFileDownloadListener(this));
            }
            // 忽略版本在点击更新按钮后隐藏
            if (mUpdateEntity.isIgnorable()) {
                mTvIgnore.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void handleStart() {
        if (!UpdateDialogFragment.this.isRemoving()) {
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
        if (!UpdateDialogFragment.this.isRemoving()) {
            if (mNumberProgressBar.getVisibility() == View.GONE) {
                doStart();
            }
            mNumberProgressBar.setProgress(Math.round(progress * 100));
            mNumberProgressBar.setMax(100);
        }
    }

    @Override
    public boolean handleCompleted(File file) {
        if (!UpdateDialogFragment.this.isRemoving()) {
            mBtnBackgroundUpdate.setVisibility(View.GONE);
            if (mUpdateEntity.isForce()) {
                showInstallButton();
            } else {
                dismissDialog();
            }
        }
        // 返回true，自动进行apk安装
        return true;
    }

    @Override
    public void handleError(Throwable throwable) {
        if (!UpdateDialogFragment.this.isRemoving()) {
            if (mPromptEntity.isIgnoreDownloadError()) {
                refreshUpdateButton();
            } else {
                dismissDialog();
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

    /**
     * 弹窗消失
     */
    private void dismissDialog() {
        clearIPrompterProxy();
        dismissAllowingStateLoss();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            _XUpdate.onUpdateError(PROMPT_UNKNOWN, e.getMessage());
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
        _XUpdate.setIsShowUpdatePrompter(false);
        super.onDestroyView();
    }

    private static void setsIPrompterProxy(IPrompterProxy sIPrompterProxy) {
        UpdateDialogFragment.sIPrompterProxy = sIPrompterProxy;
    }

    private static void clearIPrompterProxy() {
        if (sIPrompterProxy != null) {
            sIPrompterProxy.recycle();
            sIPrompterProxy = null;
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != mCurrentOrientation) {
            reloadView();
        }
        mCurrentOrientation = newConfig.orientation;
    }

    private void reloadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.xupdate_layout_update_prompter, null);
        ViewGroup root = (ViewGroup) getView();
        if (root != null) {
            root.removeAllViews();
            root.addView(view);
            initView(root);
            initData();
        }
    }

}

