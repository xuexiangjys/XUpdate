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

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdatedemo.R;
import com.xuexiang.xupdatedemo.custom.XUpdateServiceParser;
import com.xuexiang.xupdatedemo.utils.SettingSPUtils;
import com.xuexiang.xutil.net.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.HttpUrl;

/**
 * @author xuexiang
 * @since 2018/7/30 上午11:39
 */
@Page(name = "版本更新服务")
public class XUpdateServiceFragment extends XPageFragment {

    @BindView(R.id.et_service_url)
    EditText mEtServiceUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_xupdate_service;
    }

    @Override
    protected void initViews() {
        mEtServiceUrl.setText(SettingSPUtils.get().getServiceURL());
    }

    @Override
    protected void initListeners() {

    }

    @OnClick({R.id.btn_save, R.id.btn_update, R.id.btn_auto_update, R.id.btn_force_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String url = mEtServiceUrl.getText().toString().trim();
                if (NetworkUtils.isUrlValid(url) && parseBaseUrl(url)) {
                    SettingSPUtils.get().setServiceURL(url);
                }
                break;
            case R.id.btn_update:
                XUpdate.newBuild(getContext())
                        .isGet(false)
                        .updateUrl(XUpdateServiceParser.getVersionCheckUrl())
                        .updateParser(new XUpdateServiceParser())
                        .update();
                break;
            case R.id.btn_auto_update:
                XUpdate.newBuild(getContext())
                        .isGet(false)
                        .updateUrl(XUpdateServiceParser.getVersionCheckUrl())
                        .updateParser(new XUpdateServiceParser())
                        .isAutoMode(true) //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                        .update();
                break;
            case R.id.btn_force_update:
                XUpdate.newBuild(getContext())
                        .isGet(false)
                        .param("appKey", "test3")
                        .updateUrl(XUpdateServiceParser.getVersionCheckUrl())
                        .updateParser(new XUpdateServiceParser())
                        .update();
                break;
        }
    }


    /**
     * 解析baseUrl
     *
     * @param baseUrl
     * @return true: 设置baseUrl成功
     */
    public static boolean parseBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) {
            HttpUrl httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl != null) {
                List<String> pathSegments = httpUrl.pathSegments();
                return "".equals(pathSegments.get(pathSegments.size() - 1));
            }
        }
        return false;
    }

}
