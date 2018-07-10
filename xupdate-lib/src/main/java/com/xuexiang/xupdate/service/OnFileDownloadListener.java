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

import java.io.File;

/**
 * 下载服务下载监听
 *
 * @author xuexiang
 * @since 2018/7/10 上午10:05
 */
public interface OnFileDownloadListener {

    /**
     * 下载之前
     */
    void onStart();

    /**
     * 更新进度
     *
     * @param progress 进度0.00 - 0.50  - 1.00
     * @param total    文件总大小 单位字节
     */
    void onProgress(float progress, long total);

    /**
     * 下载完毕
     *
     * @param file 下载好的文件
     * @return 下载完毕后是否打开文件进行安装<br>{@code true} ：安装<br>{@code false} ：不安装
     */
    boolean onCompleted(File file);

    /**
     * 错误回调
     *
     * @param throwable 错误提示
     */
    void onError(Throwable throwable);


}
