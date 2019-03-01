package org.telegram.peifeng.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.telegram.peifeng.utils.DataCleanManager;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.TelegramUtils;

public class DeleDataBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent==null){
            return;
        }
       MyLog.write2File("DeleDataBroadCast");
        DataCleanManager.deleData();
    }
}
