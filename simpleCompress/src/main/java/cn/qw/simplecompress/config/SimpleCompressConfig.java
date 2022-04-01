package cn.qw.simplecompress.config;

import android.graphics.Bitmap;

import cn.qw.simplecompress.utils.Constants;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class SimpleCompressConfig {
    /**
     * 最小像素不压缩
     */
    private int unCompressMinPixel = 1000;
    //标准像素不压缩
    private int unCompressNormalPixel = 2000;
    /**
     * 长或宽不超过最大像素 px
     */
    private int maxPixel = 1200;
    //压缩到最大大小 单位B
    private int maxSize = 200 * 1024;
    //是否启用像素压缩
    private boolean enablePixelCompress = true;
    //是否启用质量压缩
    private boolean enableQualityCompress = true;
    //是否保留源文件
    private boolean enableSaveRaw = true;
    //压缩后缓存图片目录 非文件路径
    private String cacheDir  ;
    //是否显示压缩进度条
    private boolean showProgressDilog = true;
    /**
     * 图片压缩格式
     */
    private Bitmap.CompressFormat comPressFormat= Bitmap.CompressFormat.JPEG;

    /**
     * 图片压缩像素模式
     */
    private Bitmap.Config pixelConfig= Bitmap.Config.ARGB_8888;
    private String fileSuffix=".jpg";
    public SimpleCompressConfig() {
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public Bitmap.CompressFormat getComPressFormat() {
        return comPressFormat;
    }

    public void setComPressFormat(Bitmap.CompressFormat comPressFormat) {
        this.comPressFormat = comPressFormat;
    }

    public Bitmap.Config getPixelConfig() {
        return pixelConfig;
    }

    public void setPixelConfig(Bitmap.Config pixelConfig) {
        this.pixelConfig = pixelConfig;
    }

    public static SimpleCompressConfig getDefaultConfig() {
        return new SimpleCompressConfig();
    }

    public void setUnCompressMinPixel(int unCompressMinPixel) {
        this.unCompressMinPixel = unCompressMinPixel;
    }

    public void setUnCompressNormalPixel(int unCompressNormalPixel) {
        this.unCompressNormalPixel = unCompressNormalPixel;
    }

    public void setMaxPixel(int maxPixel) {
        this.maxPixel = maxPixel;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setEnablePixelCompress(boolean enablePixelCompress) {
        this.enablePixelCompress = enablePixelCompress;
    }

    public void setEnableQualityCompress(boolean enableQualityCompress) {
        this.enableQualityCompress = enableQualityCompress;
    }

    public void setEnableSaveRaw(boolean enableSaveRaw) {
        this.enableSaveRaw = enableSaveRaw;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public void setShowProgressDilog(boolean showProgressDilog) {
        this.showProgressDilog = showProgressDilog;
    }

    public int getUnCompressMinPixel() {
        return unCompressMinPixel;
    }

    public int getUnCompressNormalPixel() {
        return unCompressNormalPixel;
    }

    public int getMaxPixel() {
        return maxPixel;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isEnablePixelCompress() {
        return enablePixelCompress;
    }

    public boolean isEnableQualityCompress() {
        return enableQualityCompress;
    }

    public boolean isEnableSaveRaw() {
        return enableSaveRaw;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public boolean isShowProgressDilog() {
        return showProgressDilog;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SimpleCompressConfig compressConfig;
        private Builder() {
            compressConfig = new SimpleCompressConfig();
        }

        public Builder setUnCompressMinPixel(int unCompressMinPixel) {
            compressConfig.setUnCompressMinPixel(unCompressMinPixel);
            return this;
        }

        public Builder setUnCompressNormalPixel(int unCompressNormalPixel) {
            compressConfig.setUnCompressNormalPixel(unCompressNormalPixel);
            return this;
        }

        public Builder setMaxPixel(int maxPixel) {
            compressConfig.setMaxPixel(maxPixel);
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            compressConfig.setMaxSize(maxSize);
            return this;
        }

        public Builder setEnablePixelCompress(boolean enablePixelCompress) {
            compressConfig.setEnablePixelCompress(enablePixelCompress);
            return this;
        }

        public Builder setEnableQualityCompress(boolean enableQualityCompress) {
            compressConfig.setEnableQualityCompress(enableQualityCompress);
            return this;
        }

        public Builder setEnableSaveRaw(boolean enableSaveRaw) {
            compressConfig.setEnableSaveRaw(enableSaveRaw);
            return this;
        }

        public Builder setCacheDir(String cacheDir) {
            compressConfig.setCacheDir(cacheDir);
            return this;
        }

        public Builder setShowProgressDilog(boolean showProgressDilog) {
            compressConfig.setShowProgressDilog(showProgressDilog);
            return this;
        }
        public Builder format(Bitmap.CompressFormat format,Bitmap.Config pixelConfig){
            compressConfig.setComPressFormat(format);
            if (format== Bitmap.CompressFormat.JPEG){
                compressConfig.setFileSuffix(".jpg");
            }else if (format== Bitmap.CompressFormat.PNG){
                compressConfig.setFileSuffix(".png");
            }else {
                compressConfig.setFileSuffix(".webp");
            }
            compressConfig.setPixelConfig(pixelConfig);
            return this;
        }
        public SimpleCompressConfig create(){
            return compressConfig;
        }
    }
}
