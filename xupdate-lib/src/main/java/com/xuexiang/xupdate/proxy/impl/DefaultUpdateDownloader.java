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

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateDownloader;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.service.DownloadService;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

/**
 * 默认版本更新下载器
 *
 * @author xuexiang
 * @since 2018/7/5 下午5:06
 */
public class DefaultUpdateDownloader implements IUpdateDownloader {

    private DownloadService.DownloadBinder mDownloadBinder;

    private void startDownload(DownloadService.DownloadBinder binder, @NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        mDownloadBinder = binder;
        mDownloadBinder.start(updateEntity, downloadListener);
    }

    @Override
    public void startDownload(final @NonNull UpdateEntity updateEntity, final @Nullable OnFileDownloadListener downloadListener) {
        DownloadService.bindService(new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                startDownload((DownloadService.DownloadBinder) service, updateEntity, downloadListener);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        });
    }

    @Override
    public void cancelDownload() {
        if (mDownloadBinder != null) {
            mDownloadBinder.stop("取消下载");
        }
    }
}
