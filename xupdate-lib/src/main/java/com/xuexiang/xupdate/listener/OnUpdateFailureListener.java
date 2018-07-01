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

package com.xuexiang.xupdate.listener;


import com.xuexiang.xupdate.entity.UpdateError;

/**
 * 更新失败监听
 *
 * @author xuexiang
 * @since 2018/7/1 下午7:43
 */
public interface OnUpdateFailureListener {
    /**
     * 更新失败
     *
     * @param error 错误
     */
    void onFailure(UpdateError error);
}