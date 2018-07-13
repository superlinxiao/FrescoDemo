package com.example.administrator.frescodemo.imageloader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * Description
 * fresco config
 * Author lizheng
 * 参考：https://www.jianshu.com/p/8ff81be83101
 * Create Data  2018\7\9 0009
 */
public class ImageLoaderConfig {

  private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

  private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

  private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

  private static final int MAX_DISK_SMALL_LOW_SPACE_CACHE_SIZE = 5 * ByteConstants.MB;
  private static final String TAG = "image_config";

  private ImagePipelineConfig sImagePipelineConfig;

  /**
   * Creates config using android http stack as network backend.
   * tips:
   * setDownsampleEnabled
   * 如果开启该选项，pipeline 会向下采样你的图片，代替 resize 操作。
   * 你仍然需要像上面那样在每个图片请求中调用 setResizeOptions 。
   * 向下采样在大部分情况下比 resize 更快。除了支持 JPEG 图片，它还支持 PNG 和 WebP(除动画外) 图片。
   * 但是目前还有一个问题是它在 Android 4.4 上会在解码时造成更多的内存开销（相比于Resizing）。
   * 这在同时解码许多大图时会非常显著，我们希望在将来的版本中能够解决它并默认开启此选项。
   * <p>
   * <p>
   * tips:
   * 配置Image pipeline时 需要传递一个 ProgressiveJpegConfig. 的实例。
   * 这个实例需要完成两个事情:
   * 返回下一个需要解码的扫描次数
   * 确定多少个扫描次数之后的图片才能开始显示。
   * <p>
   * 下面的实例中，为了实现节省CPU，并不是每个扫描都进行解码。
   * <p>
   * 注意:
   * 每次解码完之后，调用getNextScanNumberToDecode, 等待扫描值大于返回值，才有可能进行解码。
   * 假设，随着下载的进行，下载完的扫描序列如下: 1, 4, 5, 10。那么：
   * <p>
   * 首次调用getNextScanNumberToDecode返回为2， 因为初始时，解码的扫描数为0。
   * 那么1将不会解码，下载完成4个扫描时，解码一次。下个解码为扫描数为6
   * 5不会解码，10才会解码
   */
  public ImagePipelineConfig getImagePipelineConfig(final Context context) {
    if (sImagePipelineConfig == null) {
      /**
       * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
       * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
       * 2、一些内存清理软件可以扫描出来，进行内存的清理
       */
      File fileCacheDir = context.getApplicationContext().getCacheDir();
      DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
          .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
          .setBaseDirectoryPath(fileCacheDir)
          .build();

      DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
          .setBaseDirectoryPath(fileCacheDir)
          .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
          .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
          .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_LOW_SPACE_CACHE_SIZE)
          .build();
      // 当内存紧张时采取的措施
      MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
      memoryTrimmableRegistry.registerMemoryTrimmable(trimType -> {
        final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
        if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
            ) {
          // 清除内存缓存
          Fresco.getImagePipeline().clearMemoryCaches();
        }
      });
      sImagePipelineConfig = ImagePipelineConfig.newBuilder(context)
          .setBitmapsConfig(Bitmap.Config.RGB_565) // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
          .setDownsampleEnabled(true) // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使
          // 设置Jpeg格式的图片支持渐进式显示
          .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
              //下一次
              return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
              boolean isGoodEnough = (scanNumber >= 5);
              return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
          })
          .setMemoryTrimmableRegistry(memoryTrimmableRegistry) // 报内存警告时的监听
          // 设置内存配置
          .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier(
              (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
          .setMainDiskCacheConfig(mainDiskCacheConfig) // 设置主磁盘配置
          .setSmallImageDiskCacheConfig(smallDiskCacheConfig) // 设置小图的磁盘配置
          .setResizeAndRotateEnabledForNetwork(true)// 对网络图片进行resize处理，减少内存消耗
          .setDownsampleEnabled(true)
          .build();
    }
    return sImagePipelineConfig;
  }

}
