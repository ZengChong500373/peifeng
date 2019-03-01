package org.telegram.peifeng.message;

import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.bean.CurrentResultBean;
import org.telegram.peifeng.bean.PhonesBean;
import org.telegram.peifeng.bean.UserInfoBean;
import org.telegram.peifeng.http.SdMessageHttpMethods;
import org.telegram.peifeng.listener.TelegramDataCallBack;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.PeiFengServerMessage;
import org.telegram.peifeng.utils.PeiFengUtils;
import org.telegram.peifeng.utils.RxMessageUtils;
import org.telegram.peifeng.utils.TelegramUtils;
import org.telegram.peifeng.utils.ToastUtils;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PeiFengMessageManager implements TelegramDataCallBack<ArrayList<TLRPC.User>>, NotificationCenter.NotificationCenterDelegate {

    private static final PeiFengMessageManager ourInstance = new PeiFengMessageManager();

    public static PeiFengMessageManager getInstance() {
        return ourInstance;
    }

    public static Boolean isStart = false;
    Disposable phonesDisposable, startDisposable, msgDisposable;
    public static LongSparseArray<UserInfoBean> userinfos;

    public static int position = 0;
    private static final Object loadSync = new Object();
    private static Boolean currentNotFinish = true;
    private static Long currentFinishTime = 0L;
    final public static int current_not_finish = 999;

    public static UserInfoBean currentUserInfoBean;
    public static int senType = -1;
    public static long sentId = -1;
    private static CurrentResultBean currentResultBean;

    private static int allCount=-1;
    public  static int sendErrorTimes=0;



    // 8.0 广播判断
    IntentFilter intentFilter=null;
    SendMsgBroadCast sendMsgBroadCast=null;
    private PeiFengMessageManager() {
        NotificationCenter.getInstance(UserConfig.selectedAccount).addObserver(this, NotificationCenter.messageReceivedByAck);
        NotificationCenter.getInstance(UserConfig.selectedAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
        NotificationCenter.getInstance(UserConfig.selectedAccount).addObserver(this, NotificationCenter.messageSendError);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            intentFilter=new IntentFilter("com.peifeng.sendtext");
            sendMsgBroadCast=new SendMsgBroadCast();
            ApplicationLoader.applicationContext.registerReceiver(sendMsgBroadCast,intentFilter);
        }
    }

    public void start() {
        if (isStart) {
            MyLog.write2File("PeiFengMessageManager 已经 start了");
            return;
        }
        startDisposable = RxMessageUtils.isCanStart().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean&&isStart==false) {
                    isStart = true;
                    getPhone();
                    startDisposable.dispose();
                    startDisposable = null;
                }
            }
        });
    }

    public void getPhone() {
        MyLog.write2File("message manager getphone");
        if (phonesDisposable == null) {
            phonesDisposable = RxMessageUtils.getPhoneList().subscribe(new Consumer<PhonesBean>() {
                @Override
                public void accept(PhonesBean phonesBean) throws Exception {
                    if (!TextUtils.isEmpty(phonesBean.getMsg())) {
                        TelegramUtils.telegramCheckContacts(phonesBean, PeiFengMessageManager.this);
                        userinfos = MessageUtils.list2Sparse(phonesBean);
                        phonesDisposable.dispose();
                        phonesDisposable = null;
                    }

                }
            });
        }

    }

