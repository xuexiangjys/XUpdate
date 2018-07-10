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

package com.xuexiang.xupdate.proxy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

/**
 * 版本更新下载器
 *
 * @author xuexiang
 * @since 2018/6/29 下午8:31
 */
public interface IUpdateDownloader {

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 取消下载
     */
    void cancelDownload();
}
