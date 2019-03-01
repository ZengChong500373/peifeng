package org.telegram.peifeng.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.TelegramUtils;

public class SendMsgBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent==null){
            return;
        }
        String msg=intent.getStringExtra("msg");
        long userid=intent.getLongExtra("userid",-1);
        MyLog.write2File("SendMsgBroadCast onReceive msg="+msg);
        MyLog.write2File("SendMsgBroadCast onReceive userid="+userid);
        if (userid>0&&!TextUtils.isEmpty(msg)){
            TelegramUtils.processSendingText("https://www.baidu.com" , userid);
        }

    }
}
