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

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;

/**
 * 版本更新解析器[异步解析和同步解析方法只需要实现一个就行了，当isAsyncParser为true时需要实现异步解析方法，否则实现同步解析方法]
 *
 * @author xuexiang
 * @since 2018/6/29 下午8:30
 */
public interface IUpdateParser {

    /**
     * [同步解析方法]
     * <p>
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @return
     */
    UpdateEntity parseJson(String json) throws Exception;

    /**
     * [异步解析方法]
     * <p>
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @param callback
     * @return
     */
    void parseJson(String json, final IUpdateParseCallback callback) throws Exception;

    /**
     * @return 是否是异步解析
     */
    boolean isAsyncParser();

}
