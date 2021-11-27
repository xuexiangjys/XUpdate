package com.xuexiang.xupdate.proxy.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IPrompterProxy;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

/**
 * 默认版本更新提示器代理
 *
 * @author xuexiang
 * @since 2020/6/9 12:19 AM
 */
public class DefaultPrompterProxyImpl implements IPrompterProxy {

    private IUpdateProxy mUpdateProxy;

    DefaultPrompterProxyImpl(IUpdateProxy proxy) {
        mUpdateProxy = proxy;
    }

    @Override
    public String getUrl() {
        return mUpdateProxy != null ? mUpdateProxy.getUrl() : "";
    }

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        if (mUpdateProxy != null) {
            mUpdateProxy.startDownload(updateEntity, downloadListener);
        }
    }

    @Override
    public void backgroundDownload() {
        if (mUpdateProxy != null) {
            mUpdateProxy.backgroundDownload();
        }
    }

    @Override
    public void cancelDownload() {
        _XUpdate.setIsPrompterShow(getUrl(), false);
        if (mUpdateProxy != null) {
            mUpdateProxy.cancelDownload();
        }
    }

    @Override
    public void recycle() {
        if (mUpdateProxy != null) {
            mUpdateProxy.recycle();
            mUpdateProxy = null;
        }
    }

}
