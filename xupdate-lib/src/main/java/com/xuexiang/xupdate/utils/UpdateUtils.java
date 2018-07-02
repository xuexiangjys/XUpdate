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

package com.xuexiang.xupdate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.xuexiang.xupdate.entity.UpdateEntity;

import java.io.File;

/**
 * 更新工具类
 *
 * @author xuexiang
 * @since 2018/7/2 下午3:24
 */
public final class UpdateUtils {

    private static final String IGNORE_VERSION = "xupdate_ignore_version";
    private static final String PREFS_FILE = "xupdate_prefs";

    private UpdateUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int dip2px(int dip, Context context) {
        return (int) (dip * getDensity(context) + 0.5f);
    }

    private static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取Manifest中注册的MetaData
     *
     * @param context
     * @param name
     * @return
     */
    public static String getManifestMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static String getApkNameByUpdateEntity(UpdateEntity updateEntity) {
        String downloadUrl = updateEntity.getDownloadUrl();
        if (TextUtils.isEmpty(downloadUrl)) {
            return "temp.apk";
        } else {
            String appName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length());
            if (!appName.endsWith(".apk")) {
                appName = "temp.apk";
            }
            return appName;
        }
    }


    /**
     * 不能为null
     *
     * @param object
     * @param message
     * @param <T>
     * @return
     */
    public static <T> T requireNonNull(final T object, final String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 获取应用的缓存目录
     *
     * @param uniqueName 缓存目录
     */
    public static String getDiskCacheDir(Context context, String uniqueName) {
        File cacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (cacheDir == null) {// if cacheDir is null throws NullPointerException
            cacheDir = context.getCacheDir();
        }
        return cacheDir.getPath() + File.separator + uniqueName;
    }

    /**
     * 检测当前网络是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean checkWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检查当前是否有网
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    /**
     * 保存忽略的版本信息
     *
     * @param context
     * @param newVersion
     */
    public static void saveIgnoreVersion(Context context, String newVersion) {
        getSP(context).edit().putString(IGNORE_VERSION, newVersion).apply();
    }

    /**
     * 是否是忽略版本
     *
     * @param context
     * @param newVersion
     * @return
     */
    public static boolean isIgnoreVersion(Context context, String newVersion) {
        return getSP(context).getString(IGNORE_VERSION, "").equals(newVersion);
    }

}
