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

package com.xuexiang.xupdate;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.listener.impl.DefaultInstallListener;
import com.xuexiang.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.xuexiang.xupdate.proxy.IUpdateChecker;
import com.xuexiang.xupdate.proxy.IUpdateDownloader;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.utils.ApkInstallUtils;

import java.io.File;
import java.util.Map;

/**
 * 内部版本更新参数的获取
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:27
 */
public final class _XUpdate {

    //===========================属性设置===================================//

    public static Map<String, Object> getParams() {
        return XUpdate.get().mParams;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return XUpdate.get().mIUpdateHttpService;
    }

    public static IUpdateChecker getIUpdateChecker() {
        return XUpdate.get().mIUpdateChecker;
    }

    public static IUpdateParser getIUpdateParser() {
        return XUpdate.get().mIUpdateParser;
    }

    public static IUpdateDownloader getIUpdateDownLoader() {
        return XUpdate.get().mIUpdateDownloader;
    }

    public static boolean isGet() {
        return XUpdate.get().mIsGet;
    }

    public static boolean isWifiOnly() {
        return XUpdate.get().mIsWifiOnly;
    }

    public static boolean isAutoMode() {
        return XUpdate.get().mIsAutoMode;
    }

    public static String getApkCacheDir() {
        return XUpdate.get().mApkCacheDir;
    }

    //===========================apk安装监听===================================//

    public static OnInstallListener getOnInstallListener() {
        return XUpdate.get().mOnInstallListener;
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    public static boolean onInstallApk(Context context, File apkFile, DownloadEntity downloadEntity) {
        if (XUpdate.get().mOnInstallListener == null) {
            XUpdate.get().mOnInstallListener = new DefaultInstallListener();
        }
        return XUpdate.get().mOnInstallListener.onInstallApk(context, apkFile, downloadEntity);
    }

    //===========================更新出错===================================//

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return XUpdate.get().mOnUpdateFailureListener;
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     */
    public static void onUpdateError(int errorCode) {
        onUpdateError(new UpdateError(errorCode));
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     * @param message
     */
    public static void onUpdateError(int errorCode, String message) {
        onUpdateError(new UpdateError(errorCode, message));
    }

    /**
     * 更新出现错误
     *
     * @param updateError
     */
    public static void onUpdateError(@NonNull UpdateError updateError) {
        if (XUpdate.get().mOnUpdateFailureListener == null) {
            XUpdate.get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        XUpdate.get().mOnUpdateFailureListener.onFailure(updateError);
    }

}
