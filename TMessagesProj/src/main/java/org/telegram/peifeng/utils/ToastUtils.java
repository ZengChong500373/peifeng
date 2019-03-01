package org.telegram.peifeng.utils;

import android.widget.Toast;

import org.telegram.peifeng.PeiFengSdk;


/**
 * Created by sunxx on 2016/8/3.
 */
public class ToastUtils {
    private static ToastUtils toastUtils;
    private ToastUtils() {
    }
    public static ToastUtils getInstance() {
        if (toastUtils==null){
            toastUtils=new ToastUtils();
        }
        return toastUtils;
    }

    public void show(String str) {
        MyLog.write2File(str);
        Toast.makeText(PeiFengSdk.getContext(), str, Toast.LENGTH_LONG).show();
    }
}
