package cn.qw.simplecompress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qw.simplecompress.bean.CompressPhotoInfo;
import cn.qw.simplecompress.config.SimpleCompressConfig;
import cn.qw.simplecompress.listener.CompressImageListener;
import cn.qw.simplecompress.listener.CompressResultListener;
import cn.qw.simplecompress.listener.CompressSingleResultListener;
import cn.qw.simplecompress.utils.CommonUtils;
import cn.qw.simplecompress.utils.SimpleComPressUtil;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class SimpleCompressManager implements CompressImageListener {
    private final SimpleComPressUtil simpleComPressUtil; //压缩工具类
    private List<CompressPhotoInfo> imgList = new ArrayList<>(); //需要压缩的图片集合
    private CompressResultListener resultListener;//压缩的监听
    private final SimpleCompressConfig compressConfig;//压缩的配置
    private final Context context;
    private ProgressDialog progressDialog;
    private CompressSingleResultListener singleResultListener;

    private SimpleCompressManager(Context context, SimpleCompressConfig compressConfig,
                                  List<CompressPhotoInfo> imgList, CompressResultListener resultListener) {
        this.context = context;
        this.compressConfig = compressConfig;
        this.imgList = imgList;
        this.resultListener = resultListener;
        simpleComPressUtil = new SimpleComPressUtil(compressConfig);
    }

    private SimpleCompressManager(Context context, SimpleCompressConfig compressConfig,
                                  CompressSingleResultListener resultListener) {
        this.context = context;
        this.compressConfig = compressConfig;
        this.singleResultListener = resultListener;
        simpleComPressUtil = new SimpleComPressUtil(compressConfig);
    }

    public static CompressImageListener build(Context context, SimpleCompressConfig compressConfig,
                                              List<CompressPhotoInfo> imgList, CompressResultListener resultListener) {
        return new SimpleCompressManager(context, compressConfig, imgList, resultListener);
    }

    public static CompressImageListener build(Context context, SimpleCompressConfig compressConfig,
                                              CompressSingleResultListener resultListener) {
        return new SimpleCompressManager(context, compressConfig, resultListener);
    }

    @Override
    public void startCompress() {
        if (imgList == null || imgList.isEmpty()) {
            Toast.makeText(context, "请添加需要压缩的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        compress(imgList.get(0));
    }

    @Override
    public void startSingleCompress(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) {
            Toast.makeText(context, "压缩的图片为null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (compressConfig.isShowProgressDilog()) {
            progressDialog = CommonUtils.showProgressDialog((Activity) context, "正在压缩中...");
        }
        File file = new File(imgPath);
        if (!file.exists() || !file.isFile()) {
            Toast.makeText(context, "图片格式错误", Toast.LENGTH_SHORT).show();
            hideProgress();
            return;
        }
        if (file.length() < compressConfig.getMaxSize()) {
            singleResultListener.onCompressSuccess(imgPath);
            hideProgress();
            return;
        }
        simpleComPressUtil.startCompress(imgPath, new CompressSingleResultListener() {
            @Override
            public void onCompressSuccess(String fileName) {
                hideProgress();
                singleResultListener.onCompressSuccess(fileName);
            }

            @Override
            public void onCompressFail(String imgPath, String error) {
                hideProgress();
                singleResultListener.onCompressFail(imgPath, error);
            }
        });
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void compress(CompressPhotoInfo compressPhotoInfo) {
        if (compressPhotoInfo == null) {
            Toast.makeText(context, "压缩的图片为null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(compressPhotoInfo.getOriginalPath())) {
            continueCompress(compressPhotoInfo, false);
            return;
        }
        if (compressConfig.isShowProgressDilog()) {
            progressDialog = CommonUtils.showProgressDialog((Activity) context, "正在压缩中...");
        }
        File file = new File(compressPhotoInfo.getOriginalPath());
        if (!file.exists() || !file.isFile()) {
            compressPhotoInfo.setCompressFailReason("图片格式错误");
            continueCompress(compressPhotoInfo, false);
            return;
        }
        if (file.length() < compressConfig.getMaxSize()) {
            compressPhotoInfo.setCompressPath(compressPhotoInfo.getOriginalPath());
            continueCompress(compressPhotoInfo, true);
            return;
        }
        simpleComPressUtil.startCompress(compressPhotoInfo.getOriginalPath(), new CompressSingleResultListener() {
            @Override
            public void onCompressSuccess(String fileName) {
                compressPhotoInfo.setCompressPath(fileName);
                continueCompress(compressPhotoInfo, true);
            }

            @Override
            public void onCompressFail(String imgPath, String error) {
                compressPhotoInfo.setCompressFailReason(error);
                continueCompress(compressPhotoInfo, false);
            }
        });

    }

    private void continueCompress(CompressPhotoInfo compressPhotoInfo, boolean isCompress) {
        compressPhotoInfo.setCompressed(isCompress);
        int index = imgList.indexOf(compressPhotoInfo);
        if (index == imgList.size() - 1) {
            //已经全部压缩
            callBack();
        } else {
            compress(imgList.get(index + 1));
        }
        hideProgress();
    }

    private void callBack() {
        for (CompressPhotoInfo item : imgList) {
            if (!item.isCompressed()) {
                resultListener.onCompressFail(imgList);
                return;
            }
        }
        resultListener.onCompressSuccess(imgList);
    }
}
