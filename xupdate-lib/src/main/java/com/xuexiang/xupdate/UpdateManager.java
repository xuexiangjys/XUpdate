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

import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.proxy.IUpdateChecker;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;

import java.util.Map;

/**
 * 版本更新管理
 *
 * @author xuexiang
 * @since 2018/7/1 下午9:49
 */
public class UpdateManager {

    //============请求参数==============//
    /**
     * 版本更新的url地址
     */
    private String mUpdateUrl;
    /**
     * 请求参数
     */
    private Map<String, String> mParams;

    //===========更新模式================//
    /**
     * 是否是自动版本更新模式【无人干预】
     */
    private boolean mIsAutoMode;

    private boolean mIsWifiOnly = false;

    //===========更新组件===============//
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

}
