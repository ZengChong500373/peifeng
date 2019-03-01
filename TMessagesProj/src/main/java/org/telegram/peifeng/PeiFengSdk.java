package org.telegram.peifeng;

import android.content.Context;
import android.os.Looper;

import org.telegram.peifeng.yima.YmHttpSdk;
import org.telegram.peifeng.utils.CrashHandler;

import okhttp3.OkHttpClient;


public class PeiFengSdk {
    /**
     * 上下文对象
     * */
    private static Context mContext;
    private final static OkHttpClient client = new OkHttpClient();
    public static void init(Context initContext){
        mContext=initContext;
        CrashHandler.getInstance().init(mContext);
        YmHttpSdk.init();

    }
    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }
    public static OkHttpClient getClient(){
        return client;
    }

    /**
     * 判断是否在当前主线程
     * @return
     */
    public static boolean isOnMainThread(){
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
