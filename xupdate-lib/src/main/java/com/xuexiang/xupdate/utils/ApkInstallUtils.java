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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.utils.ShellUtils.CommandResult;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.INSTALL_PACKAGES;
import static com.xuexiang.xupdate.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * APK安装工具类
 *
 * @author xuexiang
 * @since 2018/7/2 上午1:18
 */
public final class ApkInstallUtils {

    /**
     * apk安装的请求码
     */
    public static final int REQUEST_CODE_INSTALL_APP = 999;

    private ApkInstallUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * apk安装
     *
     * @param context
     * @param apkFile apk文件
     * @return
     */
    public static boolean install(Context context, File apkFile) throws IOException {
        return install(context, apkFile.getCanonicalPath());
    }


    /**
     * apk安装
     *
     * @param context
     * @param filePath apk文件的路径
     * @return
     */
    public static boolean install(Context context, String filePath) {
        if (ApkInstallUtils.isSystemApplication(context)
                || ShellUtils.checkRootPermission()) {
            return installAppSilent(context, filePath);
        }
        return installNormal(context, filePath);
    }


    /**
     * 静默安装 App
     * <p>非 root 需添加权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件路径
     * @return {@code true}: 安装成功<br>{@code false}: 安装失败
     */
    @RequiresPermission(INSTALL_PACKAGES)
    public static boolean installAppSilent(Context context, String filePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return installAppSilentBelow24(filePath);
        } else {
            return installAppSilentAbove24(context.getPackageName(), filePath);
        }
    }

    /**
     * 静默安装 App 在Android7.0以下起作用
     * <p>非 root 需添加权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件路径
     * @return {@code true}: 安装成功<br>{@code false}: 安装失败
     */
    @RequiresPermission(INSTALL_PACKAGES)
    private static boolean installAppSilentBelow24(String filePath) {
        File file = getFileByPath(filePath);
        if (!isFileExists(file)) return false;
        boolean isRoot = isDeviceRooted();
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
        CommandResult commandResult = ShellUtils.execCommand(command, isRoot);
        if (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib64 pm install " + filePath;
            commandResult = ShellUtils.execCommand(command, isRoot);
            return commandResult.successMsg != null
                    && commandResult.successMsg.toLowerCase().contains("success");
        }
    }

    /**
     * 静默安装 App 在Android7.0及以上起作用
     * <p>非 root 需添加权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件路径
     * @return {@code true}: 安装成功<br>{@code false}: 安装失败
     */
    @RequiresPermission(INSTALL_PACKAGES)
    private static boolean installAppSilentAbove24(String packageName, String filePath) {
        File file = getFileByPath(filePath);
        if (!isFileExists(file)) return false;
        boolean isRoot = isDeviceRooted();
        String command = "pm install -i " + packageName + " --user 0 " + filePath;
        CommandResult commandResult = ShellUtils.execCommand(command, isRoot);
        return (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success"));
    }

    /**
     * install package normal by system intent
     *
     * @param context
     * @param filePath file path of package
     * @return whether apk exist
     */
    private static boolean installNormal(Context context, String filePath) {
        File file = getFileByPath(filePath);
        return isFileExists(file) && installNormal(context, file);
    }

    /**
     * 使用系统的意图进行apk安装
     *
     * @param context
     * @param appFile
     * @return
     */
    private static boolean installNormal(Context context, File appFile) {
        try {
            Intent intent = getInstallAppIntent(context, appFile);
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE_INSTALL_APP);
                } else {
                    context.startActivity(intent);
                }
                return true;
            }
        } catch (Exception e) {
            _XUpdate.onUpdateError(INSTALL_FAILED, "使用系统的意图进行apk安装失败！");
        }
        return false;
    }

    /**
     * 获取安装apk的意图
     *
     * @param context
     * @param appFile
     * @return
     */
    public static Intent getInstallAppIntent(Context context, File appFile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".updateFileProvider", appFile);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            _XUpdate.onUpdateError(INSTALL_FAILED, "获取安装的意图失败！");
        }
        return null;
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 判断字符串是否为 null 或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null 或全空白字符<br> {@code false}: 不为 null 且不全空白字符
     */
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    /**
     * 判断设备是否 root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    private static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }


    /**
     * whether context is system application
     *
     * @param context
     * @return
     */
    private static boolean isSystemApplication(Context context) {
        return context != null && isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     *
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isSystemApplication(Context context,
                                               String packageName) {
        return context != null && isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * whether packageName is system application
     *
     * @param packageManager
     * @param packageName
     * @return <ul>
     * <li>if packageManager is null, return false</li>
     * <li>if package name is null or is empty, return false</li>
     * <li>if package name not exit, return false</li>
     * <li>if package name exit, but not system app, return false</li>
     * <li>else return true</li>
     * </ul>
     */
    private static boolean isSystemApplication(PackageManager packageManager,
                                               String packageName) {
        if (packageManager == null || packageName == null
                || packageName.length() == 0) {
            return false;
        }
        try {
            ApplicationInfo app = packageManager.getApplicationInfo(
                    packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
