图片上传为什么要压缩

1.能否直接上传原图，让后台处理？

  1.图片服务器的磁盘空间非常贵
  
  2.尽可能避免安卓OOM异常
  
  3.后台约束的规则，比如每张图片必须《=300KB


如何使用：

在build.gradle中引入：

io.github.yeszj:simpleCompress:1.0.0

使用之前需要申请权限 Manifest.permission.WRITE_EXTERNAL_STORAGE，如需要拍照功能的还需要申请
Manifest.permission.CAMERA权限
    
    1.单张图片压缩
    imgPath图片原始路径
    private void startCompress(String imgPath) {
        SimpleCompressConfig simpleCompressConfig = SimpleCompressConfig.builder().
                setMaxSize(100 * 1024).//压缩到100M以内
                setCacheDir(Constants.BASE_CACHE_PATH + getPackageName() + "/cache/" + Constants.SIMPLE_COMPRESS_CACHE)//压缩后图片输出路径
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

    2.多张图片压缩
    List<CompressPhotoInfo> list;
    private void startCompress(String imgPath) {
        //为了方便测试，在这直接初始化了一个数组
        list = new ArrayList<>();
        CompressPhotoInfo photoInfo = new CompressPhotoInfo();
        photoInfo.setOriginalPath(imgPath);
        list.add(photoInfo);
        
        SimpleCompressConfig simpleCompressConfig = SimpleCompressConfig.builder().
                setMaxSize(100 * 1024).
                setCacheDir(Constants.BASE_CACHE_PATH + getPackageName() + "/cache/" + Constants.SIMPLE_COMPRESS_CACHE)
                .create();
        SimpleCompressManager.build(this, simpleCompressConfig, list, new CompressResultListener() {
            @Override
            public void onCompressSuccess(List<CompressPhotoInfo> compressSuccessList) {
                CompressPhotoInfo photoInfo = compressSuccessList.get(0);
                String compressPath = photoInfo.getCompressPath();
                Log.d("------->", String.format("压缩成功,输出路径为：%s", compressPath));
            }
            @Override
            public void onCompressFail(List<CompressPhotoInfo> compressImgList) {
                Log.d("------->", "压缩失败");
            }
        }).startCompress();
    }




    
  
  
  
