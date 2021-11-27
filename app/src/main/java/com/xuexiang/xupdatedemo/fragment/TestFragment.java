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

import android.os.Handler;
import android.os.Looper;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdatedemo.Constants;
import com.xuexiang.xupdatedemo.utils.StatusBarUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2021/11/26 5:08 PM
 */
@Page(name = "调试测试")
public class TestFragment extends XPageSimpleListFragment {

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private boolean mIsFullScreen;

    @Override
    protected void initArgs() {
        mIsFullScreen = StatusBarUtils.isFullScreen(getActivity());
    }

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("多次执行版本更新检查");
        lists.add("定期执行版本更新检查");
        lists.add("全屏/非全屏显示");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                for (int i = 0; i < 10; i++) {
                    checkUpdate();
                }
                break;
            case 1:
                for (int i = 0; i < 5; i++) {
                    mMainHandler.postDelayed(this::checkUpdate, i * 5000);
                }
                break;
            case 2:
                if (mIsFullScreen) {
                    StatusBarUtils.cancelFullScreen(getActivity());
                } else {
                    StatusBarUtils.fullScreen(getActivity());
                }
                mIsFullScreen = !mIsFullScreen;
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
        XUpdate.newBuild(getActivity())
                .updateUrl(Constants.DEFAULT_UPDATE_URL)
                .update();
    }

    @Override
    public void onDestroyView() {
        mMainHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

}
