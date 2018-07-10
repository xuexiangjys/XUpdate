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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateChecker;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.util.Map;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_APK_CACHE_DIR_EMPTY;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_IGNORED_VERSION;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_JSON_EMPTY;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_PARSE;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_UNKNOWN;

/**
 * 默认版本更新检查者
 *
 * @author xuexiang
 * @since 2018/7/2 下午10:21
 */
public class DefaultUpdateChecker implements IUpdateChecker {

    @Override
    public void checkVersion(boolean isGet, @NonNull String url, @NonNull Map<String, Object> params, final @NonNull IUpdateProxy updateProxy) {
        if (isGet) {
            updateProxy.getIUpdateHttpService().asyncGet(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(updateProxy, error);
                }
            });
        } else {
            updateProxy.getIUpdateHttpService().asyncPost(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(updateProxy, error);
                }
            });
        }
    }

    /**
     * 查询成功
     *
     * @param result
     * @param updateProxy
     */
    private void onCheckSuccess(String result, @NonNull IUpdateProxy updateProxy) {
        updateProxy.onAfterCheck();
        if (!TextUtils.isEmpty(result)) {
            processCheckResult(result, updateProxy);
        } else {
            XUpdate.onUpdateError(CHECK_JSON_EMPTY);
        }
    }

    /**
     * 查询失败
     *
     * @param updateProxy
     * @param error
     */
    private void onCheckError(@NonNull IUpdateProxy updateProxy, Throwable error) {
        updateProxy.onAfterCheck();
        XUpdate.onUpdateError(CHECK_UNKNOWN, error.getMessage());
    }

    @Override
    public void processCheckResult(@NonNull String result, @NonNull IUpdateProxy updateProxy) {
        try {
            UpdateEntity updateEntity = updateProxy.parseJson(result);
            if (updateEntity != null) {
                if (updateEntity.isHasUpdate()) {
                    //校验是否是已忽略版本
                    if (UpdateUtils.isIgnoreVersion(updateProxy.getContext(), updateEntity.getVersionName())) {
                        XUpdate.onUpdateError(CHECK_IGNORED_VERSION);
                    //校验apk下载缓存目录是否为空
                    } else if (TextUtils.isEmpty(updateEntity.getApkCacheDir())) {
                        XUpdate.onUpdateError(CHECK_APK_CACHE_DIR_EMPTY);
                    } else {
                        updateProxy.findNewVersion(updateEntity, updateProxy);
                    }
                } else {
                    XUpdate.onUpdateError(CHECK_NO_NEW_VERSION);
                }
            } else {
                XUpdate.onUpdateError(CHECK_PARSE, "json:" + result);
            }
        } catch (Exception e) {
            XUpdate.onUpdateError(CHECK_PARSE, e.getMessage());
        }
    }

}
