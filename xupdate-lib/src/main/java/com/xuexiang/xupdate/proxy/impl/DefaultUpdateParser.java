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

package com.xuexiang.xupdate.proxy.impl;

import android.text.TextUtils;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.logs.UpdateLog;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 默认版本更新解析器【使用JSONObject进行解析，减少第三方的依赖】
 *
 * @author xuexiang
 * @since 2018/7/5 下午4:36
 */
public class DefaultUpdateParser extends AbstractUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        if (!TextUtils.isEmpty(json)) {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(APIKeyUpper.CODE)) {
                // 首字母大写的Json
                return parseDefaultUpperFormatJson(jsonObject);
            } else {
                // 首字母小写的Json
                return parseDefaultLowerFormatJson(jsonObject);
            }
        }
        return null;
    }

    /**
     * 解析默认接口字段为首字母大写的Json
     */
    private UpdateEntity parseDefaultUpperFormatJson(JSONObject jsonObject) throws JSONException {
        int code = jsonObject.getInt(APIKeyUpper.CODE);
        if (code == APIConstant.REQUEST_SUCCESS) {
            // 本地检查最新版本
            int updateStatus = jsonObject.getInt(APIKeyUpper.UPDATE_STATUS);
            int versionCode = jsonObject.getInt(APIKeyUpper.VERSION_CODE);
            if (updateStatus != APIConstant.NO_NEW_VERSION) {
                updateStatus = checkCurrentVersionCode(updateStatus, versionCode);
            }
            UpdateEntity updateEntity = new UpdateEntity();
            if (updateStatus == APIConstant.NO_NEW_VERSION) {
                updateEntity.setHasUpdate(false);
            } else {
                if (updateStatus == APIConstant.HAVE_NEW_VERSION_FORCED_UPDATE) {
                    updateEntity.setForce(true);
                } else if (updateStatus == APIConstant.HAVE_NEW_VERSION_IGNORE_UPDATE) {
                    updateEntity.setIsIgnorable(true);
                }
                updateEntity.setHasUpdate(true)
                        .setUpdateContent(jsonObject.getString(APIKeyUpper.MODIFY_CONTENT))
                        .setVersionCode(versionCode)
                        .setVersionName(jsonObject.getString(APIKeyUpper.VERSION_NAME))
                        .setDownloadUrl(jsonObject.getString(APIKeyUpper.DOWNLOAD_URL))
                        .setSize(jsonObject.getLong(APIKeyUpper.APK_SIZE))
                        .setMd5(jsonObject.getString(APIKeyUpper.APK_MD5));
            }
            return updateEntity;
        }
        return null;
    }


    /**
     * 解析默认接口字段为首字母小写的Json
     */
    private UpdateEntity parseDefaultLowerFormatJson(JSONObject jsonObject) throws JSONException {
        int code = jsonObject.getInt(APIKeyLower.CODE);
        if (code == APIConstant.REQUEST_SUCCESS) {
            // 本地检查最新版本
            int updateStatus = jsonObject.getInt(APIKeyLower.UPDATE_STATUS);
            int versionCode = jsonObject.getInt(APIKeyLower.VERSION_CODE);
            if (updateStatus != APIConstant.NO_NEW_VERSION) {
                updateStatus = checkCurrentVersionCode(updateStatus, versionCode);
            }
            UpdateEntity updateEntity = new UpdateEntity();
            if (updateStatus == APIConstant.NO_NEW_VERSION) {
                updateEntity.setHasUpdate(false);
            } else {
                if (updateStatus == APIConstant.HAVE_NEW_VERSION_FORCED_UPDATE) {
                    updateEntity.setForce(true);
                } else if (updateStatus == APIConstant.HAVE_NEW_VERSION_IGNORE_UPDATE) {
                    updateEntity.setIsIgnorable(true);
                }
                updateEntity.setHasUpdate(true)
                        .setUpdateContent(jsonObject.getString(APIKeyLower.MODIFY_CONTENT))
                        .setVersionCode(versionCode)
                        .setVersionName(jsonObject.getString(APIKeyLower.VERSION_NAME))
                        .setDownloadUrl(jsonObject.getString(APIKeyLower.DOWNLOAD_URL))
                        .setSize(jsonObject.getLong(APIKeyLower.APK_SIZE))
                        .setMd5(jsonObject.getString(APIKeyLower.APK_MD5));
            }
            return updateEntity;
        }
        return null;
    }

    /**
     * 本地校验版本。当最新版本小于等于应用当前的版本时，不需要更新。
     *
     * @param updateStatus 更新状态
     * @param versionCode  云端获取的版本号
     * @return 版本更新的状态
     */
    protected int checkCurrentVersionCode(int updateStatus, int versionCode) {
        int currentVersionCode = UpdateUtils.getVersionCode(XUpdate.getContext());
        if (versionCode <= currentVersionCode) {
            UpdateLog.i("云端获取的最新版本小于等于应用当前的版本，不需要更新！当前版本:" + currentVersionCode + ", 云端版本:" + versionCode);
            updateStatus = APIConstant.NO_NEW_VERSION;
        }
        return updateStatus;
    }

    /**
     * 默认接口的API Key【所有接口字段首字母大写】
     */
    public interface APIKeyUpper {
        /**
         * 请求返回码
         */
        String CODE = "Code";
        /**
         * 更新的状态
         */
        String UPDATE_STATUS = "UpdateStatus";
        /**
         * 最新版本号[根据版本号来判别是否需要升级]
         */
        String VERSION_CODE = "VersionCode";
        /**
         * APP变更的内容
         */
        String MODIFY_CONTENT = "ModifyContent";
        /**
         * 最新APP版本的名称[用于展示的版本名]
         */
        String VERSION_NAME = "VersionName";
        /**
         * 下载地址
         */
        String DOWNLOAD_URL = "DownloadUrl";
        /**
         * Apk大小【单位：KB】
         */
        String APK_SIZE = "ApkSize";
        /**
         * Apk MD5值
         */
        String APK_MD5 = "ApkMd5";
    }


    /**
     * 默认接口的API Key【所有接口字段首字母小写】
     */
    public interface APIKeyLower {
        /**
         * 请求返回码
         */
        String CODE = "code";
        /**
         * 更新的状态
         */
        String UPDATE_STATUS = "updateStatus";
        /**
         * 最新版本号[根据版本号来判别是否需要升级]
         */
        String VERSION_CODE = "versionCode";
        /**
         * APP变更的内容
         */
        String MODIFY_CONTENT = "modifyContent";
        /**
         * 最新APP版本的名称[用于展示的版本名]
         */
        String VERSION_NAME = "versionName";
        /**
         * 下载地址
         */
        String DOWNLOAD_URL = "downloadUrl";
        /**
         * Apk大小【单位：KB】
         */
        String APK_SIZE = "apkSize";
        /**
         * Apk MD5值
         */
        String APK_MD5 = "apkMd5";
    }

    /**
     * 默认接口的API常量
     * <p>
     * 0:无版本更新
     * 1:有版本更新，不需要强制升级
     * 2:有版本更新，需要强制升级
     */
    public interface APIConstant {
        /**
         * 请求成功的code码
         */
        int REQUEST_SUCCESS = 0;
        /**
         * 无版本更新
         */
        int NO_NEW_VERSION = 0;
        /**
         * 有版本更新，不需要强制升级
         */
        int HAVE_NEW_VERSION = 1;
        /**
         * 有版本更新，需要强制升级
         */
        int HAVE_NEW_VERSION_FORCED_UPDATE = 2;
        /**
         * 有版本更新, 可忽略的版本升级
         */
        int HAVE_NEW_VERSION_IGNORE_UPDATE = 3;
    }

}
