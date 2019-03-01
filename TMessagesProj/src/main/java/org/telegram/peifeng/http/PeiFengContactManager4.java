package org.telegram.peifeng.http;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.bean.CurrentResultBean;
import org.telegram.peifeng.bean.PickPhoneBean;
import org.telegram.peifeng.listener.PeiFengHttpCallBack;
import org.telegram.peifeng.utils.ContactUtils;
import org.telegram.peifeng.utils.DataCleanManager;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.PeiFengUtils;
import org.telegram.peifeng.utils.RxContactUtils;
import org.telegram.peifeng.utils.ToastUtils;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;


import java.util.ArrayList;



import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class PeiFengContactManager4 implements StatusTest.StatuListener {
    final public static int init_start = 0;
    final public static int test_telegram = 1;

    final public static int current_not_finish = 999;
    private static int current_flag = -1;
    private static CurrentResultBean currentResultBean;
    public static String phoneArea = "";

    public static int current_position = -1;
    private static final Object loadSync = new Object();

    private static Boolean currentNotFinish = true;
    private static Long currentFinishTime = 0L;
    private static final PeiFengContactManager4 ourInstance = new PeiFengContactManager4();
    Disposable disposable;
    private static int fail_time = 0;
    private static PickPhoneBean pickPhoneBean;

    public static PeiFengContactManager4 getInstance() {
        return ourInstance;
    }

    public static Boolean isStart = false;


    public void start() {
        if (isStart) {
            MyLog.write2File("PeiFengContactManager4 已经 start了");
            return;
        }
        if (disposable == null) {
            disposable = RxContactUtils.getRx().subscribe(new Consumer<CurrentResultBean>() {
                @Override
                public void accept(CurrentResultBean resultBean) throws Exception {
                    dealCurrentProcessResult(resultBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    currentResultBean = new CurrentResultBean(400, throwable.toString(), current_flag);
                    dealCurrentProcessResult(currentResultBean);
                }
            });
        }

    }



    public CurrentResultBean dealCurrentProcess(Long along) {
        MyLog.write2File("dealCurrentProcess al0ng=" + along);
        if (!isStart) {
            TLRPC.User user = ContactUtils.getInstance().getCurrentUser();
            if (user != null) {
                isStart = true;
                currentFinishTime = 0L;
                MyLog.write2File("PeiFengContactManager4 start");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyLog.write2File("休眠30秒结束了");
            } else {
                return new CurrentResultBean(400, "...", current_not_finish);
            }
        }
        Long time = System.currentTimeMillis() - currentFinishTime;
        if (Math.abs(time) < 90000) {
            return new CurrentResultBean(400, "两次操作小于90秒", current_not_finish);
        }
        if (currentNotFinish == false) {//currentNotFinish == false
            return new CurrentResultBean(400, "当前操作还在执行", current_not_finish);
        }
        synchronized (loadSync) {
            currentNotFinish = false;
        }

        if (current_flag == -1) {
            current_flag = PeiFengUtils.getCurrentFlag();
            phoneArea = PeiFengUtils.getphoneArea();
            current_position = PeiFengUtils.getCurrentSizePosition();
        }

        if (current_flag == init_start) {

            //判断网络
            phoneArea = "";

            if (TextUtils.isEmpty(phoneArea)) {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    phoneArea = SdHttpMethods.getInstance().pickPhoneArea();
                    MyLog.write2File("the " + i + "  time get phoneArea ");
                    if (!TextUtils.isEmpty(phoneArea)) {
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(phoneArea)) {
                synchronized (loadSync) {
                    currentNotFinish = true;
                }
                return currentResultBean = new CurrentResultBean(200, "获取到到手机号码为空", current_flag);
            }
            PeiFengUtils.setphoneArea(phoneArea);
            MyLog.write2File("current_position=" + current_position);
            pickPhoneBean = new Gson().fromJson(phoneArea, PickPhoneBean.class);
            MyLog.write2File("app_id="+ BuildVars.APP_ID);

            MyLog.write2File("pickPhoneBean start=" + pickPhoneBean.getStart());
            if (pickPhoneBean == null) {
                currentNotFinish = true;
                return currentResultBean = new CurrentResultBean(400, "获取到到手机号码为空");
            }

            if ((current_position % 9 == 0 || fail_time > 0) && current_position != 0) {
                TLRPC.User user = ContactUtils.getInstance().getCurrentUser();
                if (user != null) {
                    String testPhone = SdHttpMethods.getInstance().getExamPhone(user.phone);
                    StatusTest.testIsStatusOk(testPhone, this);
                } else {
                    MyLog.write2File("account exception ");
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MyLog.write2File("phoneArea=" + phoneArea);
            ArrayList<TLRPC.TL_inputPhoneContact> list = ContactUtils.getInstance().getListUser(pickPhoneBean.getStart(), pickPhoneBean.getEnd());
            telegramCheckContacts(list);
            while (!currentNotFinish) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentResultBean = new CurrentResultBean(200, "上报成功" + current_flag, current_flag);
            return currentResultBean;
        }


        return new CurrentResultBean(200, "未找到匹配情况", 9);
    }

    private void dealCurrentProcessResult(CurrentResultBean bean) {
        if (bean.getStatus() == 200) {
            dealFlag(bean);
        } else {
            ToastUtils.getInstance().show(bean.getMsg());
        }
    }

    private void dealFlag(CurrentResultBean bean) {
        int flag = bean.getCurrentFlag();
        if (bean.getStatus() != 200) {
            return;
        }
        switch (flag) {
            case init_start:
            case test_telegram:
                current_flag = init_start;
                current_position++;
                break;
            case current_not_finish:
                break;
            default:
                current_flag = init_start;
                break;

        }
        PeiFengUtils.setCurrentFlag(current_flag);
        PeiFengUtils.setCurrentSizePosition(current_position);
        MyLog.write2File("setCurrentFlag=" + current_flag+" msg="+bean.getMsg());
    }

    private void getTContact(ArrayList<TLRPC.TL_inputPhoneContact> list) {
        final TLRPC.TL_contacts_importContacts req = new TLRPC.TL_contacts_importContacts();
        if (list == null) {
            MyLog.write2File("list null");
            return;
        }
        req.contacts = list;
        MyLog.write2File("frist=" +list.get(0).phone+" end="+list.get(list.size()-1).phone);
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new RequestDelegate() {
            @Override
            public void run(TLObject response, TLRPC.TL_error error) {
                if (error == null) {
                    final TLRPC.TL_contacts_importedContacts res = (TLRPC.TL_contacts_importedContacts) response;
                    int size = res.users.size();
                    MyLog.write2File("get user count=" + size);
                    ContactUtils.getInstance().getTelegramregisteredPhones(res.users);
                    reprotPeiFeng();
                } else {
                    currentFinish();
                    MyLog.write2File("error=" + error);
                }
            }
        }, ConnectionsManager.RequestFlagFailOnServerErrors | ConnectionsManager.RequestFlagCanCompress);

    }

    private void telegramCheckContacts(ArrayList<TLRPC.TL_inputPhoneContact> list) {
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                ConnectionsManager.getInstance(a).resumeNetworkMaybe();
                getTContact(list);
            }
        }

    }

    private void reprotPeiFeng() {
        ContactUtils.getInstance().SparseArray2String(new PeiFengHttpCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                TLRPC.User user = ContactUtils.getInstance().getCurrentUser();
                if (user == null) {
                    currentFinish();
                    return;
                }
                Boolean isOk = SdHttpMethods.getInstance().registeredPhones(result, pickPhoneBean, user.phone);
                while (!isOk) {
                    isOk = SdHttpMethods.getInstance().registeredPhones(result, pickPhoneBean, user.phone);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ContactUtils.getInstance().cleanSparseArray();
                currentFinish();
            }

            @Override
            public void onFail(String str) {
                currentFinish();
            }
        });
    }


    @Override
    public void statuIsOK(Boolean isOk, String num) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (isOk) {
                    fail_time = 0;
                    current_position = 0;
                    MyLog.write2File("PeiFengContactManager4 status ok ,dont need switch account");
                } else {
                    fail_time = fail_time + 1;
                    if (fail_time == 3) {
                        TLRPC.User user = ContactUtils.getInstance().getCurrentUser();
                        MyLog.write2File("PeiFengContactManager4 status not ok ,switch account");
                        if (user != null) {
                            SdHttpMethods.getInstance().NewPhoneUseless(user.phone, num);
                            MessagesController.getInstance(UserConfig.selectedAccount).performLogout(PeiFengContant.TEST3TIMES);
                            stop();
                        }
                    }

                }
            }
        });
    }
    public void currentFinish() {
        synchronized (loadSync) {
            currentNotFinish = true;
            currentFinishTime = System.currentTimeMillis();
        }
    }
    public void stop(){
        disposable.dispose();
        disposable = null;
        fail_time = 0;
        synchronized (loadSync) {
            isStart=false;
            currentNotFinish = true;
            currentFinishTime = 0L;
        }
    }
}
