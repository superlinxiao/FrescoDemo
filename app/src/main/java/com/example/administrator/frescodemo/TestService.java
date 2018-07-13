package com.example.administrator.frescodemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Description
 * Author lizheng
 * Create Data  2018\7\12 0012
 */
public class TestService extends Service {

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d("MyApplication", "service onCreate");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Log.d("MyApplication", "service onBind");
    return null;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    Log.d("MyApplication", "service onUnbind");
    return super.onUnbind(intent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d("MyApplication", "service onDestroy");
  }
}
