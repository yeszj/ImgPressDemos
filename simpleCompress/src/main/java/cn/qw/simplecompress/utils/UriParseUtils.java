package cn.qw.simplecompress.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class UriParseUtils {
    /**
     * 创建一个文件输出路径的Uri
     *
     * @param context 上下嗯
     * @param file    文件
     * @return 转换后的scheme为FileProvider的Uri
     */
    private static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProvider(context), file);
    }

    private static String getFileProvider(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * 获取拍照后照片的Uri
     *
     * @param context   上下文
     * @param cacheFile 照片文件
     * @return 照片的Uri
     */
    public static Uri getCameraOutPutUri(Context context, File cacheFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getUriForFile(context, cacheFile);
        } else {
            return Uri.fromFile(cacheFile);
        }
    }

    /**
     * @return 返回图片绝对路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
