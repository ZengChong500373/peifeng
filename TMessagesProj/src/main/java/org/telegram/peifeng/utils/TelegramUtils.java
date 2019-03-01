package org.telegram.peifeng.utils;

import android.content.Intent;
import android.os.Looper;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.bean.PhonesBean;
import org.telegram.peifeng.listener.TelegramDataCallBack;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;


public class TelegramUtils {
    public static void telegramCheckContacts(PhonesBean phonesBean, TelegramDataCallBack callBack) {
        ArrayList<TLRPC.TL_inputPhoneContact> list = ContactUtils.getInstance().getListUser(phonesBean);
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                ConnectionsManager.getInstance(a).resumeNetworkMaybe();
                getTContact(list,callBack);
            }
        }
    }
    private  static void getTContact(ArrayList<TLRPC.TL_inputPhoneContact> list,TelegramDataCallBack callBack) {
        final TLRPC.TL_contacts_importContacts req = new TLRPC.TL_contacts_importContacts();
        if (list == null) {
            MyLog.write2File("list null");
            return;
        }
        req.contacts = list;
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new RequestDelegate() {
            @Override
            public void run(TLObject response, TLRPC.TL_error error) {
                if (error == null) {
                    final TLRPC.TL_contacts_importedContacts res = (TLRPC.TL_contacts_importedContacts) response;
                    callBack.onDataCome(res.users);
                    MyLog.write2File("get user count=" + res.users.get(0).phone);
                } else {
                    callBack.onDataCome(null);
                    MyLog.write2File("error=" + error);
                }
            }
        }, ConnectionsManager.RequestFlagFailOnServerErrors | ConnectionsManager.RequestFlagCanCompress);
    }

      /**
       * sendType  发送消息类型
       * 0 发送失败
       *
       * 1发送中
       *
       * 2发送成功
       *
       * 默认 3
       *
       * userid <0 不查询发送状态
       * */
    public static int sendType(long userid){
        if (userid<0){
            return -999;
        }
        MessageObject message = MessagesController.getInstance(UserConfig.selectedAccount).dialogMessage.get(userid);
        if (message.isSendError()){
            return PeiFengContant.SEND_ERROR;
        }
        if (message.isSending()){
            return PeiFengContant.SEND_SENDING;
        }
        if (message.isSent()){
            return PeiFengContant.SEND_SUCCESS;
        }
        return PeiFengContant.SEND_DEFAULT;
    }
    public static  void sendMsgBroad(CharSequence text,long userid){
        Intent intent = new Intent();
        intent.setAction("com.peifeng.sendtext");
        intent.putExtra("msg",text);
        intent.putExtra("userid",userid);
        ApplicationLoader.applicationContext.sendBroadcast(intent);//发送标准广播
    }

    public static  void deleData(){
        Intent intent = new Intent();
        intent.setAction("com.peifeng.dele");
        ApplicationLoader.applicationContext.sendBroadcast(intent);//发送标准广播
    }
    public  static boolean processSendingText(CharSequence text,long userid) {
        MyLog.write2File("TelegramUtils processSendingText start");
        text = AndroidUtilities.getTrimmedString(text);
        int maxLength = MessagesController.getInstance(UserConfig.selectedAccount).maxMessageLength;
        if (text.length() != 0) {
            int count = (int) Math.ceil(text.length() / (float) maxLength);
            for (int a = 0; a < count; a++) {
                CharSequence[] message = new CharSequence[]{text.subSequence(a * maxLength, Math.min((a + 1) * maxLength, text.length()))};
                ArrayList<TLRPC.MessageEntity> entities = DataQuery.getInstance(UserConfig.selectedAccount).getEntities(message);
                SendMessagesHelper.getInstance(UserConfig.selectedAccount).sendMessage(message[0].toString(), userid, null, null, true, entities, null, null);
                MyLog.write2File("TelegramUtils processSendingText end");
            }
            return true;
        }
        return false;
    }


    public static boolean isOnMainThread(){
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
