package org.telegram.peifeng.http;


import android.content.Intent;

import org.telegram.peifeng.bean.AppIdBean;
import org.telegram.peifeng.listener.PeiFengHttpCallBack;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.RxContactUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SdRxHttpMethods {
    private static Disposable appidDisposable;
    public  static void getAppId(Intent intent,PeiFengHttpCallBack<AppIdBean> callBack){
        if (appidDisposable != null) {
            MyLog.write2File("YmHttpSdk getCode set disposable null" );
            appidDisposable.dispose();
            appidDisposable=null;
        }
        appidDisposable=RxContactUtils.getAppId(intent).subscribe(new Consumer<AppIdBean>() {
            @Override
            public void accept(AppIdBean appIdBean) throws Exception {
                   if (appIdBean!=null&&appIdBean.getAppId()!=-1){
                       MyLog.write2File(" nihao wo hao dajiahao");
                       callBack.onSuccess(appIdBean);
                       appidDisposable.dispose();
                       appidDisposable=null;
                   }else {
                       MyLog.write2File(" nibuhao  wobuhao dajiabuhao");
                   }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                MyLog.write2File("SdRxHttpMethods getappid exception = "+throwable.toString());
            }
        });
    }
}
