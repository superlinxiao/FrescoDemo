package com.example.administrator.frescodemo;

import android.app.Application;
import android.util.Log;

import com.example.administrator.frescodemo.imageloader.ImageLoaderConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.logging.Logger;

/**
 * Description
 * Author lizheng
 * Create Data  2018-03-17
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this,new ImageLoaderConfig().getImagePipelineConfig(this));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == 15) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            if (imagePipeline != null) {
                imagePipeline.clearMemoryCaches();
            }
        }
        Log.d("MyApplication", "current state  " + level);
    }
}
