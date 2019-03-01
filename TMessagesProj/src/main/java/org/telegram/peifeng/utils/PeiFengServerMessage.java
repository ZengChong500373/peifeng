package org.telegram.peifeng.utils;


import android.content.Intent;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.yima.YmUtils;

public class PeiFengServerMessage {
    public static void sendMessage(int type) {
        if (type == PeiFengContant.BANNED) {
            MyLog.write2File("PeiFengServerMessage   PeiFengContant.BANNED");
            PeiFengUtils.setAppIdInLoacl(-1);
            PeiFengUtils.setAppHash("");
        }else {
            MyLog.write2File("PeiFengServerMessage   PeiFengContant.Other");
        }
        MyLog.write2File("PeiFengServerMessage sendMessage="+type);
        Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.putExtra("type", type);
        intent.setAction("com.peifeng.action");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        ApplicationLoader.applicationContext.sendBroadcast(intent, "com.peifeng.permissions");   //发送广播
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        },2000 );
    }
}
