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

package com.xuexiang.xupdate;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xuexiang.xupdate.proxy.IUpdateChecker;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;

import java.util.Map;
import java.util.TreeMap;

/**
 * 版本更新管理
 *
 * @author xuexiang
 * @since 2018/7/1 下午9:49
 */
public class UpdateManager {

    private Context mContext;
    //============请求参数==============//
    /**
     * 版本更新的url地址
     */
    private String mUpdateUrl;
    /**
     * 请求参数
     */
    private Map<String, Object> mParams;

    //===========更新模式================//
    /**
     * 是否只在wifi下进行版本更新检查
     */
    private boolean mIsWifiOnly;
    /**
     * 是否是自动版本更新模式【无人干预】
     */
    private boolean mIsAutoMode;

    //===========更新组件===============//
    /**
     * 版本更新网络请求服务API
     */
    private IUpdateHttpService mIUpdateHttpService;
    /**
     *
     */
    private IUpdateParser mIUpdateParser;
    /**
     *
     */
    private IUpdateChecker mIUpdateChecker;
    /**
     *
     */
    private IUpdatePrompter mIUpdatePrompter;


    /**
     * 构造函数
     *
     * @param builder
     */
    private UpdateManager(Builder builder) {
        mContext = builder.context;
        mUpdateUrl = builder.updateUrl;
        mParams = builder.params;

        mIUpdateHttpService = builder.updateHttpService;
    }


    /**
     * 版本更新管理构建者
     */
    public static class Builder {
        //=======必填项========//
        Context context;
        /**
         * 版本更新的url地址
         */
        String updateUrl;
        /**
         * 请求参数
         */
        Map<String, Object> params;
        /**
         * 版本更新网络请求服务API
         */
        IUpdateHttpService updateHttpService;

        //===========更新模式================//
        /**
         * 是否只在wifi下进行版本更新检查
         */
        boolean isWifiOnly;
        /**
         * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
         */
        boolean isAutoMode;

        public Builder(Context context) {
            this.context = context;

            params = new TreeMap<>();
            if (XUpdate.getParams() != null) {
                params.putAll(XUpdate.getParams());
            }

            isWifiOnly = XUpdate.isWifiOnly();
            isAutoMode = XUpdate.isAutoMode();
        }

        /**
         * 设置版本更新检查的url
         *
         * @param updateUrl
         * @return
         */
        public Builder updateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param params
         * @return
         */
        public Builder params(@NonNull Map<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param key
         * @param value
         * @return
         */
        public Builder param(@NonNull String key, @NonNull Object value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * 设置网络请求的请求服务API
         * @param updateHttpService
         * @return
         */
        public Builder updateHttpService(IUpdateHttpService updateHttpService) {
            this.updateHttpService = updateHttpService;
            return this;
        }
    }
}
