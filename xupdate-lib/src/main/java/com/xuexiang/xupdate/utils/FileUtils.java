package com.xuexiang.xupdate.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.xuexiang.xupdate.XUpdate;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作工具类
 *
 * @author xuexiang
 * @since 2020/6/6 11:52 AM
 */
public final class FileUtils {

    /**
     * 只读模式
     */
    public static final String MODE_READ_ONLY = "r";

    private static final String EXT_STORAGE_PATH = getExtStoragePath();

    private static final String EXT_STORAGE_DIR = EXT_STORAGE_PATH + File.separator;

    private static final String APP_EXT_STORAGE_PATH = EXT_STORAGE_DIR + "Android";

    private static final String EXT_DOWNLOADS_PATH = getExtDownloadsPath();

    private static final String EXT_PICTURES_PATH = getExtPicturesPath();

    private static final String EXT_DCIM_PATH = getExtDCIMPath();

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    @Nullable
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExists(file.getAbsolutePath());
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(filePath);
    }

    /**
     * Android 10判断文件是否存在的方法
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    private static boolean isFileExistsApi29(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AssetFileDescriptor afd = null;
            try {
                Uri uri = Uri.parse(filePath);
                afd = openAssetFileDescriptor(uri);
                if (afd == null) {
                    return false;
                } else {
                    closeIOQuietly(afd);
                }
            } catch (FileNotFoundException e) {
                return false;
            } finally {
                closeIOQuietly(afd);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取文件输入流
     *
     * @param file 文件
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileInputStream(File file) throws FileNotFoundException {
        if (isScopedStorageMode()) {
            return getContentResolver().openInputStream(getUriByFile(file));
        } else {
            return new FileInputStream(file);
        }
    }


    /**
     * 根据文件获取uri
     *
     * @param file 文件
     * @return
     */
    public static Uri getUriByFile(final File file) {
        if (file == null) {
            return null;
        }
        if (isScopedStorageMode() && isPublicPath(file)) {
            String filePath = file.getAbsolutePath();
            if (filePath.startsWith(EXT_DOWNLOADS_PATH)) {
                return getDownloadContentUri(XUpdate.getContext(), file);
            } else if (filePath.startsWith(EXT_PICTURES_PATH) || filePath.startsWith(EXT_DCIM_PATH)) {
                return getMediaContentUri(XUpdate.getContext(), file);
            } else {
                return getUriForFile(file);
            }
        } else {
            return getUriForFile(file);
        }
    }


    /**
     * Return a content URI for a given file.
     *
     * @param file The file.
     * @return a content URI for a given file
     */
    @Nullable
    public static Uri getUriForFile(final File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = XUpdate.getContext().getPackageName() + ".updateFileProvider";
            return FileProvider.getUriForFile(XUpdate.getContext(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 是否是分区存储模式：在公共目录下file的api无效了
     *
     * @return 是否是分区存储模式
     */
    public static boolean isScopedStorageMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy();
    }

    /**
     * 将媒体文件转化为资源定位符
     *
     * @param context
     * @param mediaFile 媒体文件
     * @return
     */
    public static Uri getMediaContentUri(Context context, File mediaFile) {
        String filePath = mediaFile.getAbsolutePath();
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(baseUri,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (mediaFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(baseUri, values);
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getDownloadContentUri(Context context, File file) {
        String filePath = file.getAbsolutePath();
        Uri baseUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(baseUri,
                new String[]{MediaStore.Downloads._ID}, MediaStore.Downloads.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.DownloadColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (file.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DATA, filePath);
                return context.getContentResolver().insert(baseUri, values);
            }
            return null;
        }
    }

    /**
     * 是否是私有目录
     *
     * @param path 需要判断的目录
     * @return
     */
    public static boolean isPrivatePath(Context context, String path) {
        if (isSpace(path)) {
            return true;
        }
        String appIntPath = "/data/data/" + context.getPackageName();
        String appExtPath = APP_EXT_STORAGE_PATH + "/data/" + context.getPackageName();
        return path.startsWith(appIntPath) || path.startsWith(appExtPath);
    }

    /**
     * 是否是公有目录
     *
     * @return 是否是公有目录
     */
    public static boolean isPublicPath(File file) {
        if (file == null) {
            return false;
        }
        try {
            return isPublicPath(file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否是公有目录
     *
     * @return 是否是公有目录
     */
    public static boolean isPublicPath(String filePath) {
        if (isSpace(filePath)) {
            return false;
        }
        return filePath.startsWith(EXT_STORAGE_PATH) && !filePath.startsWith(APP_EXT_STORAGE_PATH);
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 从uri资源符中读取文件描述
     *
     * @param uri 文本资源符
     * @return AssetFileDescriptor
     */
    public static AssetFileDescriptor openAssetFileDescriptor(Uri uri) throws FileNotFoundException {
        return getContentResolver().openAssetFileDescriptor(uri, MODE_READ_ONLY);
    }

    private static ContentResolver getContentResolver() {
        return XUpdate.getContext().getContentResolver();
    }

    /**
     * 获取 Android 外置储存的根目录
     * <pre>path: /storage/emulated/0</pre>
     *
     * @return 外置储存根目录
     */
    public static String getExtStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取下载目录
     * <pre>path: /storage/emulated/0/Download</pre>
     *
     * @return 下载目录
     */
    public static String getExtDownloadsPath() {
        return Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
    }

    /**
     * 获取图片目录
     * <pre>path: /storage/emulated/0/Pictures</pre>
     *
     * @return 图片目录
     */
    public static String getExtPicturesPath() {
        return Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath();
    }

    /**
     * 获取相机拍摄的照片和视频的目录
     * <pre>path: /storage/emulated/0/DCIM</pre>
     *
     * @return 照片和视频目录
     */
    public static String getExtDCIMPath() {
        return Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath();
    }

}
