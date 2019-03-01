package org.telegram.peifeng.http;

import android.text.TextUtils;


import com.google.gson.Gson;

import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.PeiFengSdk;
import org.telegram.peifeng.bean.ReportMsgBean;
import org.telegram.peifeng.utils.MyLog;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class SdMessageHttpMethods {
    private static final SdMessageHttpMethods ourInstance = new SdMessageHttpMethods();


    public static SdMessageHttpMethods getInstance() {
        return ourInstance;
    }

    public String getPhoneList() {
        MyLog.write2File(PeiFengContant.GET_PHONELIST);
        Request request = new Request.Builder()
                .url(PeiFengContant.GET_PHONELIST)
                .build();
        try {
            Response response = PeiFengSdk.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.write2File(e.toString());
            return "";
        }
        return "";
    }
    public static Boolean reportMsgStatus(int status,String phoneNum) {
        MyLog.write2File(getMsgStatusUrl(status,phoneNum));
        Request request = new Request.Builder()
                .url(getMsgStatusUrl(status,phoneNum))
                .build();
        try {
            Response response = PeiFengSdk.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                ReportMsgBean      bean = new Gson().fromJson(body, ReportMsgBean.class);
                if (bean!=null||!TextUtils.isEmpty(bean.getStatus())){
                    String isReport= bean.getStatus();
                    if (!TextUtils.isEmpty(isReport) && "ok".equalsIgnoreCase(isReport)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.write2File(e.toString());
            return false;
        }
        return false;
    }

    public static String getMsgStatusUrl(int status,String phoneNum){
        String MsgStatus="";
        if (status==PeiFengContant.NUM_BANNED){
            MsgStatus="BANNED";
        }
        if (status==PeiFengContant.SEND_SUCCESS){
            MsgStatus="FINISH";
        }
        if (status==PeiFengContant.SEND_ERROR){
            MsgStatus="FAIL";
        }
        return PeiFengContant.MSG_STATUS_URL+MsgStatus+"/"+phoneNum;
    }
}
