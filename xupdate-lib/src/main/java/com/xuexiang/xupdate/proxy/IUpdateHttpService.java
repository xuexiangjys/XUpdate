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

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Map;

/**
 * 版本更新网络请求服务API
 *
 * @author xuexiang
 * @since 2018/6/29 下午8:44
 */
public interface IUpdateHttpService {

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack);


    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, @NonNull Callback callBack);

    /**
     * 文件下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 文件下载回调
     */
    void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull DownloadCallback callback);

    /**
     * 取消文件下载
     *
     * @param url      下载地址
     */
    void cancelDownload(@NonNull String url);

    /**
     * 网络请求回调
     */
    interface Callback {
        /**
         * 结果回调
         *
         * @param result 结果
         */
        void onSuccess(String result);

        /**
         * 错误回调
         *
         * @param throwable 错误提示
         */
        void onError(Throwable throwable);
    }

    /**
     * 下载回调
     */
    interface DownloadCallback {
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
         * 结果回调
         *
         * @param file 下载好的文件
         */
        void onSuccess(File file);

        /**
         * 错误回调
         *
         * @param throwable 错误提示
         */
        void onError(Throwable throwable);

    }

}
