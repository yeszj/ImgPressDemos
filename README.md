图片上传为什么要压缩
1.能否直接上传原图，让后台处理？
  1.图片服务器的磁盘空间非常贵
  2.尽可能避免安卓OOM异常
  3.后台约束的规则，比如每张图片必须《=300KB

图片压缩流程：
1.递归每张图片->设置图片格式->质量压缩->像素修复->返回压缩结果集->完成压缩

图片压缩方式：
  1.设置图片格式   Bitmap.CompressFormat.JPEG
  2.质量压缩 根据width*height一个像素的所占用的字节数计算，宽高不变 bitmap.compress(format,quality,baos)
    由于png是无损压缩，所以设置quality无效(不适合作为缩略图)
  3.采样率压缩
    缩小图片分辨率，减少所占用磁盘空间和内存大小 BitmapFactory.Options.inSampleSize
  4.缩放压缩
    减少图片的像素，降低所占用磁盘空间大小和内存大小 canvas.drawBitmap(bitmap,null,rectF,null)
    可以用于缓存缩略图
  5.jni调用JPEG库

图片压缩框架 Luban
Luban框架缺点
1.当没有设定压缩路径时，抛异常无闪退
2.源码中，压缩比率固定值60，无法修改】
3.压缩配置，参数不太适应真实项目需求
4.不能指定压缩大小，比如100KB以内
    
  
  
  
