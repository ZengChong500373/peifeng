package org.telegram.peifeng.utils;

import android.content.Intent;
import android.os.Bundle;

public class PeiFengTimeUtils {
    public  static String serverType="serverType";
    public static int getInitCloseTime(Intent intent){
        if (intent!=null&&intent.getExtras()!=null){
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt(serverType);
            if (type==10){
                MyLog.write2File("getInitCloseTime return 480");
                return 480;
            }else {
                MyLog.write2File("getInitCloseTime return 120");
                return 120;
            }
        }
        MyLog.write2File("getInitCloseTime return 120");
        return 120;
    }
    public static int getGetAppIdStartTime(Intent intent){
        if (intent!=null&&intent.getExtras()!=null){
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt(serverType);
            if (type==10){
                MyLog.write2File("getGetAppIdStartTime return 300");
                return 300;
            }else {
                MyLog.write2File("getGetAppIdStartTime return 0");
                return 0;
            }
        }
        MyLog.write2File("getGetAppIdStartTime return 0");
        return 0;
    }
}
