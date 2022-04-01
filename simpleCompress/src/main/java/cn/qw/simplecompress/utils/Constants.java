package cn.qw.simplecompress.utils;

import android.os.Environment;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class Constants {
    public final static int CAMERA_CODE = 10001;
    public final static int ALBUM_CODE = 10002;
    public final static String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/";
    public final static String SIMPLE_COMPRESS_CACHE = "simple_compress/";
}
