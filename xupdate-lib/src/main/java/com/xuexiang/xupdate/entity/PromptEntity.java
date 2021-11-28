package com.xuexiang.xupdate.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * 版本更新提示器参数信息
 *
 * @author xuexiang
 * @since 2018/11/19 上午9:44
 */
public class PromptEntity implements Parcelable {

    /**
     * 主题颜色
     */
    @ColorInt
    private int mThemeColor;
    /**
     * 顶部背景图片
     */
    @DrawableRes
    private int mTopResId;
    /**
     * 顶部背景图片Drawable标识
     */
    private String mTopDrawableTag;
    /**
     * 按钮文字颜色
     */
    @ColorInt
    private int mButtonTextColor;
    /**
     * 是否支持后台更新
     */
    private boolean mSupportBackgroundUpdate;
    /**
     * 版本更新提示器宽度占屏幕的比例
     */
    private float mWidthRatio;
    /**
     * 版本更新提示器高度占屏幕的比例
     */
    private float mHeightRatio;
    /**
     * 是否忽略下载异常【为true时，下载失败更新提示框不消失，默认是false】
     */
    private boolean mIgnoreDownloadError;

    public PromptEntity() {
        mThemeColor = -1;
        mTopResId = -1;
        mTopDrawableTag = "";
        mButtonTextColor = 0;
        mSupportBackgroundUpdate = false;
        mWidthRatio = -1;
        mHeightRatio = -1;
        mIgnoreDownloadError = false;
    }

    protected PromptEntity(Parcel in) {
        mThemeColor = in.readInt();
        mTopResId = in.readInt();
        mTopDrawableTag = in.readString();
        mButtonTextColor = in.readInt();
        mSupportBackgroundUpdate = in.readByte() != 0;
        mWidthRatio = in.readFloat();
        mHeightRatio = in.readFloat();
        mIgnoreDownloadError = in.readByte() != 0;
    }

    public static final Creator<PromptEntity> CREATOR = new Creator<PromptEntity>() {
        @Override
        public PromptEntity createFromParcel(Parcel in) {
            return new PromptEntity(in);
        }

        @Override
        public PromptEntity[] newArray(int size) {
            return new PromptEntity[size];
        }
    };

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

    public String getTopDrawableTag() {
        return mTopDrawableTag;
    }

    public PromptEntity setTopDrawableTag(String topDrawableTag) {
        mTopDrawableTag = topDrawableTag;
        return this;
    }

    public int getButtonTextColor() {
        return mButtonTextColor;
    }

    public PromptEntity setButtonTextColor(int buttonTextColor) {
        mButtonTextColor = buttonTextColor;
        return this;
    }

    public boolean isSupportBackgroundUpdate() {
        return mSupportBackgroundUpdate;
    }

    public PromptEntity setSupportBackgroundUpdate(boolean supportBackgroundUpdate) {
        mSupportBackgroundUpdate = supportBackgroundUpdate;
        return this;
    }

    public PromptEntity setWidthRatio(float widthRatio) {
        mWidthRatio = widthRatio;
        return this;
    }

    public float getWidthRatio() {
        return mWidthRatio;
    }

    public PromptEntity setHeightRatio(float heightRatio) {
        mHeightRatio = heightRatio;
        return this;
    }

    public float getHeightRatio() {
        return mHeightRatio;
    }

    public PromptEntity setIgnoreDownloadError(boolean ignoreDownloadError) {
        mIgnoreDownloadError = ignoreDownloadError;
        return this;
    }

    public boolean isIgnoreDownloadError() {
        return mIgnoreDownloadError;
    }

    @Override
    public String toString() {
        return "PromptEntity{" +
                "mThemeColor=" + mThemeColor +
                ", mTopResId=" + mTopResId +
                ", mTopDrawableTag=" + mTopDrawableTag +
                ", mButtonTextColor=" + mButtonTextColor +
                ", mSupportBackgroundUpdate=" + mSupportBackgroundUpdate +
                ", mWidthRatio=" + mWidthRatio +
                ", mHeightRatio=" + mHeightRatio +
                ", mIgnoreDownloadError=" + mIgnoreDownloadError +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mThemeColor);
        dest.writeInt(mTopResId);
        dest.writeString(mTopDrawableTag);
        dest.writeInt(mButtonTextColor);
        dest.writeByte((byte) (mSupportBackgroundUpdate ? 1 : 0));
        dest.writeFloat(mWidthRatio);
        dest.writeFloat(mHeightRatio);
        dest.writeByte((byte) (mIgnoreDownloadError ? 1 : 0));
    }
}
