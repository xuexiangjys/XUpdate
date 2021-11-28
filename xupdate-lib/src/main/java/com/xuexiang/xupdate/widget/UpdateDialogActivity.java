package com.xuexiang.xupdate.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.DOWNLOAD_PERMISSION_DENIED;
import static com.xuexiang.xupdate.widget.UpdateDialogFragment.KEY_UPDATE_ENTITY;
import static com.xuexiang.xupdate.widget.UpdateDialogFragment.KEY_UPDATE_PROMPT_ENTITY;
import static com.xuexiang.xupdate.widget.UpdateDialogFragment.REQUEST_CODE_REQUEST_PERMISSIONS;

/**
 * 版本更新提示器【AppCompatActivity实现】
 *
 * @author xuexiang
 * @since 2020/6/8 10:47 PM
 */
public class UpdateDialogActivity extends AppCompatActivity implements View.OnClickListener, IDownloadEventHandler {

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
     * 显示更新提示
     *
     * @param updateEntity  更新信息
     * @param prompterProxy 更新代理
     * @param promptEntity  提示器参数信息
     */
    public static void show(@NonNull Context context, @NonNull UpdateEntity updateEntity, @NonNull IPrompterProxy prompterProxy, @NonNull PromptEntity promptEntity) {
        Intent intent = new Intent(context, UpdateDialogActivity.class);
        intent.putExtra(KEY_UPDATE_ENTITY, updateEntity);
        intent.putExtra(KEY_UPDATE_PROMPT_ENTITY, promptEntity);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        setIPrompterProxy(prompterProxy);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xupdate_layout_update_prompter);
        _XUpdate.setIsPrompterShow(getUrl(), true);
        initView();
        initData();
    }

    private void initView() {
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

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
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
            Bundle bundle = getIntent().getExtras();
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
     * @param updateEntity 更新信息
     */
    private void initUpdateInfo(UpdateEntity updateEntity) {
        // 弹出对话框
        final String newVersion = updateEntity.getVersionName();
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(this, updateEntity);
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
            themeColor = ColorUtils.getColor(this, R.color.xupdate_default_theme_color);
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
        Drawable topDrawable = _XUpdate.getTopDrawable(mPromptEntity.getTopDrawableTag());
        if (topDrawable != null) {
            mIvTop.setImageDrawable(topDrawable);
        } else {
            mIvTop.setImageResource(topResId);
        }
        DrawableUtils.setBackgroundCompat(mBtnUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, this), themeColor));
        DrawableUtils.setBackgroundCompat(mBtnBackgroundUpdate, DrawableUtils.getDrawable(UpdateUtils.dip2px(4, this), themeColor));
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
    public void onStart() {
        super.onStart();
        initWindowStyle();
    }

    private void initWindowStyle() {
        Window window = getWindow();
        if (window != null) {
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
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果是强制更新的话，就禁用返回键
        return keyCode == KeyEvent.KEYCODE_BACK && mUpdateEntity != null && mUpdateEntity.isForce();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        // 点击版本升级按钮【下载apk】
        if (i == R.id.btn_update) {
            // 权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!UpdateUtils.isPrivateApkCacheDir(mUpdateEntity) && flag != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_REQUEST_PERMISSIONS);
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
            UpdateUtils.saveIgnoreVersion(this, mUpdateEntity.getVersionName());
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
        if (!isFinishing()) {
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
        if (!isFinishing()) {
            if (mNumberProgressBar.getVisibility() == View.GONE) {
                doStart();
            }
            mNumberProgressBar.setProgress(Math.round(progress * 100));
            mNumberProgressBar.setMax(100);
        }
    }

    @Override
    public boolean handleCompleted(File file) {
        if (!isFinishing()) {
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
        if (!isFinishing()) {
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
        _XUpdate.startInstallApk(this, UpdateUtils.getApkFileByUpdateEntity(mUpdateEntity), mUpdateEntity.getDownLoadEntity());
    }

    /**
     * 弹窗消失
     */
    private void dismissDialog() {
        finish();
    }

    @Override
    protected void onStop() {
        if (isFinishing()) {
            _XUpdate.setIsPrompterShow(getUrl(), false);
            clearIPrompterProxy();
        }
        super.onStop();
    }

    private static void setIPrompterProxy(IPrompterProxy prompterProxy) {
        UpdateDialogActivity.sIPrompterProxy = prompterProxy;
    }

    private static void clearIPrompterProxy() {
        if (sIPrompterProxy != null) {
            sIPrompterProxy.recycle();
            sIPrompterProxy = null;
        }
    }

    private String getUrl() {
        return sIPrompterProxy != null ? sIPrompterProxy.getUrl() : "";
    }

}
