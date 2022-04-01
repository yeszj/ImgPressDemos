package cn.qw.simplecompress.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class CacheUtils {
    private static File getCameraCacheDir(String  fileName){
        File cache = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!cache.mkdirs() && (!cache.exists() || !cache.isDirectory())){
            return null;
        }else {
            return new File(cache,fileName);
        }
    }

    private static String getBaseFileName(){
        return new SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH).format(new Date());
    }

    public static File getCameraCacheFile(){
        String fileName = "camera_"+getBaseFileName()+".jpg";
        return getCameraCacheDir(fileName);
    }
}
