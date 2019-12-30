package com.example.administrator.frescodemo;

import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 小结:
 * 1.RGB_565如果解析24位图片，确实会减少内存的使用，但是如果用来解析32位图片，反而比ARGB_8888的内存使用要大。
 *
 * 原因没有具体分析，查看内存后，发现用RGB_565解析32位图片，会解析两次，一次是按照RGB_565解析，一次是按照ARGB_8888解析。
 *
 * 2.android 9不支持http，需要在manifest中进行配置才能支持
 *
 *
 */
public class MainActivity extends AppCompatActivity {

  //    public static final String url = "http://pic7.nipic.com/20100518/3409334_031036048098_2.jpg";
  public static final String url = "http://s3.cn-north-1.amazonaws.com.cn/cn-synative/cdn/panel.synative.com/bg/1529467278_94029.png";
  //带透明的图片
//  public static final String url = "http://s3.cn-north-1.amazonaws.com.cn/cn-synative/cdn/panel.synative.com/icon/1524648441_62621.jpeg";
  //渐进式jpg url
//  public static final String url = "http://i2.bvimg.com/652950/0f44b5e3cbd09602.jpg";
//  public static final String url = "http://i2.bvimg.com/652950/0f77d06ec0386f43.jpg";
//  public static final String url = "http://i2.bvimg.com/652950/bf6aa0b16fc93e12.jpg";
//  public static final String url = "http://i2.bvimg.com/652950/f390e1318656e08e.jpg";
//  public static final String url = "http://i2.bvimg.com/652950/5bafb6ba4e200a85.jpg";
//  public static final String url = "http://i2.bvimg.com/652950/32bc35f91f15d1a1.jpg";
  private static final String TAG = "frescoDemo";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("MyApplication", "onCreate  ");
    setContentView(R.layout.activity_main);
    SimpleDraweeView imageView = findViewById(R.id.my_image_view);
//    imageView.setImageURI(url);


    ImageDecodeOptions build = ImageDecodeOptions.newBuilder().setBitmapConfig(Config.ARGB_8888)
        .build();
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
        .setImageDecodeOptions(
            build)
        .build();
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(request)
        .setOldController(imageView.getController())
        .build();
    imageView.setController(controller);

//    startService(new Intent(this, TestService.class));
    //加载gif
//        String str = "res:///" + R.drawable.pic_ball_popup_tips;
//        String str = "res:///" + R.drawable.test;
//        UtilsTools.setControllerListener(imageView, str, getResources().getDimensionPixelOffset(R.dimen.img_dp));
//        imageView.setImageURI(url);
//1024 500
//    ImageUtils.display(imageView, url);
//    ImageUtils.prefetchPhoto(Uri.parse(url));

//    ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
//      @Override
//      public void onFinalImageSet(
//          String id,
//          @Nullable ImageInfo imageInfo,
//          @Nullable Animatable anim) {
//        if (imageInfo == null) {
//          return;
//        }
//        QualityInfo qualityInfo = imageInfo.getQualityInfo();
//        Log.d(TAG, "Final image received! " +
//            String.format("Size %d x %d" +
//                    "Quality level %d, good enough: %s, full quality: %s",
//                imageInfo.getWidth(),
//                imageInfo.getHeight(),
//                qualityInfo.getQuality(),
//                qualityInfo.isOfGoodEnoughQuality(),
//                qualityInfo.isOfFullQuality()));
//      }
//
//      @Override
//      public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//        Log.d(TAG, "Intermediate image received");
//      }
//
//      @Override
//      public void onFailure(String id, Throwable throwable) {
//        FLog.e(getClass(), throwable, "Error loading %s", id);
//      }
//    };
//
//    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//        .setProgressiveRenderingEnabled(true)
//        .build();
//    DraweeController controller = Fresco.newDraweeControllerBuilder()
//        .setImageRequest(request)
//        .setOldController(imageView.getController())
//        .setControllerListener(controllerListener)
//        .build();
//    imageView.setController(controller);
  }


  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    Log.d("MyApplication", "mainActivity current state  " + level);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d("MyApplication", "onSaveInstanceState");
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Log.d("MyApplication", "onRestoreInstanceState");
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onRestoreInstanceState(savedInstanceState, persistentState);
    Log.d("MyApplication", "onRestoreInstanceState 2");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d("MyApplication", "onDestroy");
  }
}