private  int cc=0;
    @Override
    public void onDataCome(ArrayList<TLRPC.User> users) {
        MyLog.write2File("message manager onDataCome");
        userinfos = MessageUtils.calculateData(userinfos, users);
        Log.e("jyh",userinfos.toString());
        cc++;
        if (cc!=2){
            return;
        }
        msgDisposable = RxMessageUtils.dealData().subscribe(new Consumer<CurrentResultBean>() {
            @Override
            public void accept(CurrentResultBean data) throws Exception {
                dealCurrentProcessResult(data);
            }
        });
    }


    public CurrentResultBean dealCurrentData(Long aLong) {
        MyLog.write2File("dealCurrentData start "+aLong);
        Long time = System.currentTimeMillis() - currentFinishTime;
        if (Math.abs(time) < 6000) {
            return new CurrentResultBean(400, "两次操作小于6秒", current_not_finish);
        }
        if (currentNotFinish == false) {//currentNotFinish == false
            return new CurrentResultBean(400, "当前操作还在执行", current_not_finish);
        }
        if (allCount==-1){
            allCount=PeiFengUtils.getAllSendMsgCount();
        }
        long keyAtPosition = userinfos.keyAt(position);
        currentUserInfoBean = userinfos.get(keyAtPosition);
        MyLog.write2File("current position="+position);
        MyLog.write2File("dealCurrentData currentUserInfoBean phone="+currentUserInfoBean.getPhoneNum()+"exist ="+currentUserInfoBean.getExist());
        if (currentUserInfoBean.getExist()) {
            sentId = currentUserInfoBean.getId();
            MyLog.write2File("senType senType="+senType);
            TelegramUtils.sendMsgBroad(currentUserInfoBean.getMsg(), sentId);
            while (senType==-1){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (senType==PeiFengContant.SEND_ERROR){
//                if (sendErrorTimes>5){
//                    currentFinish();
//                    currentResultBean = new CurrentResultBean(200, "重复发送5次 还是失败" , 0);
//                    return currentResultBean;
//                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyLog.write2File("senType==PeiFengContant.SEND_ERROR resend="+sentId);
//                TelegramUtils.sendMsgBroad(currentUserInfoBean.getMsg(), sentId);
//                sendErrorTimes++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (senType==PeiFengContant.SEND_DEFAULT||senType==PeiFengContant.SEND_SENDING){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                senType = TelegramUtils.sendType(sentId);
                MyLog.write2File("while senType"+senType+" send id="+sentId);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (senType==PeiFengContant.SEND_SUCCESS) {
                Boolean isFinish = SdMessageHttpMethods.getInstance().reportMsgStatus(PeiFengContant.SEND_SUCCESS,currentUserInfoBean.getPhoneNum());
                MyLog.write2File("dealCurrentData isFinish="+isFinish+" senid="+sentId);
                while (!isFinish) {
                    isFinish = SdMessageHttpMethods.getInstance().reportMsgStatus(PeiFengContant.SEND_SUCCESS,currentUserInfoBean.getPhoneNum());
                    MyLog.write2File("dealCurrentData in while isFinish="+isFinish+" send id="+sentId);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            currentFinish();
            currentResultBean = new CurrentResultBean(200, "上报成功 发送成功" , 0);
            return currentResultBean;
        } else {
            Boolean isFail = SdMessageHttpMethods.getInstance().reportMsgStatus(PeiFengContant.NUM_BANNED,currentUserInfoBean.getPhoneNum());
            MyLog.write2File("dealCurrentData isFail="+isFail);
            while (!isFail) {
                isFail = SdMessageHttpMethods.getInstance().reportMsgStatus(PeiFengContant.NUM_BANNED,currentUserInfoBean.getPhoneNum());
                MyLog.write2File("dealCurrentData in while isFail="+isFail);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentFinish();
            currentResultBean = new CurrentResultBean(200, "上报成功 禁号成功" , 1);
            return currentResultBean;
        }

    }

    private void dealCurrentProcessResult(CurrentResultBean bean) {
        if (bean.getStatus() == 200) {
            dealFlag(bean);
        } else {
            MyLog.write2File("dealCurrentProcessResult statu!=200 "+bean.getMsg());
            ToastUtils.getInstance().show(bean.getMsg());
        }
    }

    private void dealFlag(CurrentResultBean bean) {
        if (position < userinfos.size()-1) {
            position++;
        } else {
            msgDisposable.dispose();
            msgDisposable=null;
            position = 0;
            userinfos.clear();
            cc=0;
            getPhone();
            MyLog.write2File("10 个 完了");
        }
        allCount++;

        PeiFengUtils.setAllSendMsgCount(allCount);

        MyLog.writeSwitchAccount("all count is="+allCount);
        if (allCount==40){
            PeiFengServerMessage.sendMessage(PeiFengContant.CLOSE_SELF);
        }
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.messageReceivedByAck || id == NotificationCenter.messageReceivedByServer || id == NotificationCenter.messageSendError) {
            if (currentUserInfoBean!=null&&sentId == currentUserInfoBean.getId()) {
                senType = TelegramUtils.sendType(currentUserInfoBean.getId());
                MyLog.write2File("peifengMessage didReceivedNotification sentId="+sentId+" sendType="+senType);
            }else {
                MyLog.write2File("peifengMessage didReceivedNotification is not our msg");
            }
        }
    }

    public void currentFinish() {
        MyLog.write2File("currentFinish");
        synchronized (loadSync) {
            currentNotFinish = true;
            currentFinishTime = System.currentTimeMillis();
            senType=-1;
            currentUserInfoBean=null;
            sentId=-1;
            sendErrorTimes=0;
        }
    }
}
