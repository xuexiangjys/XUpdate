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

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.listener.impl.DefaultInstallListener;
import com.xuexiang.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.utils.ApkInstallUtils;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * 版本更新的入口
 *
 * @author xuexiang
 * @since 2018/6/29 下午7:47
 */
public class XUpdate {

    private static XUpdate sInstance;

    //========全局属性==========//
    /**
     * 请求参数【比如apk-key或者versionCode等】
     */
    private Map<String, Object> mParams;
    /**
     * 是否使用的是Get请求
     */
    private boolean mIsGet;
    /**
     * 是否只在wifi下进行版本更新检查
     */
    private boolean mIsWifiOnly;
    /**
     * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
     */
    private boolean mIsAutoMode;
    /**
     * 下载的apk文件缓存目录
     */
    private String mApkCacheDir;
    //========全局更新实现接口==========//
    /**
     * 版本更新网络请求服务API
     */
    private IUpdateHttpService mIUpdateHttpService;
    /**
     * 版本更新解析器
     */
    private IUpdateParser mIUpdateParser;
    /**
     * APK安装监听
     */
    private OnInstallListener mOnInstallListener;
    /**
     * 更新出错监听
     */
    private OnUpdateFailureListener mOnUpdateFailureListener;

    private XUpdate() {
        mIsGet = false;
        mIsWifiOnly = true;
        mIsAutoMode = false;

        mOnInstallListener = new DefaultInstallListener();
        mOnUpdateFailureListener = new DefaultUpdateFailureListener();


    }


    /**
     * 获取版本更新的入口
     *
     * @return 版本更新的入口
     */
    public static XUpdate get() {
        if (sInstance == null) {
            synchronized (XUpdate.class) {
                if (sInstance == null) {
                    sInstance = new XUpdate();
                }
            }
        }
        return sInstance;
    }

    //===========================属性设置===================================//

    /**
     * 设置全局的apk更新请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public XUpdate param(@NonNull String key, @NonNull Object value) {
        if (mParams == null) {
            mParams = new TreeMap<>();
        }
        mParams.put(key, value);
        return this;
    }

    /**
     * 设置全局的apk更新请求参数
     *
     * @param params
     * @return
     */
    public XUpdate params(@NonNull Map<String, Object> params) {
        mParams = params;
        return this;
    }

    public static Map<String, Object> getParams() {
        return get().mParams;
    }

    /**
     * 设置版本更新网络请求服务API
     *
     * @param updateHttpService
     * @return
     */
    public XUpdate setIUpdateHttpService(IUpdateHttpService updateHttpService) {
        mIUpdateHttpService = updateHttpService;
        return this;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return get().mIUpdateHttpService;
    }

    /**
     * 设置版本更新的解析器
     *
     * @param updateParser
     * @return
     */
    public XUpdate setIUpdateParser(IUpdateParser updateParser) {
        mIUpdateParser = updateParser;
        return this;
    }

    public static IUpdateParser getIUpdateParser() {
        return get().mIUpdateParser;
    }

    /**
     * 是否使用的是Get请求
     *
     * @param isGet
     * @return
     */
    public XUpdate isGet(boolean isGet) {
        mIsGet = isGet;
        return this;
    }

    public static boolean isGet() {
        return get().mIsGet;
    }

    /**
     * 设置是否只在wifi下进行版本更新检查
     *
     * @param isWifiOnly
     * @return
     */
    public XUpdate isWifiOnly(boolean isWifiOnly) {
        mIsWifiOnly = isWifiOnly;
        return this;
    }

    public static boolean isWifiOnly() {
        return get().mIsWifiOnly;
    }

    /**
     * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
     *
     * @param isAutoMode
     * @return
     */
    public XUpdate isAutoMode(boolean isAutoMode) {
        mIsAutoMode = isAutoMode;
        return this;
    }

    public static boolean isAutoMode() {
        return get().mIsAutoMode;
    }

    /**
     * 设置apk的缓存路径
     *
     * @param apkCacheDir
     * @return
     */
    public XUpdate setApkCacheDir(String apkCacheDir) {
        mApkCacheDir = apkCacheDir;
        return this;
    }

    public static String getApkCacheDir() {
        return get().mApkCacheDir;
    }

    //===========================apk安装监听===================================//

    /**
     * 设置安装监听
     *
     * @param onInstallListener
     * @return
     */
    public XUpdate setOnInstallListener(OnInstallListener onInstallListener) {
        mOnInstallListener = onInstallListener;
        return this;
    }

    public static OnInstallListener getOnInstallListener() {
        return get().mOnInstallListener;
    }

    /**
     * 安装apk
     *
     * @param context      传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile      apk文件
     * @param updateEntity 更新信息
     */
    public static void onInstallApk(Context context, File apkFile, UpdateEntity updateEntity) {
        if (get().mOnInstallListener == null) {
            get().mOnInstallListener = new DefaultInstallListener();
        }
        get().mOnInstallListener.onInstallApk(context, apkFile, updateEntity);
    }

    //===========================更新出错===================================//

    /**
     * 设置更新出错的监听
     *
     * @param onUpdateFailureListener
     * @return
     */
    public XUpdate setOnUpdateFailureListener(@NonNull OnUpdateFailureListener onUpdateFailureListener) {
        mOnUpdateFailureListener = onUpdateFailureListener;
        return this;
    }

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return get().mOnUpdateFailureListener;
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
        if (get().mOnUpdateFailureListener == null) {
            get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        get().mOnUpdateFailureListener.onFailure(updateError);
    }
}
