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

package com.xuexiang.xupdate.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xuexiang.xupdate.proxy.IUpdateHttpService;

/**
 * 版本更新信息实体
 *
 * @author xuexiang
 * @since 2018/6/29 下午9:33
 */
public class UpdateEntity implements Parcelable {
    //===========是否可以升级=============//
    /**
     * 是否有新版本
     */
    private boolean mHasUpdate;

    /**
     * 是否强制安装：不安装无法使用app
     */
    private boolean mIsForce;

    /**
     * 是否可忽略该版本
     */
    private boolean mIsIgnorable;

    //===========升级的信息=============//
    /**
     * 版本号
     */
    private int mVersionCode;
    /**
     * 版本名称
     */
    private String mVersionName;

    /**
     * 更新内容
     */
    private String mUpdateContent;

    /**
     * 下载信息实体
     */
    private DownloadEntity mDownloadEntity;

    //============升级行为============//
    /**
     * 是否静默下载：有新版本时不提示直接下载
     */
    private boolean mIsSilent;
    /**
     * 是否下载完成后自动安装[默认是true]
     */
    private boolean mIsAutoInstall;

    public UpdateEntity() {
        mVersionName = "unknown_version";
        mDownloadEntity = new DownloadEntity();
        mIsAutoInstall = true;
    }

    protected UpdateEntity(Parcel in) {
        mHasUpdate = in.readByte() != 0;
        mIsForce = in.readByte() != 0;
        mIsIgnorable = in.readByte() != 0;
        mVersionCode = in.readInt();
        mVersionName = in.readString();
        mUpdateContent = in.readString();
        mDownloadEntity = in.readParcelable(DownloadEntity.class.getClassLoader());
        mIsSilent = in.readByte() != 0;
        mIsAutoInstall = in.readByte() != 0;
    }

    public static final Creator<UpdateEntity> CREATOR = new Creator<UpdateEntity>() {
        @Override
        public UpdateEntity createFromParcel(Parcel in) {
            return new UpdateEntity(in);
        }

        @Override
        public UpdateEntity[] newArray(int size) {
            return new UpdateEntity[size];
        }
    };

    public boolean isHasUpdate() {
        return mHasUpdate;
    }

    public UpdateEntity setHasUpdate(boolean hasUpdate) {
        mHasUpdate = hasUpdate;
        return this;
    }

    public boolean isForce() {
        return mIsForce;
    }

    public UpdateEntity setForce(boolean force) {
        if (force) {
            //强制更新，不可以忽略
            mIsIgnorable = false;
        }
        mIsForce = force;
        return this;
    }

    public boolean isIgnorable() {
        return mIsIgnorable;
    }

    public UpdateEntity setIsIgnorable(boolean isIgnorable) {
        if (isIgnorable) {
            //可忽略的，不能是强制更新
            mIsForce = false;
        }
        mIsIgnorable = isIgnorable;
        return this;
    }

    public boolean isSilent() {
        return mIsSilent;
    }

    public UpdateEntity setIsSilent(boolean isSilent) {
        mIsSilent = isSilent;
        return this;
    }

    public boolean isAutoInstall() {
        return mIsAutoInstall;
    }

    public UpdateEntity setIsAutoInstall(boolean isAutoInstall) {
        mIsAutoInstall = isAutoInstall;
        return this;
    }

    /**
     * 设置apk的缓存地址，只支持设置一次
     *
     * @param apkCacheDir
     * @return
     */
    public UpdateEntity setApkCacheDir(String apkCacheDir) {
        if (!TextUtils.isEmpty(apkCacheDir) && TextUtils.isEmpty(mDownloadEntity.getCacheDir())) {
            mDownloadEntity.setCacheDir(apkCacheDir);
        }
        return this;
    }

    /**
     * 设置是否是自动模式【自动静默下载，自动安装】
     *
     * @param isAutoMode
     */
    public void setIsAutoMode(boolean isAutoMode) {
        if (isAutoMode) {
            //自动下载
            mIsSilent = true;
            //自动安装
            mIsAutoInstall = true;
            //自动模式下，默认下载进度条在通知栏显示
            mDownloadEntity.setShowNotification(true);
        }
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public UpdateEntity setVersionCode(int versionCode) {
        mVersionCode = versionCode;
        return this;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public UpdateEntity setVersionName(String versionName) {
        mVersionName = versionName;
        return this;
    }

    public String getUpdateContent() {
        return mUpdateContent;
    }

    public UpdateEntity setUpdateContent(String updateContent) {
        mUpdateContent = updateContent;
        return this;
    }

    public String getDownloadUrl() {
        return mDownloadEntity.getDownloadUrl();
    }

    public UpdateEntity setDownloadUrl(String downloadUrl) {
        mDownloadEntity.setDownloadUrl(downloadUrl);
        return this;
    }

    public String getMd5() {
        return mDownloadEntity.getMd5();
    }

    public UpdateEntity setMd5(String md5) {
        mDownloadEntity.setMd5(md5);
        return this;
    }

    public long getSize() {
        return mDownloadEntity.getSize();
    }

    public UpdateEntity setSize(long size) {
        mDownloadEntity.setSize(size);
        return this;
    }

    public String getApkCacheDir() {
        return mDownloadEntity.getCacheDir();
    }

    public UpdateEntity setDownLoadEntity(@NonNull DownloadEntity downloadEntity) {
        mDownloadEntity = downloadEntity;
        return this;
    }

    @NonNull
    public DownloadEntity getDownLoadEntity() {
        return mDownloadEntity;
    }

    //======内部变量，请勿设置=====//
    private IUpdateHttpService mIUpdateHttpService;

    public UpdateEntity setIUpdateHttpService(IUpdateHttpService updateHttpService) {
        mIUpdateHttpService = updateHttpService;
        return this;
    }

    public IUpdateHttpService getIUpdateHttpService() {
        return mIUpdateHttpService;
    }

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "mHasUpdate=" + mHasUpdate +
                ", mIsForce=" + mIsForce +
                ", mIsIgnorable=" + mIsIgnorable +
                ", mVersionCode=" + mVersionCode +
                ", mVersionName='" + mVersionName + '\'' +
                ", mUpdateContent='" + mUpdateContent + '\'' +
                ", mDownloadEntity=" + mDownloadEntity +
                ", mIsSilent=" + mIsSilent +
                ", mIsAutoInstall=" + mIsAutoInstall +
                ", mIUpdateHttpService=" + mIUpdateHttpService +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mHasUpdate ? 1 : 0));
        dest.writeByte((byte) (mIsForce ? 1 : 0));
        dest.writeByte((byte) (mIsIgnorable ? 1 : 0));
        dest.writeInt(mVersionCode);
        dest.writeString(mVersionName);
        dest.writeString(mUpdateContent);
        dest.writeParcelable(mDownloadEntity, flags);
        dest.writeByte((byte) (mIsSilent ? 1 : 0));
        dest.writeByte((byte) (mIsAutoInstall ? 1 : 0));
    }
}
