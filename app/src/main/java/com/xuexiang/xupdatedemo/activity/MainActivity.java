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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xupdatedemo.fragment.MainFragment;
import com.xuexiang.xupdatedemo.utils.NotifyUtils;

public class MainActivity extends XPageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openPage(MainFragment.class);

        if (!NotifyUtils.isNotifyPermissionOpen(this)) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("通知权限未打开，是否前去打开？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int w) {
                            NotifyUtils.openNotifyPermissionSetting(MainActivity.this);
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
    }
}
