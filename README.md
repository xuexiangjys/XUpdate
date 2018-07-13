# XUpdate
[![xu][xusvg]][xu]  [![api][apisvg]][api]

一个轻量级、高可用性的Android版本更新框架

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)

## 特点

* 支持post和get两种版本检查方式，支持自定义网络请求。

* 支持设置只在wifi下进行版本更新。

* 支持静默下载、自动版本更新。

* 提供界面友好的版本更新提示弹窗，可自定义其主题样式。

* 支持自定义版本更新检查器、版本更新解析器、版本更新提示器、版本更新下载器、版本更新安装、出错处理。

* 支持MD5文件校验、版本忽略、版本强制更新等功能。

* 支持自定义请求API接口。

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

## 1、演示（请star支持）

* 默认版本更新

![](https://github.com/xuexiangjys/XUpdate/blob/master/img/update_1.png)

* 强制版本更新

![](https://github.com/xuexiangjys/XUpdate/blob/master/img/update_2.png)

* 自定义提示弹窗主题

![](https://github.com/xuexiangjys/XUpdate/blob/master/img/update_3.png)

* 使用系统弹窗提示

![](https://github.com/xuexiangjys/XUpdate/blob/master/img/update_4.png)

### Demo下载

[![downloads](https://img.shields.io/badge/downloads-1.6M-blue.svg)](https://github.com/xuexiangjys/XUpdate/blob/master/apk/xupdate_demo_1.0.apk)

![](https://github.com/xuexiangjys/XUpdate/blob/master/img/download.png)


## 2、如何使用
目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

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

```
dependencies {
  ...
  implementation 'com.github.xuexiangjys:XUpdate:1.0.0'
}
```

### 2.2、初始化XUpdate

在Application进行初始化配置：

```
XUpdate.get()
        .isWifiOnly(true)     //默认设置只在wifi下检查版本更新
        .isGet(true)          //默认设置使用get请求检查版本
        .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
        .param("VersionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
        .param("AppKey", getPackageName())
//                .debug(true)
        .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
            @Override
            public void onFailure(UpdateError error) {
                ToastUtils.toast(error.toString());
            }
        })
        .setIUpdateHttpService(new OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
        .init(this);   //这个必须初始化
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
mMd5 | String | "" | 下载文件的md5值，用于校验，防止下载的apk文件被替换
mSize | long | 0 | 下载文件的大小【单位：KB】
mIsShowNotification | boolean | false | 是否在通知栏上显示下载进度

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
  "Code": 0,
  "Msg": "",
  "UpdateStatus": 1,
  "VersionCode": 3,
  "VersionName": "1.0.2",
  "ModifyContent": "1、优化api接口。\r\n2、添加使用demo演示。\r\n3、新增自定义更新服务API接口。\r\n4、优化更新提示界面。",
  "DownloadUrl": "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/apk/xupdate_demo_1.0.2.apk",
  "ApkSize": 2048
  "ApkMd5": "..."  //md5值没有的话，就无法保证apk是否完整，每次都会重新下载。
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

### 3.3、强制版本更新

就是用户不更新的话，程序将无法正常使用。只需要服务端返回`UpdateStatus`字段为2即可。

当然如果你自定义请求返回api的话，只需要设置`UpdateEntity`的`mIsForce`字段为true即可。

### 3.4、自定义版本更新提示弹窗的主题

可设置弹窗的标题背景和按钮颜色。

* themeColor: 设置主题颜色（升级/安装按钮的背景色）
* topResId: 弹窗的标题背景的资源图片

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .themeColor(ResUtils.getColor(R.color.update_theme_color))
        .topResId(R.mipmap.bg_update_top)
        .update();
```

### 3.5、自定义版本更新解析器

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

### 3.6、自定义版本更新检查器+版本更新解析器+版本更新提示器

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
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy) {
        showUpdatePrompt(updateEntity, updateProxy);
    }

    /**
     * 显示自定义提示
     *
     * @param updateEntity
     * @param updateProxy
     */
    private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(updateEntity);

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

### 3.7、只使用XUpdate的下载器功能进行apk的下载

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

### 3.8、只使用XUpdate的APK安装的功能

```
_XUpdate.startInstallApk(getContext(), FileUtils.getFileByPath(PathUtils.getFilePathByUri(getContext(), data.getData()))); //填写文件所在的路径
```

如果你的apk安装与众不同，你可以实现自己的apk安装器。你只需要实现OnInstallListener接口，并通过`XUpdate.setOnInstallListener`进行设置即可生效。


## 混淆配置

```
-keep class com.xuexiang.xupdate.entity.** { *; }
```

## 特别感谢
https://github.com/WVector/AppUpdate

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ交流群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)

[xusvg]: https://img.shields.io/badge/XUpdate-v1.0.0-brightgreen.svg
[xu]: https://github.com/xuexiangjys/XUpdate
[apisvg]: https://img.shields.io/badge/API-19+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=19