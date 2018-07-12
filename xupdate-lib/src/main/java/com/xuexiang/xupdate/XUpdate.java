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

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.listener.impl.DefaultInstallListener;
import com.xuexiang.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.xuexiang.xupdate.logs.UpdateLog;
import com.xuexiang.xupdate.proxy.IUpdateChecker;
import com.xuexiang.xupdate.proxy.IUpdateDownloader;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateDownloader;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateParser;
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

    private Application mContext;
    private static XUpdate sInstance;

    //========全局属性==========//
    /**
     * 请求参数【比如apk-key或者versionCode等】
     */
    Map<String, Object> mParams;
    /**
     * 是否使用的是Get请求
     */
    boolean mIsGet;
    /**
     * 是否只在wifi下进行版本更新检查
     */
    boolean mIsWifiOnly;
    /**
     * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
     */
    boolean mIsAutoMode;
    /**
     * 下载的apk文件缓存目录
     */
    String mApkCacheDir;
    //========全局更新实现接口==========//
    /**
     * 版本更新网络请求服务API
     */
    IUpdateHttpService mIUpdateHttpService;
    /**
     * 版本更新检查器【有默认】
     */
    IUpdateChecker mIUpdateChecker;
    /**
     * 版本更新解析器【有默认】
     */
    IUpdateParser mIUpdateParser;
    /**
     * 版本更新下载器【有默认】
     */
    IUpdateDownloader mIUpdateDownloader;
    /**
     * APK安装监听【有默认】
     */
    OnInstallListener mOnInstallListener;
    /**
     * 更新出错监听【有默认】
     */
    OnUpdateFailureListener mOnUpdateFailureListener;

    //===========================初始化===================================//

    private XUpdate() {
        mIsGet = false;
        mIsWifiOnly = true;
        mIsAutoMode = false;

        mIUpdateChecker = new DefaultUpdateChecker();
        mIUpdateParser = new DefaultUpdateParser();
        mIUpdateDownloader = new DefaultUpdateDownloader();
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

    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        mContext = application;
    }

    private Application getApplication() {
        testInitialize();
        return mContext;
    }

    private void testInitialize() {
        if (mContext == null)
            throw new ExceptionInInitializerError("请先在全局Application中调用 XUpdate.get().init() 初始化！");
    }

    public static Context getContext() {
        return get().getApplication();
    }

    //===========================对外版本更新api===================================//

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context) {
        return new UpdateManager.Builder(context);
    }

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @param updateUrl 版本更新检查的地址
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context, String updateUrl) {
        return new UpdateManager.Builder(context)
                .updateUrl(updateUrl);
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

    /**
     * 设置版本更新检查
     *
     * @param updateChecker
     * @return
     */
    public XUpdate setIUpdateChecker(IUpdateChecker updateChecker) {
        mIUpdateChecker = updateChecker;
        return this;
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

    /**
     * 设置版本更新下载器
     *
     * @param updateDownLoader
     * @return
     */
    public XUpdate setIUpdateDownLoader(IUpdateDownloader updateDownLoader) {
        mIUpdateDownloader = updateDownLoader;
        return this;
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

    /**
     * 设置是否是debug模式
     *
     * @param isDebug
     * @return
     */
    public XUpdate debug(boolean isDebug) {
        UpdateLog.debug(isDebug);
        return this;
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


}
