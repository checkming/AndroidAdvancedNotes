# Bitmap--位图

## 如何得到 bitmap 对象？

Bitmap 是 Android 系统中的图像处理中最重要类之一。Bitmap 可以获取图像文件信息，对图像进行剪切、旋转、缩放，压缩等操作，并可以以指定格式保存图像文件。 

有两种方法可以创建 Bitmap 对象，分别是通过 Bitmap.createBitmap() 和 BitmapFactory 的 decode 系列静态方法创建 Bitmap 对象。 

下面我们主要介绍 BitmapFactory 的 decode 方式创建 Bitmap 对象。

- decodeFile 从文件系统中加载
  - 通过 Intent 打开本地图片或照片
  - 根据 uri 获取图片的路径
  - 根据路径解析 Bitmap：Bitmap bm = BitmapFactory.decodeFile(path);
- decodeResource 以 R.drawable.xxx 的形式从本地资源中加载
  - Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
- decodeStream 从输入流加载
  - Bitmap bm = BitmapFactory.decodeStream(stream);
- decodeByteArray 从字节数组中加载
  - Bitmap bm = BitmapFactory.decodeByteArray(myByte,0,myByte.length);

## BitmapFactory.Options

- inSampleSize：这是表示采样大小。用于将图片缩小加载出来的，以免占用太大内存，适合缩略图。
- inJustDecodeBounds：当 inJustDecodeBounds 为 true 时，执行 decodexxx 方法时，BitmapFactory 只会解析图片的原始宽高信息，并不会真正的加载图片
- inPreferredConfig：用于配置图片解码方式，对应的类型 Bitmap.Config。如果非null，则会使用它来解码图片。默认值为是 Bitmap.Config.ARGB_8888
- inBitmap：在 Android 3.0 开始引入了 inBitmap 设置，通过设置这个参数，在图片加载的时候可以使用之前已经创建了的 Bitmap，以便节省内存，避免再次创建一个Bitmap。在 Android4.4，新增了允许 inBitmap 设置的图片与需要加载的图片的大小不同的情况，只要 inBitmap 的图片比当前需要加载的图片大就好了。

通过 BitmapFactory.Options 的这些参数，我们就可以按一定的采样率来加载缩小后的图片，然后在 ImageView 中使用缩小的图片这样就会降低内存占用避免【OOM】，提高了 Bitamp 加载时的性能。

这其实就是我们常说的图片尺寸压缩。尺寸压缩是压缩图片的像素，一张图片所占内存大小的计算方式： **图片类型＊宽＊高**，通过改变三个值减小图片所占的内存，防止OOM，当然这种方式可能会使图片失真 。

## Bitmap.Config

```java
public static enum Config {
    ALPHA_8,//每个像素占用1byte内存
    RGB_565,//每个像素占用2byte内存
    ARGB_4444,//每个像素占用2byte内存
    ARGB_8888;//每个像素占用4byte内存；默认模式
}
```

