package org.telegram.peifeng.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.R;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.bean.AppIdBean;
import org.telegram.peifeng.http.SdRxHttpMethods;
import org.telegram.peifeng.listener.PeiFengHttpCallBack;
import org.telegram.peifeng.utils.DataCleanManager;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.PeiFengServerMessage;
import org.telegram.peifeng.utils.PeiFengTimeUtils;
import org.telegram.peifeng.utils.PeiFengUtils;
import org.telegram.peifeng.utils.RxContactUtils;
import org.telegram.peifeng.utils.SpUtils;
import org.telegram.ui.LaunchActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class PeiFengInitActivity extends Activity {
    TextView tv_peifeng;
    Disposable bannedDisposable;
    Intent intent;
    Disposable stopDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peifeng_init);
        tv_peifeng = findViewById(R.id.tv_peifeng);
        MyLog.write2File("onCreate");
        initData();
        stopActivity();
//        initGoLaunch(null);
    }


    private void stopActivity() {
        if (stopDisposable != null) {
            stopDisposable.dispose();
            stopDisposable = null;
        }
        stopDisposable = Observable.interval(PeiFengTimeUtils.getInitCloseTime(getIntent()), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        MyLog.writeSwitchAccount("进入节目2分钟关闭进程");
                        PeiFengServerMessage.sendMessage(1);
                    }
                });
    }


    private void initData() {
        SdRxHttpMethods.getAppId(getIntent(), new PeiFengHttpCallBack<AppIdBean>() {
            @Override
            public void onSuccess(AppIdBean result) {
                stopDisposable.dispose();
                stopDisposable = null;
                int appid = result.getAppId();
                if (appid == -9) {
                    report();
                } else {
                    initGoLaunch(result);
                }
            }

            @Override
            public void onFail(String string) {

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MyLog.write2File("onNewIntent");
        initData();

    }

    public void initGoLaunch(AppIdBean result) {
//      BuildVars.APP_ID = result.getAppId();
//        BuildVars.APP_HASH = result.getAppHash();
//        MyLog.write2File("切换app id=" + BuildVars.APP_ID);
//        PeiFengUtils.setAppIdInLoacl(result.getAppId());
//        PeiFengUtils.setAppHash(result.getAppHash());
        AndroidUtilities.runOnUIThread(ApplicationLoader::startPushService);
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (PeiFengUtils.isGo2Main()){
                    intent = new Intent(PeiFengInitActivity.this, LaunchActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    MyLog.write2File("ban time 间隔小于5min");
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }

            }
        }, 3000);
    }


    public void report() {
        DataCleanManager.deleData();
        if (PeiFengContant.PEIFENG_STATUS != -1) {
            if (bannedDisposable != null) {
                bannedDisposable.dispose();
                bannedDisposable = null;
            }
            MyLog.write2File("PeiFengInitActivity 上报 report");
            bannedDisposable = RxContactUtils.Banned().subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    MyLog.write2File("report  =" + aBoolean + "  loginout type=" + PeiFengContant.PEIFENG_STATUS);
                    if (aBoolean) {
                        startSwtich();
                        bannedDisposable.dispose();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    MyLog.write2File("PeiFengInitActivity throwable =" + throwable.toString());
                    startSwtich();
                    bannedDisposable.dispose();
                }
            });
            return;
        }
    }

    public void startSwtich() {
        MyLog.write2File("initActivity startSwtich");
        if (PeiFengContant.PEIFENG_STATUS == PeiFengContant.TEST3TIMES||PeiFengContant.PEIFENG_STATUS==PeiFengContant.TWO_STEP_CODE) {
            MyLog.write2File("initActivity startSwtich   PeiFengContant.TEST3TIMES");
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    PeiFengContant.PEIFENG_STATUS = -1;
                    intent = new Intent(PeiFengInitActivity.this, LaunchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 5000);
            return;
        } else {
            PeiFengServerMessage.sendMessage(PeiFengContant.PEIFENG_STATUS);
        }

    }
}





