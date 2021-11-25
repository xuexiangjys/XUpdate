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

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xupdate.XUpdate;

import java.util.List;

/**
 * @author xuexiang
 * @since 2021/11/25 10:32 PM
 */
@Page(name = "基础使用")
public class BasicUseFragment extends XPageSimpleListFragment {

    private String mUpdateUrl = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_test.json";

    private String mUpdateUrl2 = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_forced.json";

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("默认App更新");
        lists.add("默认App更新 + 支持后台更新");
        lists.add("版本更新(自动模式)");
        lists.add("强制版本更新");
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
                        .supportBackgroundUpdate(true)
                        .update();
                break;
            case 2:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl)
                        //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                        .isAutoMode(true)
                        .update();
                break;
            case 3:
                XUpdate.newBuild(getActivity())
                        .updateUrl(mUpdateUrl2)
                        .update();
                break;
            default:
                break;
        }
    }

}
