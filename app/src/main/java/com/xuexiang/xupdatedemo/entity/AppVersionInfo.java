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

package com.xuexiang.xupdatedemo.entity;

/**
 * XUpdateService返回的api
 *
 * @author xuexiang
 * @since 2018/7/30 上午11:36
 */
public class AppVersionInfo {
    
    private Integer versionId;

    private Integer updateStatus;

    private Integer versionCode;

    private String versionName;

    private String uploadTime;

    private Integer apkSize;

    private String appKey;

    private String modifyContent;

    private String downloadUrl;

    private String apkMd5;

    /**
     * @return version_id
     */
    public Integer getVersionId() {
        return versionId;
    }

    /**
     * @param versionId
     */
    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    /**
     * @return update_status
     */
    public Integer getUpdateStatus() {
        return updateStatus;
    }

    /**
     * @param updateStatus
     */
    public AppVersionInfo setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
        return this;
    }

    /**
     * @return version_code
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return version_name
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return upload_time
     */
    public String getUploadTime() {
        return uploadTime;
    }

    /**
     * @param uploadTime
     */
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * @return apk_size
     */
    public Integer getApkSize() {
        return apkSize;
    }

    /**
     * @param apkSize
     */
    public void setApkSize(Integer apkSize) {
        this.apkSize = apkSize;
    }

    /**
     * @return app_key
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * @param appKey
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * @return modify_content
     */
    public String getModifyContent() {
        return modifyContent;
    }

    /**
     * @param modifyContent
     */
    public void setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
    }

    /**
     * @return download_url
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * @return apk_md5
     */
    public String getApkMd5() {
        return apkMd5;
    }

    /**
     * @param apkMd5
     */
    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }
}