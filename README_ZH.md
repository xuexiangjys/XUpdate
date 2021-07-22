# XUpdate

[![](https://jitpack.io/v/xuexiangjys/XUpdate.svg)](https://jitpack.io/#xuexiangjys/XUpdate)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XUpdate.svg)](https://github.com/xuexiangjys/XUpdate/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XUpdate.svg)](https://github.com/xuexiangjys/XUpdate)

## [英文](./README.md) | [中文](./README_ZH.md) ｜ [视频教程](https://space.bilibili.com/483850585/channel/detail?cid=164280)

一个轻量级、高可用性的Android版本更新框架。赶紧点击[使用说明文档](https://github.com/xuexiangjys/XUpdate/wiki)，体验一下吧！

在提issue前，请先阅读[【提问的智慧】](https://xuexiangjys.blog.csdn.net/article/details/83344235)，并严格按照[issue模板](https://github.com/xuexiangjys/XUpdate/issues/new/choose)进行填写，节约大家的时间。

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUpdate/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUpdate/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XUpdate/wiki),重要的事情说三遍！！！

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![简书](https://img.shields.io/badge/简书-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![掘金](https://img.shields.io/badge/掘金-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![知乎](https://img.shields.io/badge/知乎-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys)

## 简化使用

想要更快地使用XUpdate，降低集成的难度，支持断点续传下载等拓展功能，可以尝试使用[XUpdateAPI](https://github.com/xuexiangjys/XUpdateAPI).

## X系列库快速集成

为了方便大家快速集成X系列框架库，我提供了一个空壳模版供大家参考使用: https://github.com/xuexiangjys/TemplateAppProject

---

## 特征

* 支持post和get两种版本检查方式，支持自定义网络请求。

* 支持设置只在wifi下进行版本更新。

* 支持静默下载（后台更新）、自动版本更新。

* 提供界面友好的版本更新提示弹窗，可自定义其主题样式。

* 支持自定义版本更新检查器、版本更新解析器、版本更新提示器、版本更新下载器、版本更新安装、出错处理。

* 支持MD5文件校验、版本忽略、版本强制更新等功能。

* 支持自定义文件校验方法【默认是MD5校验】。

* 支持自定义请求API接口。

* 兼容Android6.0～11.0。

* 支持中文和英文两种语言显示（国际化）。

* 支持Flutter插件使用：[flutter_xupdate](https://github.com/xuexiangjys/flutter_xupdate)。

* 支持React-Native插件使用：[react-native-xupdate](https://github.com/xuexiangjys/react-native-xupdate)。

## Star趋势图

[![Stargazers over time](https://starchart.cc/xuexiangjys/XUpdate.svg)](https://starchart.cc/xuexiangjys/XUpdate)

## 组成结构

本框架借鉴了[AppUpdate](https://github.com/WVector/AppUpdate)中的部分思想和UI界面，将版本更新中的各部分环节抽离出来，形成了如下几个部分：

* 版本更新检查器`IUpdateChecker`：检查是否有最新版本。

* 版本更新解析器`IUpdateParser`：解析服务端返回的数据结果。

* 版本更新提示器`IUpdatePrompter`：展示最新的版本信息。

* 版本更新下载器`IUpdateDownloader`：下载最新的版本APK安装包。

* 网络请求服务接口`IUpdateHttpService`：定义了进行网络请求的相关接口。

除此之外，还有两个监听器：

* 版本更新失败的监听器`OnUpdateFailureListener`。

* 版本更新apk安装的监听器`OnInstallListener`。

更新调度核心：

* 版本更新业务代理`IUpdateProxy`：负责版本更新的流程控制，调用update开始进行版本更新流程。

## 更新流程

调用update之后的流程：

```
IUpdateProxy/XUpdate --- (update) ---> IUpdateChecker --->（请求服务器，获取最新版本信息）---> IUpdateParser ---> (解析服务器返回的数据，并构建UpdateEntity版本更新实体）---> IUpdateProxy ---> (如无最新版本，直接结束，否则进行下面流程）

    ---自动模式---> IUpdateDownloader ---> （下载最新的应用apk） ---> 安装应用

    ---非自动模式---> IUpdatePrompter ---> 给出版本更新的提示

                                                        ---> 用户点击更新 ---> IUpdateDownloader ---> （下载最新的应用apk） ---> 跳转到应用安装界面

                                                        ---> 用户点击（取消或忽略） ---> 结束
```

[点击查看框架UML设计图](https://github.com/xuexiangjys/XUpdate/blob/master/img/xupdate_uml.png)

---

## 1、演示（请star支持）

* 默认版本更新

![xupdate_default.png](https://ss.im5i.com/2021/06/14/6TPSz.png)

* 后台更新

![xupdate_background.png](https://ss.im5i.com/2021/06/14/6TgXW.png)

* 强制版本更新

![xupdate_force.png](https://ss.im5i.com/2021/06/14/6Tlw8.png)

* 可忽略版本更新

![xupdate_ignore.png](https://ss.im5i.com/2021/06/14/6TVu5.png)

* 自定义提示弹窗主题

![xupdate_custom.png](https://ss.im5i.com/2021/06/14/6TGDG.png)

* 使用系统弹窗提示

![xupdate_system.png](https://ss.im5i.com/2021/06/14/6Td86.png)

### Demo更新后台服务

由于github最近访问比较慢，如果需要更好地体验XUpdate，你可以[点击自己搭建一个简易的版本更新服务](https://github.com/xuexiangjys/XUpdateService)。

### Demo下载

#### 蒲公英下载

> 蒲公英下载的密码: xuexiangjys

[![downloads](https://img.shields.io/badge/downloads-2.1M-blue.svg)](https://www.pgyer.com/xupdate)

[![xupdate_download_pugongying.png](https://ss.im5i.com/2021/06/14/6jaJj.png)](https://www.pgyer.com/xupdate)

#### Github下载

[![downloads](https://img.shields.io/badge/downloads-2.1M-blue.svg)](https://github.com/xuexiangjys/XUpdate/blob/master/apk/xupdate_demo_1.0.apk?raw=true)

[![xupdate_download.png](https://ss.im5i.com/2021/06/14/6jDhD.png)](https://github.com/xuexiangjys/XUpdate/blob/master/apk/xupdate_demo_1.0.apk?raw=true)

---

## 2、快速集成指南

> 目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、Android Studio导入方法，添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在dependencies添加:

以下是版本说明，选择一个即可。

* androidx版本：2.0.0及以上

```
dependencies {
  ...
  // androidx版本
  implementation 'com.github.xuexiangjys:XUpdate:2.0.9'
}
```

* support版本：1.1.6及以下

```
dependencies {
  ...
  // support版本
  implementation 'com.github.xuexiangjys:XUpdate:1.1.6'
}
```

### 2.2、初始化XUpdate

在Application进行初始化配置：

```
XUpdate.get()
    .debug(true)
    .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
    .isGet(true)                                                    //默认设置使用get请求检查版本
    .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
    .param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
    .param("appKey", getPackageName())
    .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
        @Override
        public void onFailure(UpdateError error) {
            if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                ToastUtils.toast(error.toString());
            }
        }
    })
    .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
    .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
    .init(this);                                                    //这个必须初始化
```

【注意】：如果出现任何问题，可开启debug模式来追踪问题。如果你还需要将日志记录在磁盘上，可实现以下接口

```
XUpdate.get().setILogger(new ILogger() {
    @Override
    public void log(int priority, String tag, String message, Throwable t) {
        //实现日志记录功能
    }
});
```

### 2.3、版本更新实体信息

(1) UpdateEntity字段属性

字段名 | 类型 | 默认值 | 备注
:-|:-:|:-:|:-
mHasUpdate | boolean | false | 是否有新版本
mIsForce | boolean | false | 是否强制安装：不安装无法使用app
mIsIgnorable | boolean | false | 是否可忽略该版本
mVersionCode | int | 0 | 最新版本code
mVersionName | String | unknown_version | 最新版本名称
mUpdateContent | String | "" | 更新内容
mDownloadEntity | DownloadEntity | ／ | 下载信息实体
mIsSilent | boolean | false | 是否静默下载：有新版本时不提示直接下载
mIsAutoInstall | boolean | true | 是否下载完成后自动安装

(2) DownloadEntity字段属性

字段名 | 类型 | 默认值 | 备注
:-|:-:|:-:|:-
mDownloadUrl | String | "" | 下载地址
mCacheDir | String | "" | 文件下载的目录
mMd5 | String | "" | 下载文件的加密校验值(默认使用md5加密)，用于校验，防止下载的apk文件被替换（最新演示demo中有计算校验值的工具），注意这里的md5值非应用签名文件的md5值！
mSize | long | 0 | 下载文件的大小【单位：KB】
mIsShowNotification | boolean | false | 是否在通知栏上显示下载进度

(3) PromptEntity字段属性

字段名 | 类型 | 默认值 | 备注
:-|:-:|:-:|:-
mThemeColor | int | R.color.xupdate_default_theme_color | 主题色（进度条和按钮的背景色）
mTopResId | int | R.drawable.xupdate_bg_app_top | 顶部背景图片资源id
mButtonTextColor | int | 0 | 按钮文字颜色
mSupportBackgroundUpdate | boolean | false | 是否支持后台更新
mWidthRatio | float | -1（无约束） | 版本更新提示器宽度占屏幕的比例
mHeightRatio | float | -1（无约束） | 版本更新提示器高度占屏幕的比例

### 2.4、文件加密校验方式

本框架默认使用的文件加密校验方法是MD5加密方式，当然如果你不想使用MD5加密，你也可以自定义文件加密器`IFileEncryptor`,以下是MD5文件加密器的实现供参考：

```
/**
 * 默认的文件加密计算使用的是MD5加密
 *
 * @author xuexiang
 * @since 2019-09-06 14:21
 */
public class DefaultFileEncryptor implements IFileEncryptor {
    /**
     * 加密文件
     *
     * @param file
     * @return
     */
    @Override
    public String encryptFile(File file) {
        return Md5Utils.getFileMD5(file);
    }

    /**
     * 检验文件是否有效（加密是否一致）
     *
     * @param encrypt 加密值, 如果encrypt为空，直接认为是有效的
     * @param file    需要校验的文件
     * @return 文件是否有效
     */
    @Override
    public boolean isFileValid(String encrypt, File file) {
        return TextUtils.isEmpty(encrypt) || encrypt.equalsIgnoreCase(encryptFile(file));
    }
}

```
最后再调用`XUpdate.get().setIFileEncryptor`方法设置即可生效。

---

## 3、版本更新

### 3.1、默认版本更新

直接调用如下代码即可完成版本更新操作：

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .update();
```
需要注意的是，使用默认版本更新，请求服务器返回的json格式应包括如下内容：

```
{
  "Code": 0, //0代表请求成功，非0代表失败
  "Msg": "", //请求出错的信息
  "UpdateStatus": 1, //0代表不更新，1代表有版本更新，不需要强制升级，2代表有版本更新，需要强制升级
  "VersionCode": 3,
  "VersionName": "1.0.2",
  "ModifyContent": "1、优化api接口。\r\n2、添加使用demo演示。\r\n3、新增自定义更新服务API接口。\r\n4、优化更新提示界面。",
  "DownloadUrl": "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/apk/xupdate_demo_1.0.2.apk",
  "ApkSize": 2048
  "ApkMd5": "..."  //应用apk的md5值没有的话，就无法保证apk是否完整，每次都会重新下载。框架默认使用的是md5加密。
}
```

### 3.2、自动版本更新

自动版本更新：自动检查版本 + 自动下载apk + 自动安装apk（静默安装）。
只需要设置`isAutoMode(true)`,不过如果设备没有root权限的话，是无法做到完全的自动更新（因为静默安装需要root权限）。

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .isAutoMode(true) //如果需要完全无人干预，自动更新，需要root权限【静默安装需要】
        .update();
```

### 3.3、支持后台更新

开启支持后台更新后, 用户点击“后台更新”按钮后,就可以进入到后台更新,不用一直在更新界面等待.

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .supportBackgroundUpdate(true)
        .update();
```

### 3.4、强制版本更新

就是用户不更新的话，程序将无法正常使用。只需要服务端返回`UpdateStatus`字段为2即可。

当然如果你自定义请求返回api的话，只需要设置`UpdateEntity`的`mIsForce`字段为true即可。

### 3.5、自定义版本更新提示弹窗的主题

通过设置更新顶部图片、主题色、按钮文字颜色、宽高比率等来实现自定义主题样式.

* promptThemeColor: 设置主题颜色
* promptButtonTextColor: 设置按钮的文字颜色
* promptTopResId: 设置顶部背景图片
* promptWidthRatio: 设置版本更新提示器宽度占屏幕的比例，默认是-1，不做约束
* promptHeightRatio: 设置版本更新提示器高度占屏幕的比例，默认是-1，不做约束

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .promptThemeColor(ResUtils.getColor(R.color.update_theme_color))
        .promptButtonTextColor(Color.WHITE)
        .promptTopResId(R.mipmap.bg_update_top)
        .promptWidthRatio(0.7F)
        .update();
```

### 3.6、自定义版本更新解析器

实现IUpdateParser接口即可实现解析器的自定义。

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl3)
        .updateParser(new CustomUpdateParser()) //设置自定义的版本更新解析器
        .update();

public class CustomUpdateParser implements IUpdateParser {
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
}

```

### 3.7、自定义版本更新检查器+版本更新解析器+版本更新提示器

* 实现`IUpdateChecker`接口即可实现检查器的自定义。

* 实现`IUpdateParser`接口即可实现解析器的自定义。

* 实现`IUpdatePrompter`接口即可实现提示器的自定义。

```
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
        })
        .updateParser(new CustomUpdateParser())
        .updatePrompter(new CustomUpdatePrompter(getActivity()))
        .update();


public class CustomUpdatePrompter implements IUpdatePrompter {

    private Context mContext;

    public CustomUpdatePrompter(Context context) {
        mContext = context;
    }

    @Override
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
        showUpdatePrompt(updateEntity, updateProxy);
    }

    /**
     * 显示自定义提示
     *
     * @param updateEntity
     * @param updateProxy
     */
    private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(mContext, updateEntity);

        new AlertDialog.Builder(mContext)
                .setTitle(String.format("是否升级到%s版本？", updateEntity.getVersionName()))
                .setMessage(updateInfo)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(mContext, "下载进度", false);
                            }

                            @Override
                            public void onProgress(float progress, long total) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                            }

                            @Override
                            public boolean onCompleted(File file) {
                                HProgressDialogUtils.cancel();
                                return true;
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                HProgressDialogUtils.cancel();
                            }
                        });
                    }
                })
                .setNegativeButton("暂不升级", null)
                .setCancelable(false)
                .create()
                .show();
    }
```

### 3.8、只使用XUpdate的下载器功能进行apk的下载

```
XUpdate.newBuild(getActivity())
        .apkCacheDir(PathUtils.getExtDownloadsPath()) //设置下载缓存的根目录
        .build()
        .download(mDownloadUrl, new OnFileDownloadListener() {   //设置下载的地址和下载的监听
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
```

### 3.9、只使用XUpdate的APK安装的功能

```
_XUpdate.startInstallApk(getContext(), FileUtils.getFileByPath(PathUtils.getFilePathByUri(getContext(), data.getData()))); //填写文件所在的路径
```

如果你的apk安装与众不同，你可以实现自己的apk安装器。你只需要实现OnInstallListener接口，并通过`XUpdate.setOnInstallListener`进行设置即可生效。

### 3.10、国际化配置

由于作者水平有限, 目前只支持中文和英文两种语言的显示【默认语言是英语】。需要支持其他语言的，只需要在自己项目的`res`下新建对应的语言文件，进行多语言的翻译即可。

需要翻译的详细内容参见 ***[xupdate_strings.xml](https://github.com/xuexiangjys/XUpdate/blob/master/xupdate-lib/src/main/res/values-zh-rCN/xupdate_strings.xml)*** 文件。

什么？你还不会Android的多语言配置？建议你看一下这篇文章：[Android项目国际化多国语言适配](https://blog.csdn.net/qq_29769851/article/details/90606437)

## 混淆配置

```
-keep class com.xuexiang.xupdate.entity.** { *; }

//注意，如果你使用的是自定义Api解析器解析，还需要给你自定义Api实体配上混淆，如下是本demo中配置的自定义Api实体混淆规则：
-keep class com.xuexiang.xupdatedemo.entity.** { *; }

```

## 相关链接

* [XUpdate 文档](https://github.com/xuexiangjys/XUpdate/wiki)
* [XUpdate 简化库](https://github.com/xuexiangjys/XUpdateAPI)
* [XUpdate 管理服务](https://github.com/xuexiangjys/XUpdateService)
* [XUpdate 后台管理系统](https://github.com/xuexiangjys/xupdate-management)
* [XUpdate Flutter插件](https://github.com/xuexiangjys/flutter_xupdate)
* [XUpdate React-Native插件](https://github.com/xuexiangjys/react-native-xupdate)
* [Flutter版本更新弹窗组件](https://github.com/xuexiangjys/flutter_update_dialog)

---


## 特别感谢

https://github.com/WVector/AppUpdate

## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![pay.png](https://ss.im5i.com/2021/06/14/6twG6.png)

感谢下面小伙伴的打赏：

姓名 | 金额 | 方式
:-|:-|:-
*天 | 100￥ | 微信
*航 | 10￥ | 支付宝
X*? | 18.88￥ | 微信
*网 | 1￥ | 微信
Joe | 88.88￥ | 微信

## 公众号

> 更多资讯内容，欢迎扫描关注我的个人微信公众号:【我的Android开源之旅】

![](https://ss.im5i.com/2021/06/14/65yoL.jpg)

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ交流群-720212425-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=37ea606864cafa3c8a5d6b07f04fd672936a50fd6e535c13702baf705d57e6e8)

![xupdate_qq.jpg](https://ss.im5i.com/2021/06/14/6TQuQ.jpg)

