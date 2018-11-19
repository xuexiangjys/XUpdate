package com.xuexiang.xupdate.entity;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * 版本更新提示器参数信息
 *
 * @author xuexiang
 * @since 2018/11/19 上午9:44
 */
public class PromptEntity implements Serializable {

    /**
     * 主题颜色
     */
    @ColorInt
    private int mThemeColor = -1;
    /**
     * 顶部背景图片
     */
    @DrawableRes
    private int mTopResId = -1;
    /**
     * 是否支持后台更新
     */
    private boolean mSupportBackgroundUpdate = false;

    public int getThemeColor() {
        return mThemeColor;
    }

    public PromptEntity setThemeColor(int themeColor) {
        mThemeColor = themeColor;
        return this;
    }

    public int getTopResId() {
        return mTopResId;
    }

    public PromptEntity setTopResId(int topResId) {
        mTopResId = topResId;
        return this;
    }

    public boolean isSupportBackgroundUpdate() {
        return mSupportBackgroundUpdate;
    }

    public PromptEntity setSupportBackgroundUpdate(boolean supportBackgroundUpdate) {
        mSupportBackgroundUpdate = supportBackgroundUpdate;
        return this;
    }

    @Override
    public String toString() {
        return "PromptEntity{" +
                "mThemeColor=" + mThemeColor +
                ", mTopResId=" + mTopResId +
                ", mSupportBackgroundUpdate=" + mSupportBackgroundUpdate +
                '}';
    }
}
