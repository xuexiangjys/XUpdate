package com.xuexiang.xupdatedemo;

import com.xuexiang.xupdate.entity.CheckVersionResult;
import com.xuexiang.xupdate.utils.Md5Utils;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.net.JsonUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        CheckVersionResult result = new CheckVersionResult();
        result.setCode(0)
                .setMsg("")
                .setRequireUpgrade(CheckVersionResult.HAVE_NEW_VERSION)
//                .setApkMd5(Md5Utils.getFileMD5())
                .setApkSize(2 * 1024) //2M = 2 * 1024 K
                .setVersionCode(3)
                .setVersionName("1.0.2")
                .setDownloadUrl("https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/apk/xupdate_demo_1.0.2.apk")
                .setUploadTime(DateUtils.getNowString(DateUtils.yyyyMMddHHmmss.get()))
                .setModifyContent("1、优化api接口。\r\n2、添加使用demo演示。\r\n3、新增自定义更新服务API接口。\r\n4、优化更新提示界面。");

        System.out.println(JsonUtil.toJson(result));
    }
}