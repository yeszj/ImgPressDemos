package cn.qw.simplecompress.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;


import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.qw.simplecompress.config.SimpleCompressConfig;
import cn.qw.simplecompress.listener.CompressSingleResultListener;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class SimpleComPressUtil {
    private final SimpleCompressConfig compressConfig;

    public SimpleComPressUtil(SimpleCompressConfig compressConfig) {
        this.compressConfig = compressConfig == null ? SimpleCompressConfig.getDefaultConfig() : compressConfig;
    }

    public void startCompress(String imgPath, CompressSingleResultListener resultListener) {
        if (compressConfig.isEnablePixelCompress()) {
            try {
                compressImageByPixel(imgPath, resultListener);
            } catch (Exception e) {
                resultListener.onCompressFail(imgPath, String.format("图片压缩失败%s", e.toString()));
                e.printStackTrace();
            }
        } else {
            compressImageByQuality(imgPath, resultListener);
        }
    }

    private void compressImageByQuality(String imgPath, CompressSingleResultListener resultListener, ByteArrayOutputStream... byteArrayOutputStreams) {
        Bitmap inputBitmap = BitmapFactory.decodeFile(imgPath);
        ByteArrayOutputStream byteArrayOutputStream;
        if (byteArrayOutputStreams.length > 0) {
            byteArrayOutputStream = byteArrayOutputStreams[0];
        } else {
            byteArrayOutputStream = new ByteArrayOutputStream();
        }
        int quality = 90;
        int degree = readPictureDegree(imgPath);
        inputBitmap = rotateBitmap(inputBitmap, degree);
        inputBitmap.compress(compressConfig.getComPressFormat(), quality, byteArrayOutputStream);
        //如果压缩后图片还是>targetSize，则继续压缩
        while (byteArrayOutputStream.toByteArray().length > compressConfig.getMaxSize()) {
            byteArrayOutputStream.reset();
            quality -= 5;
            inputBitmap.compress(compressConfig.getComPressFormat(), quality, byteArrayOutputStream);
            if (quality == 5) {
                //限制最低压缩到5
                break;
            }
        }
        inputBitmap.recycle();
        String outputPath = compressConfig.getCacheDir() + getFileName(imgPath) + compressConfig.getFileSuffix();
        resultListener.onCompressSuccess(outputPath);
        File file = createFile(outputPath);
        saveCompressedImage(file, byteArrayOutputStream);
    }

    @NonNull
    private File createFile(String outputPath) {
        File file = new File(outputPath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    private void compressImageByPixel(String imgPath, CompressSingleResultListener resultListener) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算采样率只需要宽高
        options.inJustDecodeBounds = true;
        //此处在option中已取得宽高
        BitmapFactory.decodeFile(imgPath, options);
        //设置采样率
        options.inSampleSize = calculateSampleSize(options, compressConfig);

        //重新设置为decode整张图片，准备压缩
        options.inJustDecodeBounds = false;

        options.inPreferredConfig = compressConfig.getPixelConfig(); // 即使不设置，也会默认采用改模式

        // 以下两项，4.4以下生效，当系统内存不够时候图片自动被回收
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int degree = readPictureDegree(imgPath);
        bitmap = rotateBitmap(bitmap, degree);
        bitmap.compress(compressConfig.getComPressFormat(), 100, byteArrayOutputStream);
        bitmap.recycle();

        String outputPath = compressConfig.getCacheDir() + getFileName(imgPath) + compressConfig.getFileSuffix();
        File file = createFile(outputPath);
        saveCompressedImage(file, byteArrayOutputStream);
        //还需要质量压缩
        if (compressConfig.isEnableQualityCompress()) {
            //此时质量压缩的源文件路径应该为像素压缩的输出路径
            compressImageByQuality(file.getAbsolutePath(), resultListener);
        } else {
            resultListener.onCompressSuccess(outputPath);
        }
    }

    /**
     * 计算出所需要压缩的大小
     */
    private int calculateSampleSize(BitmapFactory.Options options, SimpleCompressConfig compressConfig) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        int maxPixel = compressConfig.getMaxPixel();

        // 缩放比,用高或者宽其中较大的一个数据进行计算
        if (picWidth >= picHeight && picWidth > maxPixel) {
            sampleSize = picWidth / maxPixel;
            sampleSize++;
        } else if (picWidth < picHeight && picHeight > maxPixel) {
            sampleSize = picHeight / maxPixel;
            sampleSize++;
        }
        return sampleSize;
    }

    private void saveCompressedImage(File file, ByteArrayOutputStream bos) {
        FileOutputStream fos;//将压缩后的图片保存的本地上指定路径中
        try {
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件路径中提取文件名
     */
    private String getFileName(String path) {
        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 获取图片旋转角度
     */
    private static int readPictureDegree(String srcPath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(srcPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //处理图片旋转
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
