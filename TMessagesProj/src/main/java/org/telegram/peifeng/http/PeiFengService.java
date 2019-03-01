package org.telegram.peifeng.http;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.telegram.peifeng.utils.MyLog;

public class PeiFengService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true){
                   SdHttpMethods.getInstance().deviceHeartbeat();
                   try {
                       Thread.sleep(60000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }).start();
    }
}
