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

import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xupdatedemo.entity.CustomResult;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/7/9 下午2:20
 */
@Page(name = "XUpdate 版本更新")
public class MainFragment extends XPageSimpleListFragment {

    private String mUpdateUrl = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_test.json";

    private String mUpdateUrl2 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_forced.json";

    private String mUpdateUrl3 = "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/jsonapi/update_custom.json";

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认App更新");
        lists.add("版本更新(自动模式)");
        lists.add("强制版本更新");
        lists.add("默认App更新 + 自定义提示弹窗样式");
        lists.add("默认App更新 + 自定义Api");
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
                        .updateUrl(mUpdateUrl)
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
            default:
                break;
        }
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
