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
    private boolean mHasUpdate = false;

    /**
     * 是否强制安装：不安装无法使用app
     */
    private boolean mIsForce = false;

    /**
     * 是否可忽略该版本
     */
    private boolean mIsIgnorable = true;

    //============升级行为============//

    /**
     * 是否静默下载：有新版本时不提示直接下载
     */
    private boolean mIsSilent = false;

    /**
     * 是否下载完成后自动安装
     */
    private boolean mIsAutoInstall = true;

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
     * app大小
     */
    private long mSize;

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
        mIsForce = force;
        return this;
    }

    public boolean ismIsIgnorable() {
        return mIsIgnorable;
    }

    public UpdateEntity setmIsIgnorable(boolean isIgnorable) {
        mIsIgnorable = isIgnorable;
        return this;
    }

    public boolean ismIsSilent() {
        return mIsSilent;
    }

    public UpdateEntity setmIsSilent(boolean isSilent) {
        mIsSilent = isSilent;
        return this;
    }

    public boolean ismIsAutoInstall() {
        return mIsAutoInstall;
    }

    public UpdateEntity setmIsAutoInstall(boolean isAutoInstall) {
        mIsAutoInstall = isAutoInstall;
        return this;
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
}
