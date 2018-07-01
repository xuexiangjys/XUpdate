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
import com.xuexiang.xupdate.utils.ApkInstallUtils;

import java.io.File;

/**
 * 版本更新的入口
 *
 * @author xuexiang
 * @since 2018/6/29 下午7:47
 */
public class XUpdate {

    private static XUpdate sInstance;


    /**
     * APK安装监听
     */
    private OnInstallListener mOnInstallListener;

    /**
     * 更新出错监听
     */
    private OnUpdateFailureListener mOnUpdateFailureListener;

    private XUpdate() {
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

    public OnInstallListener getOnInstallListener() {
        return mOnInstallListener;
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

    public OnUpdateFailureListener getOnUpdateFailureListener() {
        return mOnUpdateFailureListener;
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
