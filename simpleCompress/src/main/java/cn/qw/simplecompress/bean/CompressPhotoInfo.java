package cn.qw.simplecompress.bean;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class CompressPhotoInfo {
    private String originalPath; //图片原始路径
    private String compressPath;//图片压缩后的路径
    private boolean compressed;
    private String compressFailReason;
    private String netImgUrl;

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getCompressFailReason() {
        return compressFailReason;
    }

    public void setCompressFailReason(String compressFailReason) {
        this.compressFailReason = compressFailReason;
    }

    public String getNetImgUrl() {
        return netImgUrl;
    }

    public void setNetImgUrl(String netImgUrl) {
        this.netImgUrl = netImgUrl;
    }
}
