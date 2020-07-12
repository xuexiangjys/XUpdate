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

import java.util.Map;

/**
 * 版本更新检查器
 *
 * @author xuexiang
 * @since 2018/6/29 下午8:29
 */
public interface IUpdateChecker {

    /**
     * 版本检查之前
     */
    void onBeforeCheck();

    /**
     * 检查应用的版本信息
     *
     * @param isGet       是否是get请求
     * @param url         版本更新的url地址
     * @param params      请求参数
     * @param updateProxy 版本更新代理
     */
    void checkVersion(boolean isGet, @NonNull String url, @NonNull Map<String, Object> params, @NonNull IUpdateProxy updateProxy);

    /**
     * 版本检查之后
     */
    void onAfterCheck();

    /**
     * 处理版本检查的结果
     *
     * @param result
     * @param updateProxy 版本更新代理
     */
    void processCheckResult(@NonNull String result, @NonNull IUpdateProxy updateProxy);

}
