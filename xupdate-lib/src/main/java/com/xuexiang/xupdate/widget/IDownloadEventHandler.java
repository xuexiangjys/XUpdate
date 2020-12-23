/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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
 *
 */

package com.xuexiang.xupdate.widget;

import java.io.File;

/**
 * 下载事件处理者
 *
 * @author xuexiang
 * @since 2020/12/23 10:47 PM
 */
public interface IDownloadEventHandler {

    /**
     * 处理开始下载
     */
    void handleStart();

    /**
     * 处理下载中的进度更新
     *
     * @param progress 下载进度
     */
    void handleProgress(float progress);

    /**
     * 处理下载完毕
     *
     * @param file 下载文件
     * @return 下载完毕后是否打开文件进行安装<br>{@code true} ：安装<br>{@code false} ：不安装
     */
    boolean handleCompleted(File file);

    /**
     * 处理下载失败
     *
     * @param throwable 失败原因
     */
    void handleError(Throwable throwable);
}
