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

package com.xuexiang.xupdatedemo.custom;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.impl.AbstractUpdateParser;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateParser;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xupdatedemo.entity.ApiResult;
import com.xuexiang.xupdatedemo.entity.AppVersionInfo;
import com.xuexiang.xupdatedemo.http.XHttpUpdateHttpService;
import com.xuexiang.xupdatedemo.utils.SettingSPUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.net.type.TypeBuilder;

import java.lang.reflect.Type;

/**
 * @author xuexiang
 * @since 2018/7/30 下午12:00
 */
public class XUpdateServiceParser extends AbstractUpdateParser {
    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        ApiResult<AppVersionInfo> apiResult = JsonUtil.fromJson(json, getApiResultType(AppVersionInfo.class));
        if (apiResult != null) {
            AppVersionInfo appVersionInfo = doLocalCompare(apiResult.getData());

            UpdateEntity updateEntity = new UpdateEntity();
            if (appVersionInfo.getUpdateStatus() == DefaultUpdateParser.APIConstant.NO_NEW_VERSION) {
                updateEntity.setHasUpdate(false);
            } else {
                if (appVersionInfo.getUpdateStatus() == DefaultUpdateParser.APIConstant.HAVE_NEW_VERSION_FORCED_UPDATE) {
                    updateEntity.setForce(true);
                }
                updateEntity.setHasUpdate(true)
                        //兼容一下
                        .setUpdateContent(appVersionInfo.getModifyContent().replaceAll("\\\\r\\\\n", "\r\n"))
                        .setVersionCode(appVersionInfo.getVersionCode())
                        .setVersionName(appVersionInfo.getVersionName())
                        .setDownloadUrl(getDownLoadUrl(appVersionInfo.getDownloadUrl()))
                        .setSize(appVersionInfo.getApkSize())
                        .setMd5(appVersionInfo.getApkMd5());
            }
            return updateEntity;
        }
        return null;
    }

    /**
     * 为请求的返回类型加上ApiResult包装类
     *
     * @param type
     * @return
     */
    public static Type getApiResultType(Type type) {
        return TypeBuilder
                .newInstance(ApiResult.class)
                .addTypeParam(type)
                .build();
    }

    /**
     * 进行本地版本判断[防止服务端出错，本来是不需要更新，但是服务端返回是需要更新]
     *
     * @param appVersionInfo
     * @return
     */
    private AppVersionInfo doLocalCompare(AppVersionInfo appVersionInfo) {
        //服务端返回需要更新
        if (appVersionInfo.getUpdateStatus() != DefaultUpdateParser.APIConstant.NO_NEW_VERSION) {
            int lastVersionCode = appVersionInfo.getVersionCode();
            //最新版本小于等于现在的版本，不需要更新
            if (lastVersionCode <= UpdateUtils.getVersionCode(XUpdate.getContext())) {
                appVersionInfo.setUpdateStatus(DefaultUpdateParser.APIConstant.NO_NEW_VERSION);
            }
        }
        return appVersionInfo;
    }

    public static String getVersionCheckUrl() {
        String baseUrl = SettingSPUtils.get().getServiceURL();
        if (baseUrl.endsWith("/")) {
            return baseUrl + "update/checkVersion";
        } else {
            return baseUrl + "/update/checkVersion";
        }
    }

    public static String getDownLoadUrl(String url) {
        String baseUrl = SettingSPUtils.get().getServiceURL();
        if (baseUrl.endsWith("/")) {
            return baseUrl + "update/apk/" + url;
        } else {
            return baseUrl + "/update/apk/" + url;
        }
    }

    public static IUpdateHttpService getUpdateHttpService() {
        return new XHttpUpdateHttpService(SettingSPUtils.get().getServiceURL());
    }
}
