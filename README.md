# XUpdate

[![](https://jitpack.io/v/xuexiangjys/XUpdate.svg)](https://jitpack.io/#xuexiangjys/XUpdate)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XUpdate.svg)](https://github.com/xuexiangjys/XUpdate/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XUpdate.svg)](https://github.com/xuexiangjys/XUpdate)

## [English](./README.md) | [Chinese](./README_ZH.md) ｜ [Video tutorial](https://space.bilibili.com/483850585/channel/detail?cid=164280)

A lightweight, high availability Android version update framework. Click [instruction document](https://github.com/xuexiangjys/XUpdate/wiki) to experience it！

Please read [【wisdom of asking questions】](https://xuexiangjys.blog.csdn.net/article/details/83344235) before raising the issue and strictly follow the [issue template](https://github.com/xuexiangjys/XUpdate/issues/new/choose) fill in and save everyone's time.

Please read the [instruction document](https://github.com/xuexiangjys/XUpdate/wiki) carefully before use, important things are to be repeated for three time!!！

Please read the [instruction document](https://github.com/xuexiangjys/XUpdate/wiki) carefully before use, important things are to be repeated for three time!!！

Please read the [instruction document](https://github.com/xuexiangjys/XUpdate/wiki) carefully before use, important things are to be repeated for three time!!！

## About me

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![jianshu](https://img.shields.io/badge/jianshu-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![juejin](https://img.shields.io/badge/juejin-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![zhihu](https://img.shields.io/badge/zhihu-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys)

## Simplify use

If you want to use xupdate faster, reduce the difficulty of integration, support breakpoint continuation download and other expansion functions, you can try to use [XUpdateAPI](https://github.com/xuexiangjys/XUpdateAPI).

## Rapid integration of X-Library

In order to facilitate the rapid integration of X-Library, I provide a template project for your reference: [https://github.com/xuexiangjys/TemplateAppProject](https://github.com/xuexiangjys/TemplateAppProject)

---

## Features

* It supports `Post` or `Get` request to check version, and supports user-defined network requests.

* Only version update under WiFi is supported。

* Support silent download (background update) and automatic version update.

* The user-friendly version update prompt pop-up window is provided to customize the theme style.

* It supports user-defined version update checker, parser, prompter, downloader, install listener and error listener.

* Support MD5 file verification, version ignore, version forced update and other functions.

* It supports the user-defined file verification method【MD5 verification by default】.

* Support custom request API interface.

* Compatible with Android 6.0, 7.0, 8.0, 9.0 and 10.0.

* Support Chinese and English language (internationalization).

* Support the use of the flutter plugin：[flutter_xupdate](https://github.com/xuexiangjys/flutter_xupdate)。

* Support the use of react native plugin：[react-native-xupdate](https://github.com/xuexiangjys/react-native-xupdate)。

## Composition structure

This framework refers to [AppUpdate](https://github.com/WVector/AppUpdate) some ideas and UI, the various parts of the version update are separated to form the following parts:

* `IUpdateChecker`: Check for the latest version.

* `IUpdateParser`: Parsing the data results returned by the server.

* `IUpdatePrompter`: Display the latest version information.

* `IUpdateDownloader`: Download the latest version of APK installation package.

* `IUpdateHttpService`: The interface for network request.

In addition, there are two listeners:

* `OnUpdateFailureListener`: Listening error

* `OnInstallListener`: Callback of application installation

Update core manager:

* `IUpdateProxy`: Responsible for the process control of version update, calling update to start the version update process.

## Update process

Process after calling `update`:

```
IUpdateProxy/XUpdate --- (update) ---> IUpdateChecker --->（Request the server to get the latest version information）---> IUpdateParser ---> (Parse the data returned by the server, and build the UpdateEntity）---> IUpdateProxy ---> (If there is no latest version, end it directly, otherwise proceed to the following process）

    ---Automatic mode---> IUpdateDownloader ---> （Download the latest app apk） ---> Install application

    ---Non automatic mode---> IUpdatePrompter ---> Prompt for version update

                                                        ---> click Update ---> IUpdateDownloader ---> （Download the latest app apk） ---> Jump to application installation UI

                                                        ---> Click cancel or ignore ---> End
```

[Click to view the framework UML design diagram](https://github.com/xuexiangjys/XUpdate/blob/master/img/xupdate_uml.png)

---

## 1、Demonstration

* Default version update

![xupdate_default.png](https://img.rruu.net/image/5f9d7b77af102)

* Background update

![xupdate_background.png](https://img.rruu.net/image/5f9d7b769901a)

* Force version update

![xupdate_force.png](https://img.rruu.net/image/5f9d7b774b990)

* Ignored version update

![xupdate_ignore.png](https://img.rruu.net/image/5f9d7b77ad3be)

* Custom pop up theme

![xupdate_custom.png](https://img.rruu.net/image/5f9d7b76cee50)

* Use the system pop-up prompt

![xupdate_system.png](https://img.rruu.net/image/5f9d7b77ab42f)

### Demo update background service

Because GitHub is slow to visit recently, if you need to experience xupdate better, you can [Click to build a simple version update service](https://github.com/xuexiangjys/XUpdateService).

### Demo Download

#### Pgyer Download

> Pgyer Download password: xuexiangjys

[![downloads](https://img.shields.io/badge/downloads-2.1M-blue.svg)](https://www.pgyer.com/xupdate)

![xupdate_download_pugongying.png](https://img.rruu.net/image/5f7d94983032e)

#### GitHub Download

[![downloads](https://img.shields.io/badge/downloads-2.1M-blue.svg)](https://github.com/xuexiangjys/XUpdate/blob/master/apk/xupdate_demo_1.0.apk?raw=true)

![xupdate_download.png](https://img.rruu.net/image/5f7d949839379)

---

## 2、Quick integration guide

> At present, it supports the use of the mainstream development tool AndroidStudio and add dependency by configures `build.gradle` directly.

### 2.1、Add gradle dependency

1.In the project root directory `build.gradle`:

```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.Then, in the dependencies of the project:

The following is the version description. Choose one.

* androidx project: above 2.0.0

```
dependencies {
  ...
  // androidx project
  implementation 'com.github.xuexiangjys:XUpdate:2.0.7'
}
```

* support project: below 1.1.6

```
dependencies {
  ...
  // support project
  implementation 'com.github.xuexiangjys:XUpdate:1.1.6'
}
```

### 2.2、Initialization

Initialize settings at the top of the application:

```
XUpdate.get()
    .debug(true)
    .isWifiOnly(true)                                               // By default, only version updates are checked under WiFi
    .isGet(true)                                                    // The default setting uses Get request to check versions
    .isAutoMode(false)                                              // The default setting is non automatic mode
    .param("versionCode", UpdateUtils.getVersionCode(this))         // Set default public request parameters
    .param("appKey", getPackageName())
    .setOnUpdateFailureListener(new OnUpdateFailureListener() {     // Set listening for version update errors
        @Override
        public void onFailure(UpdateError error) {
            if (error.getCode() != CHECK_NO_NEW_VERSION) {          // Handling different errors
                ToastUtils.toast(error.toString());
            }
        }
    })
    .supportSilentInstall(true)                                     // Set whether silent installation is supported. The default is true
    .setIUpdateHttpService(new OKHttpUpdateHttpService())           // This must be set! Realize the network request function.
    .init(this);                                                    // This must be initialized
```

【note】: if there is any problem, you can open debug mode to track the problem. If you also need to log on disk, you can implement the following interface.

```
XUpdate.get().setILogger(new ILogger() {
    @Override
    public void log(int priority, String tag, String message, Throwable t) {
        // Realize the function of logging
    }
});
```

### 2.3、Version update entity information

(1) UpdateEntity

Field name | Type | Default value | Description
:-|:-:|:-:|:-
mHasUpdate | boolean | false | Whether have the latest version
mIsForce | boolean | false | Force installation: app cannot be used without installation
mIsIgnorable | boolean | false | Whether the version can be ignored
mVersionCode | int | 0 | Latest version code
mVersionName | String | unknown_version | Latest version name
mUpdateContent | String | "" | Update content
mDownloadEntity | DownloadEntity | ／ | Download information entity
mIsSilent | boolean | false | Whether to download silently: when there is a new version, do not prompt to download directly
mIsAutoInstall | boolean | true | Whether to automatic install app when the download is completed

(2) DownloadEntity

Field name | Type | Default value | Description
:-|:-:|:-:|:-
mDownloadUrl | String | "" | Download address
mCacheDir | String | "" | File download directory
mMd5 | String | "" | The encrypted check value of the downloaded file (MD5 encryption is used by default), which is used to verify and prevent the downloaded APK file from being replaced (the latest demo has a tool for calculating the check value). Note that the MD5 value here is not the MD5 value of the application signature file!
mSize | long | 0 | Size of download file【unit: KB】
mIsShowNotification | boolean | false | Whether to show download progress in the notification bar

(3) PromptEntity

Field name | Type | Default value | Description
:-|:-:|:-:|:-
mThemeColor | int | R.color.xupdate_default_theme_color | Theme colors (background colors for progress bars and buttons)
mTopResId | int | R.drawable.xupdate_bg_app_top | Top background image resource ID
mButtonTextColor | int | 0 | Button text color
mSupportBackgroundUpdate | boolean | false | Whether background updates are supported
mWidthRatio | float | -1（Unconstrained） | The ratio of the width of the version update prompter to the screen
mHeightRatio | float | -1（Unconstrained） | The ratio of the height of the version update prompter to the screen

### 2.4、File encryption verification method

The default file encryption verification method used in this framework is MD5 encryption. Of course, if you don't want to use MD5 encryption, you can also customize the File Encryptor `IFileEncryptor`. The following is the implementation of MD5 File Encryptor for reference:

```
/**
 * The default file encryption calculation uses MD5 encryption
 *
 * @author xuexiang
 * @since 2019-09-06 14:21
 */
public class DefaultFileEncryptor implements IFileEncryptor {
    /**
     * Encrypted files
     *
     * @param file
     * @return
     */
    @Override
    public String encryptFile(File file) {
        return Md5Utils.getFileMD5(file);
    }

    /**
     * Verify that the file is valid (whether the encryption is consistent)
     *
     * @param encrypt The encrypted value, is considered to be valid if encrypt is empty.
     * @param file    File to be verified
     * @return Whether the document is valid
     */
    @Override
    public boolean isFileValid(String encrypt, File file) {
        return TextUtils.isEmpty(encrypt) || encrypt.equalsIgnoreCase(encryptFile(file));
    }
}

```

Finally, call the `XUpdate.get().setIFileEncryptor` method, settings will take effect.

---

## 3、Version update

### 3.1、Default version update

You can directly call the following code to complete the version update operation:

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .update();
```

It should be noted that with the default version update, the JSON format returned by the request server should include the following contents:

```
{
  "Code": 0, // 0 means the request is successful, non-0 means failure
  "Msg": "", // Request error information
  "UpdateStatus": 1, // 0 means no update, 1 means version update, no forced upgrade is required, and 2 represents version update and forced upgrade is required
  "VersionCode": 3,
  "VersionName": "1.0.2",
  "ModifyContent": "1、优化api接口。\r\n2、添加使用demo演示。\r\n3、新增自定义更新服务API接口。\r\n4、优化更新提示界面。",
  "DownloadUrl": "https://raw.githubusercontent.com/xuexiangjys/XUpdate/master/apk/xupdate_demo_1.0.2.apk",
  "ApkSize": 2048
  "ApkMd5": "..."  // If the MD5 value of APK is not available, there is no guarantee that the APK is complete and will be downloaded again every time. The framework uses MD5 encryption by default.
}
```

### 3.2、Automatic version update

Automatic version update: auto check version + auto download APK + auto install APK (silent install).

You only need to set `isAutoMode(true)`. However, if the device does not have root permission, it will not be able to complete automatic update (because the silent installation requires root permission).

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .isAutoMode(true) // If you need to be completely unattended and update automatically, you need root permission【required for silent installation】
        .update();
```

### 3.3、Support background update

After enabling the background update, users can enter the background update after clicking the "background update" button.

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .supportBackgroundUpdate(true)
        .update();
```

### 3.4、Force version update

If the user does not update, the program will not work normally. The server only needs to return the `UpdateStatus` field to 2.

Of course, if you customize the request return API, you only need to set the `mIsForce` field of `UpdateEntity` to true.

### 3.5、Custom version update prompt pop-up theme

By setting the update top picture, theme color, button text color, width to height ratio, etc

* promptThemeColor: Set theme color
* promptButtonTextColor: Set the text color of the button
* promptTopResId: Set top background image Resource ID
* promptWidthRatio: Set the ratio of the width of the version update prompt to the screen. The default value is -1(No constraint is required).
* promptHeightRatio: Set the ratio of the height of the version update prompt to the screen. The default value is -1(No constraint is required).

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl)
        .promptThemeColor(ResUtils.getColor(R.color.update_theme_color))
        .promptButtonTextColor(Color.WHITE)
        .promptTopResId(R.mipmap.bg_update_top)
        .promptWidthRatio(0.7F)
        .update();
```

### 3.6、Custom version update parser

The implementation of `IUpdateParser` interface can realize the user-defined parser.

```
XUpdate.newBuild(getActivity())
        .updateUrl(mUpdateUrl3)
        .updateParser(new CustomUpdateParser()) // Set up a custom version update parser
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

### 3.7、Custom version update checker + version update parser + version update prompter

* By implementing the `IUpdateChecker` interface, the checker can be customized.

* By implementing the `IUpdateParser` interface, the parser can be customized.

* By implementing the `IUpdatePrompter` interface, the prompter can be customized.

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
     * Show custom version update prompter
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

### 3.8、Only use the downloader function to download APK

```
XUpdate.newBuild(getActivity())
        .apkCacheDir(PathUtils.getExtDownloadsPath())  // Set the root directory of the download cache
        .build()
        .download(mDownloadUrl, new OnFileDownloadListener() {   // Set the download address and download listener
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

### 3.9、Only use the APK installed features of XUpdate

```
_XUpdate.startInstallApk(getContext(), FileUtils.getFileByPath(PathUtils.getFilePathByUri(getContext(), data.getData()))); // Set the path where the file is located
```

If your APK installation is different, you can implement your own APK installer. You only need to implement the `OnInstallListener` interface and use the `XUpdate.setOnInstallListener` set it to take effect.


## Proguard

```
-keep class com.xuexiang.xupdate.entity.** { *; }

// Note: if you use a custom API parser for parsing, you need to add confusion to your custom API entities. Here are the custom API entity obfuscation rules configured in this demo:
-keep class com.xuexiang.xupdatedemo.entity.** { *; }

```

## Related links

* [XUpdate Document](https://github.com/xuexiangjys/XUpdate/wiki)
* [XUpdate Easy Use](https://github.com/xuexiangjys/XUpdateAPI)
* [XUpdate Management Service](https://github.com/xuexiangjys/XUpdateService)
* [XUpdate Background Management System](https://github.com/xuexiangjys/xupdate-management)
* [XUpdate Flutter Plugin](https://github.com/xuexiangjys/flutter_xupdate)
* [XUpdate React-Native Plugin](https://github.com/xuexiangjys/react-native-xupdate)
* [Flutter Version update pop up components](https://github.com/xuexiangjys/flutter_update_dialog)

---


## Thanks

https://github.com/WVector/AppUpdate

## Sponsor

> Your support is the driving force of my maintenance. I will list the list of all the reward personnel at the bottom as the voucher. Please leave the notes of the support items before rewarding!

![pay.png](https://img.rruu.net/image/5f871d00045da)

Thank you for your sponsorship：

Name | Money | Platform
:-|:-|:-
*天 | 100￥ | WeChat
*航 | 10￥ | Alipay

## WeChat Subscription

> More information, please scan my personal WeChat Subscription:【我的Android开源之旅】

![gzh_weixin.jpg](https://img.rruu.net/image/5f871cfff3194)

## Contact

[![](https://img.shields.io/badge/QQGroup-720212425-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=37ea606864cafa3c8a5d6b07f04fd672936a50fd6e535c13702baf705d57e6e8)

![xupdate_qq.jpg](https://img.rruu.net/image/5f7d949844672)

