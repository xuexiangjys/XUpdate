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

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 版本更新信息实体
 *
 * @author xuexiang
 * @since 2018/6/29 下午9:33
 */
public class UpdateEntity implements Serializable {
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
     * app下载地址
     */
    private String mDownloadUrl;
    /**
     * 下载文件的md5值，用于校验，防止下载的apk文件被替换
     */
    private String mMd5;
    /**
     * app大小【单位：KB】
     */
    private long mSize;

    //============升级行为============//
    /**
     * 是否静默下载：有新版本时不提示直接下载
     */
    private boolean mIsSilent;
    /**
     * 是否下载完成后自动安装
     */
    private boolean mIsAutoInstall;
    /**
     * apk下载的目录
     */
    private String mApkCacheDir;

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
            mIsIgnorable = false; //强制更新，不可以忽略
        }
        mIsForce = force;
        return this;
    }

    public boolean isIgnorable() {
        return mIsIgnorable;
    }

    public UpdateEntity setIsIgnorable(boolean isIgnorable) {
        if (isIgnorable) {
            mIsForce = false;  //可忽略的，不能是强制更新
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
        if (!TextUtils.isEmpty(apkCacheDir) && TextUtils.isEmpty(mApkCacheDir)) {
            mApkCacheDir = apkCacheDir;
        }
        return this;
    }

    /**
     * 设置是否是自动模式【自动静默下载，自动安装】
     * @param isAutoMode
     */
    public void setIsAutoMode(boolean isAutoMode) {
        if (isAutoMode) {
            mIsSilent = true;  //自动下载
            mIsAutoInstall = true; //自动安装
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
        return mDownloadUrl;
    }

    public UpdateEntity setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
        return this;
    }

    public String getMd5() {
        return mMd5;
    }

    public UpdateEntity setMd5(String md5) {
        mMd5 = md5;
        return this;
    }

    public long getSize() {
        return mSize;
    }

    public UpdateEntity setSize(long size) {
        mSize = size;
        return this;
    }

    public String getApkCacheDir() {
        return mApkCacheDir;
    }
}
