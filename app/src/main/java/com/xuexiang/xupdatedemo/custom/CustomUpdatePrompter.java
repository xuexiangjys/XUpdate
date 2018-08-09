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

package com.xuexiang.xupdatedemo.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xupdatedemo.utils.HProgressDialogUtils;

import java.io.File;

/**
 * 自定义版本更新提示器
 *
 * @author xuexiang
 * @since 2018/7/12 下午3:48
 */
public class CustomUpdatePrompter implements IUpdatePrompter {

    private Context mContext;

    public CustomUpdatePrompter(Context context) {
        mContext = context;
    }

    @Override
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy) {
        showUpdatePrompt(updateEntity, updateProxy);
    }


    /**
     * 显示自定义提示
     *
     * @param updateEntity
     * @param updateProxy
     */
    private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(mContext, updateEntity);

        new AlertDialog.Builder(mContext)
                .setTitle(String.format("是否升级到%s版本？", updateEntity.getVersionName()))
                .setMessage(updateInfo)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(mContext, "下载进度", false);
                            }

                            @Override
                            public void onProgress(float progress, long total) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                            }

                            @Override
                            public boolean onCompleted(File file) {
                                HProgressDialogUtils.cancel();
                                return true;
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                HProgressDialogUtils.cancel();
                            }
                        });
                    }
                })
                .setNegativeButton("暂不升级", null)
                .setCancelable(false)
                .create()
                .show();
    }
}
