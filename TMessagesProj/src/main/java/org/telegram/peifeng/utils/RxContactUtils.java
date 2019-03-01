package org.telegram.peifeng.utils;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.telegram.peifeng.bean.AppIdBean;
import org.telegram.peifeng.bean.CurrentResultBean;

import org.telegram.peifeng.http.PeiFengContactManager4;
import org.telegram.peifeng.http.SdHttpMethods;
import org.telegram.peifeng.yima.YmHttpMethods;
import org.telegram.peifeng.yima.YmUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxContactUtils {
    public static Observable<Boolean> getInit() {
        return Observable.interval(0, 30, TimeUnit.SECONDS).map(new Function<Long, Boolean>() {
            @Override
            public Boolean apply(Long aLong) throws Exception {
                String token = PeiFengUtils.getToken();
                if (TextUtils.isEmpty(token)) {
                    token = YmHttpMethods.getInstance().getToken();

                    if (TextUtils.isEmpty(token)) {
                        return false;
                    }
                    PeiFengUtils.setToken(token);
                    return true;
                } else {
                    return true;
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<AppIdBean> getAppId(Intent intent) {
        int delayTime=PeiFengTimeUtils.getGetAppIdStartTime(intent);
        MyLog.write2File("getAppid delayTime="+delayTime);
        return Observable.interval(delayTime, 20, TimeUnit.SECONDS).map(new Function<Long, AppIdBean>() {
            @Override
            public AppIdBean apply(Long aLong) throws Exception {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    MyLog.write2File("app bundle ==null");
                   return PeiFengUtils.getAppId();
                } else {
                    int type = bundle.getInt("type");
                    if (type != 0) {
                        MyLog.write2File("rx getAppId type !=0");
                        return new AppIdBean(-9);
                    } else {
                        return PeiFengUtils.getAppId();
                    }

                }


            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> Banned() {
        return Observable.interval(0, 30, TimeUnit.SECONDS).map(new Function<Long, Boolean>() {
            @Override
            public Boolean apply(Long aLong) throws Exception {
                if (aLong==0){
                    TelegramUtils.deleData();
                    return false;
                }
                Boolean isOk = SdHttpMethods.getInstance().deviceBanned();
                return isOk;
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<CurrentResultBean> getRx() {
        return Observable.interval(0, 2, TimeUnit.MINUTES).map(new Function<Long, CurrentResultBean>() {
            @Override
            public CurrentResultBean apply(Long aLong) throws Exception {
                return PeiFengContactManager4.getInstance().dealCurrentProcess(aLong);
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<String> getCode(String num) {
        return Observable.interval(0, 5, TimeUnit.SECONDS).takeUntil(Observable.timer(240, TimeUnit.SECONDS)).map(new Function<Long, String>() {
            @Override
            public String apply(Long aLong) throws Exception {
                String code = YmHttpMethods.getInstance().getCode(num);
                MyLog.write2File("RxContactUtils getCode =" + code);
                if (code.contains("2007")) {
                    YmHttpMethods.getInstance().releaseNum2(num);
                }
                return code;
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<String> getPhone() {
        return Observable.interval(0, 45, TimeUnit.SECONDS).map(new Function<Long, String>() {
            @Override
            public String apply(Long aLong) throws Exception {

                String code = YmHttpMethods.getInstance().syncGetPhoneNum();
                MyLog.write2File("RxContactUtils getPhone =" + code);
                String phoneNum = "";
                if (!TextUtils.isEmpty(code)) {
                    phoneNum = YmUtils.getInstance().getEffectiveValue(code);
                    MyLog.write2File("RxContactUtils phoneNum =" + phoneNum);
                }
                return phoneNum;
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

}
