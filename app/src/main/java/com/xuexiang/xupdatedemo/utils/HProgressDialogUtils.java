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

package com.xuexiang.xupdatedemo.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Vector on 2016/8/12 0012.
 */
public class HProgressDialogUtils {
    private static ProgressDialog sHorizontalProgressDialog;

    private HProgressDialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    @SuppressLint("NewApi")
    public static void showHorizontalProgressDialog(Context context, String msg, boolean isShowSize) {
        cancel();

        if (sHorizontalProgressDialog == null) {
            sHorizontalProgressDialog = new ProgressDialog(context);
            sHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sHorizontalProgressDialog.setCancelable(false);
            if (isShowSize)
                sHorizontalProgressDialog.setProgressNumberFormat("%2dMB/%1dMB");

        }
        if (!TextUtils.isEmpty(msg)) {
            sHorizontalProgressDialog.setMessage(msg);
        }
        sHorizontalProgressDialog.show();

    }

    public static void setMax(long total) {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
    }

    public static void cancel() {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void setProgress(int current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(current);
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void setProgress(long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    public static void onLoading(long total, long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        if (current == 0) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }
}
