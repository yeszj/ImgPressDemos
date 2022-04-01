package cn.qw.simplecompress.listener;

import java.util.List;

import cn.qw.simplecompress.bean.CompressPhotoInfo;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public interface CompressResultListener {
    void onCompressSuccess(List<CompressPhotoInfo> compressSuccessList);

    void onCompressFail(List<CompressPhotoInfo> compressImgList);
}
