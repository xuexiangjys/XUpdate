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

package com.xuexiang.xupdatedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdatedemo.R;

/**
 * @author xuexiang
 * @since 2018/7/24 上午10:38
 */
public class UpdateActivity extends Activity implements View.OnClickListener{

    private String mUpdateUrl = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_test.json";
    private String mUpdateUrl2 = "https://gitee.com/xuexiangjys/XUpdate/raw/master/jsonapi/update_forced.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_update:
                XUpdate.newBuild(this)
                        .updateUrl(mUpdateUrl)
                        .update();
                break;
            case R.id.btn_support_background_update:
                XUpdate.newBuild(this)
                        .updateUrl(mUpdateUrl)
                        .promptWidthRatio(0.7F)
                        .supportBackgroundUpdate(true)
                        .update();
                break;
            case R.id.btn_auto_update:
                XUpdate.newBuild(this)
                        .updateUrl(mUpdateUrl)
                        //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
                        .isAutoMode(true)
                        .update();
                break;
            case R.id.btn_force_update:
                XUpdate.newBuild(this)
                        .updateUrl(mUpdateUrl2)
                        .update();
                break;
            default:
                break;
        }
    }
}
