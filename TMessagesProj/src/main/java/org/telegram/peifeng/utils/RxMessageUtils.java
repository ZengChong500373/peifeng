package org.telegram.peifeng.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import org.telegram.peifeng.bean.CurrentResultBean;
import org.telegram.peifeng.bean.PhonesBean;


import org.telegram.peifeng.http.SdMessageHttpMethods;
import org.telegram.peifeng.message.PeiFengMessageManager;
import org.telegram.tgnet.TLRPC;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxMessageUtils {


    public static Observable<Boolean> isCanStart() {
        return Observable.interval(0, 60, TimeUnit.SECONDS).map(new Function<Long, Boolean>() {
            @Override
            public Boolean apply(Long aLong) throws Exception {
                TLRPC.User user = ContactUtils.getInstance().getCurrentUser();
                if (user!=null){
                    MyLog.write2File("RxMessageUtils account not null can start");
                    return true;
                }
                MyLog.write2File("RxMessageUtils account is null can not start");
             return false;

            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }
    public static Observable<PhonesBean> getPhoneList() {
        return Observable.interval(10, 20, TimeUnit.SECONDS).map(new Function<Long, PhonesBean>() {
            @Override
            public PhonesBean apply(Long aLong) throws Exception {
               String httpstr = SdMessageHttpMethods.getInstance().getPhoneList();
//                 String httpstr="{\"list\":[\"8615387906462\",\"8615510012672\",\"8615510012671\",\"8615510012601\",\"8615361547797\",\"8615526450082\",\"8615362998052\",\"8615363168105\",\"8615510012302\",\"8615389906004\"],\"msg\":\"你好\"}";
                MyLog.write2File("httpstr ="+httpstr);
                if(TextUtils.isEmpty(httpstr)){
                    MyLog.write2File("RxMessageUtils getphoneList is null");
                    return new PhonesBean();
                }
                MyLog.write2File("RxMessageUtils getphoneList success");
                return new Gson().fromJson(httpstr, PhonesBean.class);
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }

    public static Observable<CurrentResultBean> dealData(){
        return Observable.interval(5, 12, TimeUnit.SECONDS).map(new Function<Long, CurrentResultBean>() {
            @Override
            public CurrentResultBean apply(Long aLong) throws Exception {
                return PeiFengMessageManager.getInstance().dealCurrentData(aLong);

            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io());
    }




}
