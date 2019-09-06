package com.xuexiang.xupdate.proxy;

import java.io.File;

/**
 * 文件加密器，用于文件有效性校验
 *
 * @author xuexiang
 * @since 2019-09-06 14:15
 */
public interface IFileEncryptor {

    /**
     * 加密文件
     *
     * @param file
     * @return
     */
    String encryptFile(File file);

    /**
     * 检验文件是否有效（加密是否一致）
     *
     * @param encrypt 加密值
     * @param file    需要校验的文件
     * @return 文件是否有效
     */
    boolean isFileValid(String encrypt, File file);
}
