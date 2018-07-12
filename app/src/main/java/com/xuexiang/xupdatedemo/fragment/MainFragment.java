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

package com.xuexiang.xupdatedemo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdatePrompter;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xupdatedemo.entity.CustomResult;
import com.xuexiang.xupdatedemo.utils.CProgressDialogUtils;
import com.xuexiang.xupdatedemo.utils.HProgressDialogUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResUtils;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2018/7/9 下午2:20
 */
@Page(name = "XUpdate 版本更新")
public class MainFragment extends XPageSimpleListFragment {

    private String mUpdateUrl = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_test.json";

    private String mUpdateUrl2 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_forced.json";

    private String mUpdateUrl3 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_custom.json";

    private final static int REQUEST_CODE_SELECT_APK_FILE = 1000;
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认App更新");
        lists.add("版本更新(自动模式)");
        lists.add("强制版本更新");
        lists.add("默认App更新 + 自定义提示弹窗主题");
        lists.add("默认App更新 + 自定义Api");
        lists.add("默认App更新 + 自定义Api + 自定义提示弹窗(系统）");
        lists.add("使用apk安装功能");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        .update();
                break;
            case 1:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        .isAutoMode(true) //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                        .update();
                break;
            case 2:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl2)
                        .update();
                break;
            case 3:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        .themeColor(ResUtils.getColor(R.color.update_theme_color))
                        .topResId(R.mipmap.bg_update_top)
                        .update();
                break;
            case 4:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl3)
                        .updateParser(new IUpdateParser() {
                            @Override
                            public UpdateEntity parseJson(String json) throws Exception {
                                CustomResult result = JsonUtil.fromJson(json, CustomResult.class);
                                if (result != null) {
                                    return new UpdateEntity()
                                            .setHasUpdate(result.hasUpdate)
                                            .setIsIgnorable(result.isIgnorable)
                                            .setVersionCode(result.versionCode)
                                            .setVersionName(result.versionName)
                                            .setUpdateContent(result.updateLog)
                                            .setDownloadUrl(result.apkUrl)
                                            .setSize(result.apkSize);
                                }
                                return null;
                            }
                        })
                        .update();
                break;
            case 5:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl3)
                        .updateChecker(new DefaultUpdateChecker() {
                            @Override
                            public void onBeforeCheck() {
                                super.onBeforeCheck();
                                CProgressDialogUtils.showProgressDialog(getActivity(), "查询中...");
                            }

                            @Override
                            public void onAfterCheck() {
                                super.onAfterCheck();
                                CProgressDialogUtils.cancelProgressDialog(getActivity());
                            }
                        })
                        .updateParser(new IUpdateParser() {
                            @Override
                            public UpdateEntity parseJson(String json) throws Exception {
                                CustomResult result = JsonUtil.fromJson(json, CustomResult.class);
                                if (result != null) {
                                    return new UpdateEntity()
                                            .setHasUpdate(result.hasUpdate)
                                            .setIsIgnorable(result.isIgnorable)
                                            .setVersionCode(result.versionCode)
                                            .setVersionName(result.versionName)
                                            .setUpdateContent(result.updateLog)
                                            .setDownloadUrl(result.apkUrl)
                                            .setSize(result.apkSize);
                                }
                                return null;
                            }
                        })
                        .updatePrompter(new IUpdatePrompter() {
                            @Override
                            public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy) {
                                showUpdatePrompt(updateEntity, updateProxy);
                            }
                        })
                        .update();
                break;
            case 6:
                selectAPKFile();
                break;
            default:
                break;
        }
    }

    @Permission(PermissionConsts.STORAGE)
    private void selectAPKFile() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.ANY), REQUEST_CODE_SELECT_APK_FILE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_APK_FILE) {
                _XUpdate.startInstallApk(getContext(), FileUtils.getFileByPath(PathUtils.getFilePathByUri(getContext(), data.getData())));
            }
        }
    }

    /**
     * 显示自定义提示
     *
     * @param updateEntity
     * @param updateProxy
     */
    private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(updateEntity);

        new AlertDialog.Builder(getActivity())
                .setTitle(String.format("是否升级到%s版本？", updateEntity.getVersionName()))
                .setMessage(updateInfo)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(getActivity(), "下载进度", false);
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

    @Override
    protected TitleBar initTitleBar() {
        return super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtils.exitBy2Click();
            }
        });
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click();
        }
        return true;
    }
}
