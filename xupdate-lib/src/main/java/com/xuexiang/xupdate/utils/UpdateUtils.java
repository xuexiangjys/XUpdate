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

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;

import java.io.File;
import java.util.List;

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

    //=======================检查========================//

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

    /**
     * 获取应用的VersionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionCode : -1;
    }

    /**
     * 获取应用的VersionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionName : "";
    }

    /**
     * 比较两个版本号
     *
     * @param versionName1
     * @param versionName2
     * @return [> 0 versionName1 > versionName2] [= 0 versionName1 = versionName2]  [< 0 versionName1 < versionName2]
     */
    public static int compareVersionName(@NonNull String versionName1, @NonNull String versionName2) {
        if (versionName1.equals(versionName2)) {
            return 0;
        }
        String[] versionArray1 = versionName1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = versionName2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    /**
     * 把 JSON 字符串 转换为 单个指定类型的对象
     *
     * @param json     包含了单个对象数据的JSON字符串
     * @param classOfT 指定类型对象的Class
     * @return 指定类型对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把 单个指定类型的对象 转换为 JSON 字符串
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return new Gson().toJson(src);
    }

    //=============显示====================//
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
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

    /**
     * 获取版本更新展示信息
     *
     * @param updateEntity
     * @return
     */
    @NonNull
    public static String getDisplayUpdateInfo(Context context, @NonNull UpdateEntity updateEntity) {
        String targetSize = Formatter.formatShortFileSize(XUpdate.getContext(), updateEntity.getSize() * 1024);
        final String updateContent = updateEntity.getUpdateContent();

        String updateInfo = "";
        if (!TextUtils.isEmpty(targetSize)) {
            updateInfo = context.getString(R.string.xupdate_lab_new_version_size) + targetSize + "\n\r";
        }
        if (!TextUtils.isEmpty(updateContent)) {
            updateInfo += updateContent;
        }
        return updateInfo;
    }

    //=============下载====================//

    /**
     * 判断更新的安装包是否已下载完成【比较md5值】
     *
     * @param updateEntity 更新信息
     * @return
     */
    public static boolean isApkDownloaded(UpdateEntity updateEntity) {
        File appFile = getApkFileByUpdateEntity(updateEntity);
        return !TextUtils.isEmpty(updateEntity.getMd5())
                && appFile.exists()
                && Md5Utils.getFileMD5(appFile).equals(updateEntity.getMd5());
    }

    /**
     * 根据更新信息获取apk安装文件
     *
     * @param updateEntity 更新信息
     * @return
     */
    public static File getApkFileByUpdateEntity(UpdateEntity updateEntity) {
        String appName = getApkNameByDownloadUrl(updateEntity.getDownloadUrl());
        return new File(updateEntity.getApkCacheDir()
                .concat(File.separator + updateEntity.getVersionName())
                .concat(File.separator + appName));
    }

    /**
     * 根据下载地址获取文件名
     *
     * @param downloadUrl
     * @return
     */
    @NonNull
    public static String getApkNameByDownloadUrl(String downloadUrl) {
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

    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString() : "";
    }

    public static Drawable getAppIcon(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.applicationInfo.loadIcon(context.getPackageManager()) : null;
    }

    /**
     * 应用是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
