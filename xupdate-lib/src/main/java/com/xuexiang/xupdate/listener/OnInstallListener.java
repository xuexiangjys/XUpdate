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

package com.xuexiang.xupdate.listener;

import android.content.Context;

import androidx.annotation.NonNull;

import com.xuexiang.xupdate.entity.DownloadEntity;

import java.io.File;

/**
 * 安装监听
 *
 * @author xuexiang
 * @since 2018/6/29 下午4:14
 */
public interface OnInstallListener {

    /**
     * 开始安装apk的监听
     *
     * @param apkFile        安装的apk文件
     * @param downloadEntity 文件下载信息
     */
    boolean onInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity);

    /**
     * apk安装完毕
     */
    void onInstallApkSuccess();
}
