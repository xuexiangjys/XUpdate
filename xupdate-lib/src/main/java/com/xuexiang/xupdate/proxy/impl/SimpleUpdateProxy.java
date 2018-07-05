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

package com.xuexiang.xupdate.proxy.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.proxy.IUpdateProxy;

/**
 * 简单的版本更新代理
 *
 * @author xuexiang
 * @since 2018/7/1 下午9:47
 */
public class SimpleUpdateProxy implements IUpdateProxy {

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public IUpdateHttpService getIUpdateHttpService() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void onBeforeCheck() {

    }

    @Override
    public void checkVersion() {

    }

    @Override
    public UpdateEntity parseJson(@NonNull String json) {
        return null;
    }

    @Override
    public void onAfterCheck() {

    }

    @Override
    public void findNewVersion(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy) {

    }

    @Override
    public void noNewVersion(@NonNull Throwable throwable) {

    }

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @NonNull IUpdateHttpService.DownLoadCallback callback) {

    }
}
