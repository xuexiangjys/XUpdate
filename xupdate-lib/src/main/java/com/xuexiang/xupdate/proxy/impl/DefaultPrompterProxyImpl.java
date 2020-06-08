package com.xuexiang.xupdate.proxy.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IPrompterProxy;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

import java.lang.ref.WeakReference;

/**
 * 默认版本更新提示器代理
 *
 * @author xuexiang
 * @since 2020/6/9 12:19 AM
 */
public class DefaultPrompterProxyImpl implements IPrompterProxy {

    private WeakReference<IUpdateProxy> mWeakUpdateProxy;

    public DefaultPrompterProxyImpl(IUpdateProxy proxy) {
        mWeakUpdateProxy = new WeakReference<>(proxy);
    }

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        IUpdateProxy proxy = getUpdateProxy();
        if (proxy != null) {
            proxy.startDownload(updateEntity, downloadListener);
        }
    }

    @Override
    public void backgroundDownload() {
        IUpdateProxy proxy = getUpdateProxy();
        if (proxy != null) {
            proxy.backgroundDownload();
        }
    }

    @Override
    public void cancelDownload() {
        IUpdateProxy proxy = getUpdateProxy();
        if (proxy != null) {
            proxy.cancelDownload();
        }
    }

    private IUpdateProxy getUpdateProxy() {
        if (mWeakUpdateProxy != null) {
            return mWeakUpdateProxy.get();
        }
        return null;
    }
}
