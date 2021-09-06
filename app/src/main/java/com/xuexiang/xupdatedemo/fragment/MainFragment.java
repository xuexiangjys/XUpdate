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
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateParser;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xupdatedemo.activity.UpdateActivity;
import com.xuexiang.xupdatedemo.custom.CustomUpdateParser;
import com.xuexiang.xupdatedemo.custom.CustomUpdatePrompter;
import com.xuexiang.xupdatedemo.http.XHttpUpdateHttpService;
import com.xuexiang.xupdatedemo.utils.CProgressDialogUtils;
import com.xuexiang.xupdatedemo.utils.HProgressDialogUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.resource.ResUtils;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2018/7/9 下午2:20
 */
@Page(name = "XUpdate 版本更新", anim = CoreAnim.none)
public class MainFragment extends XPageSimpleListFragment {

    private String mUpdateUrl = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_test.json";

    private String mUpdateUrl2 = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_forced.json";

    private String mUpdateUrl3 = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_custom.json";

    private String mDownloadUrl = "https://xuexiangjys.oss-cn-shanghai.aliyuncs.com/apk/xupdate_demo_1.0.2.apk";

    private final static int REQUEST_CODE_SELECT_APK_FILE = 1000;

    private boolean mIsSimplifiedChinese;

    @Override
    protected void initArgs() {
        Resources resource = getResources();
        Configuration config = resource.getConfiguration();
        mIsSimplifiedChinese = config.locale == Locale.SIMPLIFIED_CHINESE;
    }

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("获取文件的MD5值");
        lists.add("默认App更新");
        lists.add("默认App更新 + 支持后台更新");
        lists.add("版本更新(自动模式)");
        lists.add("强制版本更新");
        lists.add("默认App更新 + 自定义提示弹窗主题");
        lists.add("默认App更新 + 自定义Api");
        lists.add("默认App更新 + 自定义Api + 自定义提示弹窗(系统）");
        lists.add("直接传入UpdateEntity进行更新");
        lists.add("使用apk下载功能");
        lists.add("使用apk安装功能");
        lists.add("版本更新提示框在FragmentActivity中使用UpdateDialogFragment, 在普通Activity中使用UpdateDialog");
        lists.add("使用XUpdateService版本更新服务");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                openPage(FileMD5Fragment.class);
                break;
            case 1:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        .update();
                break;
            case 2:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        .supportBackgroundUpdate(true)
                        .update();
                break;
            case 3:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                        .isAutoMode(true)
                        .update();
                break;
            case 4:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl2)
                        .update();
                break;
            case 5:
                XUpdate.newBuild(getActivity())
                        .updateHttpService(new XHttpUpdateHttpService("https://gitee.com"))
                        .updateUrl("/xuexiangjys/XUpdate/raw/master/jsonapi/update_test.json")
                        .promptThemeColor(ResUtils.getColor(R.color.update_theme_color))
                        .promptButtonTextColor(Color.WHITE)
                        .promptTopResId(R.mipmap.bg_update_top)
                        .promptWidthRatio(0.7F)
                        .update();
                break;
            case 6:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl3)
                        .updateParser(new CustomUpdateParser())
                        .update();
                break;
            case 7:
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
            case 8:
                XUpdate.newBuild(getActivity())
                        .supportBackgroundUpdate(true)
//                        // 忽略下载异常，不关闭更新提示窗
//                        .promptIgnoreDownloadError(true)
                        .build()
                        .update(getUpdateEntityFromAssets());
                break;
            case 9:
                useApkDownLoadFunction();
                break;
            case 10:
                selectAPKFile();
                break;
            case 11:
                startActivity(new Intent(getContext(), UpdateActivity.class));
                break;
            case 12:
                openPage(XUpdateServiceFragment.class);
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
                .download(mDownloadUrl, new OnFileDownloadListener() {
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


    private TextView mAction;

    @Override
    protected TitleBar initTitleBar() {
        TitleBar titleBar = super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtils.exitBy2Click();
            }
        });
        mAction = (TextView) titleBar.addAction(new TitleBar.TextAction(mIsSimplifiedChinese ? "简体中文" : "系统默认") {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (mIsSimplifiedChinese) {
                    changeLocale(Locale.getDefault());
                } else {
                    changeLocale(Locale.SIMPLIFIED_CHINESE);
                }
                mIsSimplifiedChinese = !mIsSimplifiedChinese;
                mAction.setText(mIsSimplifiedChinese ? "简体中文" : "系统默认");

            }
        });
        return titleBar;
    }


    /**
     * 切换语言
     *
     * @param locale 需要切换的语言
     */
    private void changeLocale(Locale locale) {
        Resources resource = getResources();
        Configuration config = resource.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        getResources().updateConfiguration(config, null);
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

    @Override
    public void onDestroyView() {
        CollectionUtils.clear(mSimpleData);
        super.onDestroyView();
    }
}
