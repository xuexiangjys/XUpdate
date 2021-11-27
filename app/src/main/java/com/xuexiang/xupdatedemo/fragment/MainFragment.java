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

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xupdatedemo.activity.UpdateActivity;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;

import java.util.List;
import java.util.Locale;

/**
 * @author xuexiang
 * @since 2018/7/9 下午2:20
 */
@Page(name = "XUpdate 版本更新", anim = CoreAnim.none)
public class MainFragment extends XPageSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("获取安装文件的MD5值");
        lists.add("基础使用（傻瓜式一键更新）");
        lists.add("进阶使用（自定义）");
        lists.add("版本更新提示框在FragmentActivity中使用UpdateDialogFragment, 在普通Activity中使用UpdateDialog");
        lists.add("使用XUpdateService版本更新服务");
        lists.add("调试测试");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                openPage(FileMD5Fragment.class);
                break;
            case 1:
                openPage(BasicUseFragment.class);
                break;
            case 2:
                openPage(AdvancedUseFragment.class);
                break;
            case 3:
                startActivity(new Intent(getContext(), UpdateActivity.class));
                break;
            case 4:
                openPage(XUpdateServiceFragment.class);
                break;
            case 5:
                openPage(TestFragment.class);
                break;
            default:
                break;
        }
    }

    private boolean mIsSimplifiedChinese;

    private TextView mAction;

    @Override
    protected TitleBar initTitleBar() {
        TitleBar titleBar = super.initTitleBar().setLeftClickListener(view -> ClickUtils.exitBy2Click());
        mAction = (TextView) titleBar.addAction(new TitleBar.TextAction(getLanguageTitle()) {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (mIsSimplifiedChinese) {
                    changeLocale(Locale.getDefault());
                } else {
                    changeLocale(Locale.SIMPLIFIED_CHINESE);
                }
                mIsSimplifiedChinese = !mIsSimplifiedChinese;
                mAction.setText(getLanguageTitle());

            }
        });
        return titleBar;
    }

    private String getLanguageTitle() {
        return String.format("语言:%s", mIsSimplifiedChinese ? "简体中文" : "系统默认");
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

    @Override
    protected void initArgs() {
        Resources resource = getResources();
        Configuration config = resource.getConfiguration();
        mIsSimplifiedChinese = config.locale == Locale.SIMPLIFIED_CHINESE;
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
