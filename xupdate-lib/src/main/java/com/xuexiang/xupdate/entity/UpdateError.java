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

package com.xuexiang.xupdate.entity;


import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.xuexiang.xupdate.R;

/**
 * 更新错误
 *
 * @author xuexiang
 * @since 2018/6/29 下午9:01
 */
public class UpdateError extends Throwable {

    /**
     * 错误码
     */
    private final int mCode;

    public UpdateError(int code) {
        this(code, null);
    }

    public UpdateError(int code, String message) {
        super(make(code, message));
        mCode = code;
    }

    public UpdateError(Throwable e) {
        super(e);
        mCode = ERROR.UPDATE_UNKNOWN;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    private static String make(int code, String message) {
        String m = sMessages.get(code);
        if (TextUtils.isEmpty(m)) {
            return "";
        }
        if (TextUtils.isEmpty(message) || message.equals("null")) {
            return m;
        }
        return m + "(" + message + ")";
    }

    /**
     * 获取详细的错误信息
     *
     * @return
     */
    public String getDetailMsg() {
        return "Code:" + mCode + ", msg:" + getMessage();
    }

    /**
     * 版本更新错误码
     */
    public final static class ERROR {

        /**
         * 查询更新失败
         */
        public static final int CHECK_NET_REQUEST = 2000;
        public static final int CHECK_NO_WIFI = CHECK_NET_REQUEST + 1;
        public static final int CHECK_NO_NETWORK = CHECK_NO_WIFI + 1;
        public static final int CHECK_UPDATING = CHECK_NO_NETWORK + 1;
        public static final int CHECK_NO_NEW_VERSION = CHECK_UPDATING + 1;
        public static final int CHECK_JSON_EMPTY = CHECK_NO_NEW_VERSION + 1;
        public static final int CHECK_PARSE = CHECK_JSON_EMPTY + 1;
        public static final int CHECK_IGNORED_VERSION = CHECK_PARSE + 1;
        public static final int CHECK_APK_CACHE_DIR_EMPTY = CHECK_IGNORED_VERSION + 1;

        public static final int PROMPT_UNKNOWN = 3000;
        public static final int PROMPT_ACTIVITY_DESTROY = PROMPT_UNKNOWN + 1;

        public static final int DOWNLOAD_FAILED = 4000;
        public static final int DOWNLOAD_PERMISSION_DENIED = DOWNLOAD_FAILED + 1;

        /**
         * apk安装错误
         */
        public static final int INSTALL_FAILED = 5000;

        /**
         * 未知的错误
         */
        public static final int UPDATE_UNKNOWN = 5100;
    }

    private static final SparseArray<String> sMessages = new SparseArray<>();

    /**
     * 初始化错误信息
     *
     * @param context
     */
    public static void init(Context context) {
        sMessages.append(ERROR.CHECK_NET_REQUEST, context.getString(R.string.xupdate_error_check_net_request));
        sMessages.append(ERROR.CHECK_NO_WIFI, context.getString(R.string.xupdate_error_check_no_wifi));
        sMessages.append(ERROR.CHECK_NO_NETWORK, context.getString(R.string.xupdate_error_check_no_network));
        sMessages.append(ERROR.CHECK_UPDATING, context.getString(R.string.xupdate_error_check_updating));
        sMessages.append(ERROR.CHECK_NO_NEW_VERSION, context.getString(R.string.xupdate_error_check_no_new_version));
        sMessages.append(ERROR.CHECK_JSON_EMPTY, context.getString(R.string.xupdate_error_check_json_empty));
        sMessages.append(ERROR.CHECK_PARSE, context.getString(R.string.xupdate_error_check_parse));
        sMessages.append(ERROR.CHECK_IGNORED_VERSION, context.getString(R.string.xupdate_error_check_ignored_version));
        sMessages.append(ERROR.CHECK_APK_CACHE_DIR_EMPTY, context.getString(R.string.xupdate_error_check_apk_cache_dir_empty));

        sMessages.append(ERROR.PROMPT_UNKNOWN, context.getString(R.string.xupdate_error_prompt_unknown));
        sMessages.append(ERROR.PROMPT_ACTIVITY_DESTROY, context.getString(R.string.xupdate_error_prompt_activity_destroy));

        sMessages.append(ERROR.DOWNLOAD_FAILED, context.getString(R.string.xupdate_error_download_failed));
        sMessages.append(ERROR.DOWNLOAD_PERMISSION_DENIED, context.getString(R.string.xupdate_error_download_permission_denied));

        sMessages.append(ERROR.INSTALL_FAILED, context.getString(R.string.xupdate_error_install_failed));
    }
}
