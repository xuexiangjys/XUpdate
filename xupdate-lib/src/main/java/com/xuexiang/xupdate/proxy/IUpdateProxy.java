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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

/**
 * 版本更新代理
 *
 * @author xuexiang
 * @since 2018/7/1 下午9:45
 */
public interface IUpdateProxy {

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    @Nullable
    Context getContext();

    /**
     * 获取版本更新的地址
     *
     * @return 版本更新的地址
     */
    String getUrl();

    /**
     * 获取版本更新网络请求服务API
     *
     * @return 网络请求服务API
     */
    IUpdateHttpService getIUpdateHttpService();

    /**
     * 开始版本更新
     */
    void update();

    //============ICheckerProxy=================//

    /**
     * 版本检查之前
     */
    void onBeforeCheck();

    /**
     * 执行网络请求，检查应用的版本信息
     */
    void checkVersion();

    /**
     * 版本检查之后
     */
    void onAfterCheck();

    /**
     * 发现新版本
     *
     * @param updateEntity 版本更新信息
     * @param updateProxy  版本更新代理
     */
    void findNewVersion(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy);

    /**
     * 未发现新版本
     *
     * @param throwable 未发现的原因
     */
    void noNewVersion(Throwable throwable);

    //=============IParserProxy================//

    /**
     * 是否是异步解析者
     *
     * @return 是否是异步解析者
     */
    boolean isAsyncParser();

    /**
     * 将请求的json结果解析为版本更新信息实体【同步方法】
     *
     * @param json 请求的json数据
     * @return 版本更新信息实体
     */
    UpdateEntity parseJson(@NonNull String json) throws Exception;

    /**
     * 将请求的json结果解析为版本更新信息实体【异步方法】
     *
     * @param json     请求的json数据
     * @param callback 解析回调
     */
    void parseJson(@NonNull String json, final IUpdateParseCallback callback) throws Exception;

    //=============IPrompterProxy================//

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 后台下载
     */
    void backgroundDownload();

    /**
     * 取消下载
     */
    void cancelDownload();

    /**
     * 资源回收
     */
    void recycle();

}
