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
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateDownloader;
import com.xuexiang.xupdate.service.DownloadService;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

/**
 * 默认版本更新下载器
 *
 * @author xuexiang
 * @since 2018/7/5 下午5:06
 */
public class DefaultUpdateDownloader implements IUpdateDownloader {

    private DownloadService.DownloadBinder mDownloadBinder;

    /**
     * 服务绑定连接
     */
    private ServiceConnection mServiceConnection;

    /**
     * 是否已绑定下载服务
     */
    private boolean mIsBound;

    @Override
    public void startDownload(final @NonNull UpdateEntity updateEntity, final @Nullable OnFileDownloadListener downloadListener) {
        if (isDownloadUrl(updateEntity)) {
            startDownloadService(updateEntity, downloadListener);
        } else {
            startOpenHtml(updateEntity, downloadListener);
        }
    }

    /**
     * 地址是否是下载地址，需要开启下载服务进行下载【可以根据自己的逻辑进行重写】
     *
     * @param updateEntity 版本更新信息
     * @return 地址是否是下载地址
     */
    protected boolean isDownloadUrl(@NonNull UpdateEntity updateEntity) {
        return isApkDownloadUrl(updateEntity) || !isStaticHtmlUrl(updateEntity);
    }

    /**
     * 地址是否是apk的下载地址
     *
     * @param updateEntity 版本更新信息
     * @return 地址是否是apk的下载地址
     */
    protected boolean isApkDownloadUrl(@NonNull UpdateEntity updateEntity) {
        String downloadUrl = updateEntity.getDownloadUrl();
        return !TextUtils.isEmpty(downloadUrl) && downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1).endsWith(".apk");
    }

    /**
     * 地址是否是静态网页
     *
     * @param updateEntity 版本更新信息
     * @return 地址是否是静态网页
     */
    protected boolean isStaticHtmlUrl(@NonNull UpdateEntity updateEntity) {
        String downloadUrl = updateEntity.getDownloadUrl();
        if (TextUtils.isEmpty(downloadUrl)) {
            return false;
        }
        String urlContent = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        return urlContent.contains(".htm") || urlContent.contains(".shtm");
    }


    /**
     * 开启下载服务
     *
     * @param updateEntity     版本更新信息
     * @param downloadListener 下载监听
     */
    protected void startDownloadService(@NonNull final UpdateEntity updateEntity, @Nullable final OnFileDownloadListener downloadListener) {
        DownloadService.bindService(mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIsBound = true;
                startDownload((DownloadService.DownloadBinder) service, updateEntity, downloadListener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsBound = false;
            }
        });
    }

    /**
     * 使用系统的api打开网页
     *
     * @param updateEntity     版本更新信息
     * @param downloadListener 监听回调
     */
    protected void startOpenHtml(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateEntity.getDownloadUrl()));
        boolean result = UpdateUtils.startActivity(intent);
        if (downloadListener != null) {
            if (result) {
                // 强制更新的话，不能关闭更新弹窗
                if (!updateEntity.isForce()) {
                    downloadListener.onCompleted(null);
                }
            } else {
                downloadListener.onError(null);
            }
        }
    }

    /**
     * 开始下载
     *
     * @param binder           下载服务绑定
     * @param updateEntity     版本更新信息
     * @param downloadListener 下载监听
     */
    private void startDownload(DownloadService.DownloadBinder binder, @NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        mDownloadBinder = binder;
        mDownloadBinder.start(updateEntity, downloadListener);
    }

    @Override
    public void cancelDownload() {
        if (mDownloadBinder != null) {
            mDownloadBinder.stop("取消下载");
        }
        if (mIsBound && mServiceConnection != null) {
            XUpdate.getContext().unbindService(mServiceConnection);
            mIsBound = false;
        }
    }

    /**
     * 后台下载更新
     */
    @Override
    public void backgroundDownload() {
        if (mDownloadBinder != null) {
            mDownloadBinder.showNotification();
        }
    }
}
