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

import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.proxy.IUpdateProxy;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

/**
 * 简单的版本更新代理
 *
 * @author xuexiang
 * @since 2018/7/1 下午9:47
 */
public abstract class AbstractUpdateProxy implements IUpdateProxy {

    @Override
    public void onBeforeCheck() {

    }

    @Override
    public void onAfterCheck() {

    }

    @Override
    public void noNewVersion(Throwable throwable) {
        _XUpdate.onUpdateError(CHECK_NO_NEW_VERSION, throwable != null ? throwable.getMessage() : null);
    }

}
