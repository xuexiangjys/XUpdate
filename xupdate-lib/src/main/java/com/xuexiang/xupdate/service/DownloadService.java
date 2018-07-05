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

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * APK下载服务
 *
 * @author xuexiang
 * @since 2018/7/5 上午11:15
 */
public class DownloadService extends IntentService {

    private static final String SERVICE_NAME = "xupdate-download";
    private static final String CHANNEL_ID = "xupdate_channel_id";
    private static final CharSequence CHANNEL_NAME = "xupdate_channel_name";

    private NotificationManager mNotificationManager;

    public DownloadService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
