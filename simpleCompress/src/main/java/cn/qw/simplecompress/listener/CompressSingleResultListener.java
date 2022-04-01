package cn.qw.simplecompress.listener;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public interface CompressSingleResultListener {
    void onCompressSuccess(String fileName);
    void onCompressFail(String imgPath,String error);
}
