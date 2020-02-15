package com.xuexiang.xupdate.proxy.impl;

import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.proxy.IUpdateParser;

/**
 * 默认是使用同步解析器，因此异步解析方法不需要实现
 *
 * @author xuexiang
 * @since 2020-02-15 17:56
 */
public abstract class AbstractUpdateParser implements IUpdateParser {

    @Override
    public void parseJson(String json, IUpdateParseCallback callback) throws Exception {
        //当isAsyncParser为 true时调用该方法
    }

    @Override
    public boolean isAsyncParser() {
        return false;
    }
}
