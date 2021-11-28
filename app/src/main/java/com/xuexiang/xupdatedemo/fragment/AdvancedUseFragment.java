/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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
 *
 */

package com.xuexiang.xupdatedemo.fragment;

import android.content.Intent;
import android.graphics.Color;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateParser;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdatedemo.Constants;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xupdatedemo.custom.CustomUpdateParser;
import com.xuexiang.xupdatedemo.custom.CustomUpdatePrompter;
import com.xuexiang.xupdatedemo.http.XHttpUpdateHttpService;
import com.xuexiang.xupdatedemo.utils.CProgressDialogUtils;
import com.xuexiang.xupdatedemo.utils.HProgressDialogUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.resource.ResUtils;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2021/11/25 11:09 PM
 */
@Page(name = "进阶使用")
public class AdvancedUseFragment extends XPageSimpleListFragment {

    private final static int REQUEST_CODE_SELECT_APK_FILE = 1000;

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认App更新 + 自定义提示弹窗主题");
        lists.add("默认App更新 + 自定义Api");
        lists.add("默认App更新 + 自定义Api + 自定义提示弹窗(系统）");
        lists.add("直接传入UpdateEntity进行更新");
        lists.add("使用apk下载功能");
        lists.add("使用apk安装功能");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                XUpdate.newBuild(getActivity())
                        .updateHttpService(new XHttpUpdateHttpService("https://gitee.com"))
                        .updateUrl("/xuexiangjys/XUpdate/raw/master/jsonapi/update_test.json")
                        .promptThemeColor(ResUtils.getColor(R.color.update_theme_color))
                        .promptButtonTextColor(Color.WHITE)
                        .promptTopDrawable(ResUtils.getDrawable(getContext(), R.mipmap.bg_update_top))
//                        .promptTopResId(R.mipmap.bg_update_top)
                        .promptWidthRatio(0.7F)
                        .update();
                break;
            case 1:
                XUpdate.newBuild(getActivity())
                        .updateUrl(Constants.CUSTOM_UPDATE_URL)
                        .updateParser(new CustomUpdateParser())
                        .update();
                break;
            case 2:
                XUpdate.newBuild(getActivity())
                        .updateUrl(Constants.CUSTOM_UPDATE_URL)
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

                            @Override
                            public void noNewVersion(Throwable throwable) {
                                super.noNewVersion(throwable);
                                // 没有最新版本的处理
                            }
                        })
                        .updateParser(new CustomUpdateParser())
                        .updatePrompter(new CustomUpdatePrompter())
                        .update();
                break;
            case 3:
                XUpdate.newBuild(getActivity())
                        .supportBackgroundUpdate(true)
//                        // 忽略下载异常，不关闭更新提示窗
//                        .promptIgnoreDownloadError(true)
                        .build()
                        .update(getUpdateEntityFromAssets());
                break;
            case 4:
                useApkDownLoadFunction();
                break;
            case 5:
                selectAPKFile();
                break;
            default:
                break;
        }
    }

    @MemoryCache
    private UpdateEntity getUpdateEntityFromAssets() {
        try {
            return new DefaultUpdateParser().parseJson(ResourceUtils.readStringFromAssert("update_test.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Permission(PermissionConsts.STORAGE)
    private void useApkDownLoadFunction() {
        XUpdate.newBuild(getActivity())
                // 注意在Android10及以上存在存储权限问题，不建议设置在外部存储下载目录
                .apkCacheDir(PathUtils.getAppExtCachePath())
                .build()
                .download(Constants.XUPDATE_DEMO_DOWNLOAD_URL, new OnFileDownloadListener() {
                    @Override
                    public void onStart() {
                        HProgressDialogUtils.showHorizontalProgressDialog(getContext(), "下载进度", false);
                    }

                    @Override
                    public void onProgress(float progress, long total) {
                        HProgressDialogUtils.setProgress(Math.round(progress * 100));
                    }

                    @Override
                    public boolean onCompleted(File file) {
                        HProgressDialogUtils.cancel();
                        ToastUtils.toast("apk下载完毕，文件路径：" + file.getPath());
                        return false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        HProgressDialogUtils.cancel();
                    }
                });
    }

    @Permission(PermissionConsts.STORAGE)
    private void selectAPKFile() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.ANY), REQUEST_CODE_SELECT_APK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_APK_FILE) {
                _XUpdate.startInstallApk(getContext(), FileUtils.getFileByPath(PathUtils.getFilePathByUri(getContext(), data.getData())));
            }
        }
    }
}
