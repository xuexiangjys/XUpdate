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
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 默认版本更新解析器【使用JSONObject进行解析】
 *
 * @author xuexiang
 * @since 2018/7/5 下午4:36
 */
public class DefaultUpdateParser extends AbstractUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = jsonObject.getInt(APIKey.CODE);
                if (code == 0) {
                    // 本地检查最新版本
                    int updateStatus = jsonObject.getInt(APIKey.UPDATE_STATUS);
                    int versionCode = jsonObject.getInt(APIKey.VERSION_CODE);
                    if (updateStatus != APIConstant.NO_NEW_VERSION) {
                        //最新版本小于等于现在的版本，不需要更新
                        if (versionCode <= UpdateUtils.getVersionCode(XUpdate.getContext())) {
                            updateStatus = APIConstant.NO_NEW_VERSION;
                        }
                    }
                    UpdateEntity updateEntity = new UpdateEntity();
                    if (updateStatus == APIConstant.NO_NEW_VERSION) {
                        updateEntity.setHasUpdate(false);
                    } else {
                        if (updateStatus == APIConstant.HAVE_NEW_VERSION_FORCED_UPLOAD) {
                            updateEntity.setForce(true);
                        }
                        updateEntity.setHasUpdate(true)
                                .setUpdateContent(jsonObject.getString(APIKey.MODIFY_CONTENT))
                                .setVersionCode(versionCode)
                                .setVersionName(jsonObject.getString(APIKey.VERSION_NAME))
                                .setDownloadUrl(jsonObject.getString(APIKey.DOWNLOAD_URL))
                                .setSize(jsonObject.getLong(APIKey.APK_SIZE))
                                .setMd5(jsonObject.getString(APIKey.APK_MD5));
                    }
                    return updateEntity;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 默认接口的API Key
     */
    public interface APIKey {
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
     * 默认接口的API常量
     * <p>
     * 0:无版本更新
     * 1:有版本更新，不需要强制升级
     * 2:有版本更新，需要强制升级
     */
    public interface APIConstant {
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
        int HAVE_NEW_VERSION_FORCED_UPLOAD = 2;
    }

}
