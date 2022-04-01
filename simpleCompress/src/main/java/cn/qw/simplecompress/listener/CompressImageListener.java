package cn.qw.simplecompress.listener;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public interface CompressImageListener {
    void startCompress();

    void startSingleCompress(String imgPath);
}
