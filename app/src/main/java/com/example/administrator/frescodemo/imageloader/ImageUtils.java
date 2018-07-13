package com.example.administrator.frescodemo.imageloader;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Description
 * Author lizheng
 * Create Data  2018\7\11
 */
public class ImageUtils {
  private static final String TAG = ImageUtils.class.getSimpleName();

  private ImageUtils() {

  }

  /**
   * 从网络加载
   */
  public static void display(SimpleDraweeView draweeView, String url) {
    if (TextUtils.isEmpty(url)) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    draweeView.setImageURI(url);
  }

  /**
   * 从本地文件加载
   */
  public static void display(SimpleDraweeView draweeView, File file) {
    if (file == null) {
      Log.e(TAG, "display: error the file is empty");
      return;
    }
    Uri uri = Uri.fromFile(file);
    if (uri == null) {
      return;
    }
    draweeView.setImageURI(uri);
  }

  /**
   * 从指定的uri加载
   */
  public static void display(SimpleDraweeView draweeView, Uri uri) {
    if (uri == null) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    draweeView.setImageURI(uri);
  }

  /**
   * 指定目标位置宽高，图片根据目标宽高进行缩小
   * <p>
   * tips1:
   * 缩略图预览功能
   * 如果本地的JPEG图有EXIF的缩略图，pipeline可以立刻返回这个缩略图。
   * DraweeView会先显示缩略图然后再显示完整的清晰大图。
   * 这个功能有限制，仅支持本地URI，并且是JPEG图片格式。
   * 这个也很简单，直接在imageRequest里面配置一下setLocalThumbnailPreviewsEnabled(true)即可
   * <p>
   * tips2:
   * 在指定一个新的controller的时候，使用setOldController，这可节省不必要的内存分配
   */
  public static void display(SimpleDraweeView draweeView, Uri uri, int width, int height) {
    if (uri == null) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(width, height))
        .setAutoRotateEnabled(true)
        .setLocalThumbnailPreviewsEnabled(true)
        .build();

    PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
        .setOldController(draweeView.getController())
        .setImageRequest(request)
        .build();

    draweeView.setController(controller);
  }


  /**
   * 指定目标位置宽高，图片根据目标宽高进行缩小
   */
  public static void display(SimpleDraweeView draweeView, String url, int width, int height) {
    if (TextUtils.isEmpty(url)) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    Uri uri = Uri.parse(url);
    display(draweeView, uri, width, height);
  }

  /**
   * 是否存放在small文件夹
   */
  public static void display(SimpleDraweeView draweeView, Uri uri, boolean isSmall) {
    if (uri == null) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    ImageRequest request;
    if (isSmall) {
      request = ImageRequestBuilder.newBuilderWithSource(uri)
          .setAutoRotateEnabled(true)
          .setCacheChoice(ImageRequest.CacheChoice.SMALL)
          .setLocalThumbnailPreviewsEnabled(true)
          .build();
    } else {
      request = ImageRequestBuilder.newBuilderWithSource(uri)
          .setAutoRotateEnabled(true)
          .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
          .setLocalThumbnailPreviewsEnabled(true)
          .build();
    }

    PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
        .setOldController(draweeView.getController())
        .setImageRequest(request)
        .build();

    draweeView.setController(controller);
  }


  /**
   * 是否存放在small文件夹，仅仅是放到small文件夹，不会改变图片大小
   */
  public static void display(SimpleDraweeView draweeView, String url, boolean isSmall) {
    if (TextUtils.isEmpty(url)) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    Uri uri = Uri.parse(url);
    display(draweeView, uri, isSmall);
  }

  /**
   * 预加载一张图片到磁盘缓存，
   */
  public static void prefetchPhoto(Uri uri) {
    if (uri == null) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setAutoRotateEnabled(true)
        .setRequestPriority(Priority.LOW)
        .build();
    Fresco.getImagePipeline().prefetchToDiskCache(request, null);
  }

  /**
   * 预加载一张图片到内存,并可以根据宽高进行缩放
   */
  public static void prefetchPhotoToBitmap(Uri uri, int width, int height) {
    if (uri == null) {
      Log.e(TAG, "display: error the url is empty");
      return;
    }
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(width, height))
        .setAutoRotateEnabled(true)
        .setRequestPriority(Priority.LOW)
        .build();
    Fresco.getImagePipeline().prefetchToBitmapCache(request, null);
  }
}
