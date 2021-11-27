package com.xuexiang.xupdate.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

/**
 * 版本更新提示器代理
 *
 * @author xuexiang
 * @since 2020/6/9 12:16 AM
 */
public interface IPrompterProxy {

    /**
     * 获取版本更新的地址
     *
     * @return 版本更新的地址
     */
    String getUrl();

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 后台下载
     */
    void backgroundDownload();

    /**
     * 取消下载
     */
    void cancelDownload();

    /**
     * 资源回收
     */
    void recycle();
}
