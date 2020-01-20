package com.xuexiang.xupdate.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;

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

    public PromptEntity() {
        mThemeColor = -1;
        mTopResId = -1;
        mSupportBackgroundUpdate = false;
        mWidthRatio = -1;
        mHeightRatio = -1;
    }

    protected PromptEntity(Parcel in) {
        mThemeColor = in.readInt();
        mTopResId = in.readInt();
        mSupportBackgroundUpdate = in.readByte() != 0;
        mWidthRatio = in.readFloat();
        mHeightRatio = in.readFloat();
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

    @Override
    public String toString() {
        return "PromptEntity{" +
                "mThemeColor=" + mThemeColor +
                ", mTopResId=" + mTopResId +
                ", mSupportBackgroundUpdate=" + mSupportBackgroundUpdate +
                ", mWidthRatio=" + mWidthRatio +
                ", mHeightRatio=" + mHeightRatio +
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
        dest.writeByte((byte) (mSupportBackgroundUpdate ? 1 : 0));
        dest.writeFloat(mWidthRatio);
        dest.writeFloat(mHeightRatio);
    }
}
