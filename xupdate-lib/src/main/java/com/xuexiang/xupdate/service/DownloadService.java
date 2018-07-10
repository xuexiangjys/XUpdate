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

package com.xuexiang.xupdate.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.xuexiang.xupdate.R;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.utils.ApkInstallUtils;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.DOWNLOAD_FAILED;

/**
 * APK下载服务
 *
 * @author xuexiang
 * @since 2018/7/5 上午11:15
 */
public class DownloadService extends Service {

    private static final int DOWNLOAD_NOTIFY_ID = 1000;

    private static boolean mIsRunning = false;

    private static final String CHANNEL_ID = "xupdate_channel_id";
    private static final CharSequence CHANNEL_NAME = "xupdate_channel_name";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    //=====================绑定服务============================//

    /**
     * 绑定服务
     *
     * @param connection
     */
    public static void bindService(ServiceConnection connection) {
        Intent intent = new Intent(XUpdate.getContext(), DownloadService.class);
        XUpdate.getContext().startService(intent);
        XUpdate.getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        mIsRunning = true;
    }

    /**
     * 停止下载服务
     *
     * @param contentText
     */
    private void stop(String contentText) {
        if (mBuilder != null) {
            mBuilder.setContentTitle(UpdateUtils.getAppName(DownloadService.this))
                    .setContentText(contentText);
            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, notification);
        }
        close();
    }

    /**
     * 关闭服务
     */
    private void close() {
        mIsRunning = false;
        stopSelf();
    }

    //=====================生命周期============================//
    /**
     * 下载服务是否在运行
     *
     * @return
     */
    public static boolean isRunning() {
        return mIsRunning;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mIsRunning = true;
        return new DownloadBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsRunning = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        super.onDestroy();
    }

    //========================下载通知===================================//

    /**
     * 创建通知
     */
    private void setUpNotification(@NonNull DownloadEntity downloadEntity) {
        if (!downloadEntity.isShowNotification()) {
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //设置绕过免打扰模式
//            channel.setBypassDnd(false);
//            //检测是否绕过免打扰模式
//            channel.canBypassDnd();
//            //设置在锁屏界面上显示这条通知
//            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            channel.setLightColor(Color.GREEN);
//            channel.setShowBadge(true);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.enableVibration(false);
            channel.enableLights(false);

            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = getNotificationBuilder();
        mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, mBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.xupdate_start_download))
                .setContentText(getString(R.string.xupdate_connecting_service))
                .setSmallIcon(R.drawable.xupdate_icon_app_update)
                .setLargeIcon(UpdateUtils.drawable2Bitmap(UpdateUtils.getAppIcon(DownloadService.this)))
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
    }

    /**
     * DownloadBinder中定义了一些实用的方法
     *
     * @author user
     */
    public class DownloadBinder extends Binder {
        /**
         * 开始下载
         *
         * @param updateEntity      新app信息
         * @param downloadListener  下载监听
         */
        public void start(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
            //下载
            startDownload(updateEntity, downloadListener);
        }

        /**
         * 停止下载服务
         *
         * @param msg
         */
        public void stop(String msg) {
            DownloadService.this.stop(msg);
        }
    }

    /**
     * 下载模块
     */
    private void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        String apkUrl = updateEntity.getDownloadUrl();
        if (TextUtils.isEmpty(apkUrl)) {
            String contentText = "新版本下载路径错误";
            stop(contentText);
            return;
        }
        String appName = UpdateUtils.getApkNameByDownloadUrl(apkUrl);

        File appDir = new File(updateEntity.getApkCacheDir());
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String target = appDir + File.separator + updateEntity.getVersionName();

        updateEntity.getIUpdateHttpService().download(apkUrl, target, appName, new FileDownloadCallBack(updateEntity, downloadListener));
    }


    /**
     * 文件下载处理
     */
    private class FileDownloadCallBack implements IUpdateHttpService.DownloadCallback {

        private final DownloadEntity mDownloadEntity;
        /**
         * 文件下载监听
         */
        private final OnFileDownloadListener mOnFileDownloadListener;

        /**
         * 是否下载完成后自动安装
         */
        private boolean mIsAutoInstall;

        private int oldRate = 0;

        FileDownloadCallBack(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener listener) {
            mDownloadEntity = updateEntity.getDownLoadEntity();
            mIsAutoInstall = updateEntity.isAutoInstall();
            mOnFileDownloadListener = listener;
        }


        @Override
        public void onStart() {
            //初始化通知栏
            setUpNotification(mDownloadEntity);
            if (mOnFileDownloadListener != null) {
                mOnFileDownloadListener.onStart();
            }
        }

        @Override
        public void onProgress(float progress, long total) {
            //做一下判断，防止自回调过于频繁，造成更新通知栏进度过于频繁，而出现卡顿的问题。
            int rate = Math.round(progress * 100);
            if (oldRate != rate) {
                if (mOnFileDownloadListener != null) {
                    mOnFileDownloadListener.onProgress(progress, total);
                }

                if (mBuilder != null) {
                    mBuilder.setContentTitle("正在下载：" + UpdateUtils.getAppName(DownloadService.this))
                            .setContentText(rate + "%")
                            .setProgress(100, rate, false)
                            .setWhen(System.currentTimeMillis());
                    Notification notification = mBuilder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
                    mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, notification);
                }
                //重新赋值
                oldRate = rate;
            }

        }

        @Override
        public void onSuccess(File file) {
            if (mOnFileDownloadListener != null) {
                if (!mOnFileDownloadListener.onCompleted(file)) {
                    close();
                    return;
                }
            }

            try {
                if (UpdateUtils.isAppOnForeground(DownloadService.this)) {
                    //App前台运行
                    mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);

                    if (mIsAutoInstall) {
                        XUpdate.onInstallApk(DownloadService.this, file, mDownloadEntity);
                    } else {
                        showDownloadCompleteNotification(file);
                    }
                } else {
                    showDownloadCompleteNotification(file);
                }
                //下载完自杀
                close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            XUpdate.onUpdateError(DOWNLOAD_FAILED, throwable.getMessage());
            //App前台运行
            if (mOnFileDownloadListener != null) {
                mOnFileDownloadListener.onError(throwable);
            }
            try {
                mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDownloadCompleteNotification(File file) {
        //App后台运行
        //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
        Intent installAppIntent = ApkInstallUtils.getInstallAppIntent(DownloadService.this, file);
        PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, 0, installAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mBuilder == null) {
            mBuilder = getNotificationBuilder();
        }
        mBuilder.setContentIntent(contentIntent)
                .setContentTitle(UpdateUtils.getAppName(DownloadService.this))
                .setContentText(getString(R.string.xupdate_download_complete))
                .setProgress(0, 0, false)
                //                        .setAutoCancel(true)
                .setDefaults((Notification.DEFAULT_ALL));
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, notification);
    }
}
