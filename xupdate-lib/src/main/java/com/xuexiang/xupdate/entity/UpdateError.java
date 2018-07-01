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


import android.text.TextUtils;
import android.util.SparseArray;

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
     * 版本更新错误码
     */
    public final static class ERROR {
        /**
         * 查询更新失败
         */
        public static final int CHECK_UNKNOWN = 2000;
        public static final int CHECK_NO_WIFI = CHECK_UNKNOWN + 1;
        public static final int CHECK_NO_NETWORK = CHECK_NO_WIFI + 1;
        public static final int CHECK_NETWORK_IO = CHECK_NO_NETWORK + 1;
        public static final int CHECK_HTTP_STATUS = CHECK_NETWORK_IO + 1;
        public static final int CHECK_PARSE = CHECK_HTTP_STATUS + 1;

        public static final int DOWNLOAD_UNKNOWN = 3000;
        public static final int DOWNLOAD_CANCELLED = DOWNLOAD_UNKNOWN + 1;
        public static final int DOWNLOAD_DISK_NO_SPACE = DOWNLOAD_CANCELLED + 1;
        public static final int DOWNLOAD_DISK_IO = DOWNLOAD_DISK_NO_SPACE + 1;
        public static final int DOWNLOAD_NETWORK_IO = DOWNLOAD_DISK_IO + 1;
        public static final int DOWNLOAD_NETWORK_BLOCKED = DOWNLOAD_NETWORK_IO +1;
        public static final int DOWNLOAD_NETWORK_TIMEOUT = DOWNLOAD_NETWORK_BLOCKED + 1;
        public static final int DOWNLOAD_HTTP_STATUS = DOWNLOAD_NETWORK_TIMEOUT + 1;
        public static final int DOWNLOAD_INCOMPLETE = DOWNLOAD_HTTP_STATUS + 1;
        public static final int DOWNLOAD_VERIFY = DOWNLOAD_INCOMPLETE + 1;

        /**
         * apk安装错误
         */
        public static final int INSTALL_FAILED = 4000;

        /**
         * 未知的错误
         */
        public static final int UPDATE_UNKNOWN = 5000;
    }

    private static final SparseArray<String> sMessages = new SparseArray<String>();

    static {
        sMessages.append(ERROR.CHECK_UNKNOWN, "查询更新失败：未知错误");
        sMessages.append(ERROR.CHECK_NO_WIFI, "查询更新失败：没有 WIFI");
        sMessages.append(ERROR.CHECK_NO_NETWORK, "查询更新失败：没有网络");
        sMessages.append(ERROR.CHECK_NETWORK_IO, "查询更新失败：网络异常");
        sMessages.append(ERROR.CHECK_HTTP_STATUS, "查询更新失败：错误的HTTP状态");
        sMessages.append(ERROR.CHECK_PARSE, "查询更新失败：解析错误");

        sMessages.append(ERROR.DOWNLOAD_UNKNOWN, "下载失败：未知错误");
        sMessages.append(ERROR.DOWNLOAD_CANCELLED, "下载失败：下载被取消");
        sMessages.append(ERROR.DOWNLOAD_DISK_NO_SPACE, "下载失败：磁盘空间不足");
        sMessages.append(ERROR.DOWNLOAD_DISK_IO, "下载失败：磁盘读写错误");
        sMessages.append(ERROR.DOWNLOAD_NETWORK_IO, "下载失败：网络异常");
        sMessages.append(ERROR.DOWNLOAD_NETWORK_BLOCKED, "下载失败：网络中断");
        sMessages.append(ERROR.DOWNLOAD_NETWORK_TIMEOUT, "下载失败：网络超时");
        sMessages.append(ERROR.DOWNLOAD_HTTP_STATUS, "下载失败：错误的HTTP状态");
        sMessages.append(ERROR.DOWNLOAD_INCOMPLETE, "下载失败：下载不完整");
        sMessages.append(ERROR.DOWNLOAD_VERIFY, "下载失败：校验错误");

        sMessages.append(ERROR.INSTALL_FAILED, "安装APK失败！");
    }
}
