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

package com.xuexiang.xupdate.listener.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.utils.ApkInstallUtils;
import com.xuexiang.xupdate.utils.Md5Utils;

import java.io.File;
import java.io.IOException;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * 默认的apk安装监听
 *
 * @author xuexiang
 * @since 2018/7/1 下午11:58
 */
public class DefaultInstallListener implements OnInstallListener {

    @Override
    public boolean onInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        try {
            return downloadEntity.isApkFileValid(apkFile) && ApkInstallUtils.install(context, apkFile);
        } catch (IOException e) {
            _XUpdate.onUpdateError(INSTALL_FAILED, "获取apk的路径出错！");
        }
        return false;
    }
}
