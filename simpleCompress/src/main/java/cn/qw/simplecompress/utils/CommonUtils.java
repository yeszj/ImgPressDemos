package cn.qw.simplecompress.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * @author: witness
 * created: 2022/3/31
 * desc:
 */
public class CommonUtils {
    /**
     * 许多定制的android系统，并不带相机功能。如果强行调用，程序会崩溃
     *
     * @param activity    上下文
     * @param intent      相机意图
     * @param requestCode 回调标识码
     */
    public static void hasCamera(Activity activity, Intent intent, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("activity为空");
        }
        PackageManager packageManager = activity.getPackageManager();
        try {
            CameraManager cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            boolean hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
                    || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                    || cameraManager.getCameraIdList().length > 0;
            if (hasCamera) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                Toast.makeText(activity, "当前设备没有相机", Toast.LENGTH_SHORT).show();
                throw new IllegalStateException("当前设备没有相机");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param outPutUri 拍照后图片的输出uri
     * @return 返回intent，方便封装跳转
     */
    public static Intent getCameraIntent(Uri outPutUri) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        return intent;
    }

    /**
     * 打开图库
     *
     * @param activity    上下文
     * @param requestCode 回调码
     */
    public static void openAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static ProgressDialog showProgressDialog(Activity activity, String... progressTitle) {
        if (activity == null || activity.isFinishing()) return null;
        String title = "提示";
        if (progressTitle != null && progressTitle.length > 0) title = progressTitle[0];
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

}
