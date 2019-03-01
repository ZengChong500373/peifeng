package org.telegram.peifeng.yima;

import android.text.TextUtils;



import org.telegram.peifeng.listener.PeiFengHttpCallBack;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.RxContactUtils;
import org.telegram.peifeng.utils.ToastUtils;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class YmHttpSdk {
    public static String TAG = "YmHttpSdk";
    private static Disposable disposable;
    private static Disposable disposable2;
    public static void init() {
        if (disposable2 != null) {
            MyLog.write2File("YmHttpSdk getCode set disposable2 null" );
            disposable2.dispose();
            disposable2=null;
        }
        disposable2= RxContactUtils.getInit()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {

                            MyLog.write2File("易码平台初始化成功");
                            disposable2.dispose();
                            disposable2 = null;
                            return;
                        }
                        MyLog.write2File("易码平台初始化失败");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    MyLog.write2File("易码平台异常初始化失败");
                        disposable2.dispose();
                        disposable2 = null;
                    }
                });
    }

    public static void getCode(final String num, PeiFengHttpCallBack<String> callBack) {
        MyLog.write2File("phone=" + num + " to getCode");
        if (disposable != null) {
            MyLog.write2File("YmHttpSdk getCode set disposable null" );
            disposable.dispose();
            disposable=null;
        }
        disposable = RxContactUtils.getCode(num).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                MyLog.write2File("YmHttpSdk phone num="+num+"sms code is "+s);
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (s.contains("2007")) {
                    MyLog.write2File("YmHttpSdk getcode is 2007 in 80Line ");
                    callBack.onSuccess(s);
                    disposable.dispose();
                    disposable = null;
                    return;
                }
                if (s.contains("is")) {
                    MyLog.write2File("YmHttpSdk getCode contain is  ");
                    String str = s.split("is")[1];
                    callBack.onSuccess(str.trim());

                    disposable.dispose();
                    disposable = null;
                    return;
                }

                MyLog.write2File("YmHttpSdk getCode  in the end not choose  ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                MyLog.write2File("YmHttpSdk getCode exception return 2007");
                callBack.onSuccess("2007");
                disposable.dispose();
                disposable = null;
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                YmHttpMethods.getInstance().releaseNum(num);
                MyLog.write2File("YmHttpSdk getCode 完成 retrun 2007");
                callBack.onSuccess("2007");
                disposable.dispose();
                disposable = null;
            }
        });

    }

}
