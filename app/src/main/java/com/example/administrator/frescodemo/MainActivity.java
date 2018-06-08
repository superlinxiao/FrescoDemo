package com.example.administrator.frescodemo;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AbstractAnimatedDrawable;
import com.facebook.imagepipeline.image.ImageInfo;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    public static final String url = "http://pic7.nipic.com/20100518/3409334_031036048098_2.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDraweeView imageView = findViewById(R.id.my_image_view);

        //加载gif
        String str = "res:///" + R.drawable.pic_ball_popup_tips;
//        String str = "res:///" + R.drawable.test;
        UitlsToos.setControllerListener(imageView, str, getResources().getDimensionPixelOffset(R.dimen.img_dp));
    }

    static class UitlsToos {

        /**
         * 通过imageWidth 的宽度，自动适应高度
         * * @param simpleDraweeView view
         * * @param imagePath  Uri
         * * @param imageWidth width
         */
        public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath, final int imageWidth) {
            final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                    if (imageInfo == null) {
                        return;
                    }

                    //,自适应高度
                    int height = imageInfo.getHeight();
                    int width = imageInfo.getWidth();
                    layoutParams.width = imageWidth;
                    layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                    simpleDraweeView.setLayoutParams(layoutParams);

                    //动画只播放一次
                    try {
                        Field field = AbstractAnimatedDrawable.class.getDeclaredField("mTotalLoops");
                        field.setAccessible(true);
                        field.set(anim, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    Log.d("TAG", "Intermediate image received");
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    throwable.printStackTrace();
                }
            };
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(imagePath)).build();
            simpleDraweeView.setController(controller);
        }
    }

}
