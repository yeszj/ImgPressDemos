package cn.qw.imgpressdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import cn.qw.simplecompress.SimpleCompressManager;
import cn.qw.simplecompress.config.SimpleCompressConfig;
import cn.qw.simplecompress.listener.CompressSingleResultListener;
import cn.qw.simplecompress.utils.CacheUtils;
import cn.qw.simplecompress.utils.CommonUtils;
import cn.qw.simplecompress.utils.Constants;
import cn.qw.simplecompress.utils.UriParseUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
        findViewById(R.id.tv_camera).setOnClickListener(view -> openBlum());
    }

    private void startCompress(String imgPath) {
        SimpleCompressConfig simpleCompressConfig = SimpleCompressConfig.builder().
                setMaxSize(100 * 1024).
                setCacheDir(Constants.BASE_CACHE_PATH + getPackageName() + "/cache/" + Constants.SIMPLE_COMPRESS_CACHE)
                .create();
        SimpleCompressManager.build(this, simpleCompressConfig, new CompressSingleResultListener() {
            @Override
            public void onCompressSuccess(String fileName) {
                Log.d("------->", String.format("压缩成功,输出路径为：%s", fileName));
            }
            @Override
            public void onCompressFail(String imgPath, String error) {
                Log.d("------->", String.format("压缩失败：%s%s", imgPath, error));
            }
        }).startSingleCompress(imgPath);
    }

//    //多张图片压缩
//    List<CompressPhotoInfo> list;
//    private void startCompress(String imgPath) {
//        list = new ArrayList<>();
//        CompressPhotoInfo photoInfo = new CompressPhotoInfo();
//        photoInfo.setOriginalPath(imgPath);
//        list.add(photoInfo);
//        SimpleCompressConfig simpleCompressConfig = SimpleCompressConfig.builder().
//                setMaxSize(100 * 1024).
//                setCacheDir(Constants.BASE_CACHE_PATH + getPackageName() + "/cache/" + Constants.SIMPLE_COMPRESS_CACHE)
//                .create();
//        SimpleCompressManager.build(this, simpleCompressConfig, list, new CompressResultListener() {
//            @Override
//            public void onCompressSuccess(List<CompressPhotoInfo> compressSuccessList) {
//                CompressPhotoInfo photoInfo = compressSuccessList.get(0);
//                String compressPath = photoInfo.getCompressPath();
//                Log.d("------->", String.format("压缩成功,输出路径为：%s", compressPath));
//            }
//            @Override
//            public void onCompressFail(List<CompressPhotoInfo> compressImgList) {
//                Log.d("------->", "压缩失败");
//            }
//        }).startCompress();
//    }

    private String cameraCachePath;

    private void camera() {
        File cameraCacheFile = CacheUtils.getCameraCacheFile();
        cameraCachePath = cameraCacheFile.getAbsolutePath();
        Uri cameraOutPutUri = UriParseUtils.getCameraOutPutUri(this, cameraCacheFile);
        CommonUtils.hasCamera(this, CommonUtils.getCameraIntent(cameraOutPutUri), Constants.CAMERA_CODE);
    }

    private void openBlum() {
        CommonUtils.openAlbum(this, Constants.ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ALBUM_CODE) {
                if (data != null) {
                    Uri uri = data.getData();
                    String path = UriParseUtils.getRealFilePath(this, uri);
                    startCompress(path);
                }
            } else if (requestCode == Constants.CAMERA_CODE) {
                startCompress(cameraCachePath);
            }
        }
    }
}