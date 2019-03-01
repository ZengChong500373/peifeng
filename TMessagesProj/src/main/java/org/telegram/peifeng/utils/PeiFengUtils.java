package org.telegram.peifeng.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.PeiFengSdk;
import org.telegram.peifeng.bean.AppIdBean;
import org.telegram.peifeng.http.SdHttpMethods;


public class PeiFengUtils {

    public static AppIdBean getAppId(){
        String appStr="";
        int appid=getAppIdInLoacl();
        String appHash=getAppHash();
        if (appid==-1){
            MyLog.write2File("rx getAppId appid==-1 开始网络请求");
            appStr = SdHttpMethods.getInstance().dynamicAppId();
            if (TextUtils.isEmpty(appStr)) {
                MyLog.write2File("rx getAppId appid==-1 请求到null");
                return new AppIdBean(-1,"");
            } else {
                MyLog.write2File("rx getAppId appid==-1 请求到数据");
                return new Gson().fromJson(appStr, AppIdBean.class);
            }
        }else {
            MyLog.write2File("rx getAppId  缓存数据 appid="+appid+" apphash="+appHash);
            return new AppIdBean(appid,appHash);
        }
    }


    public static void setCurrentFlag(int flag) {
        SpUtils.put(PeiFengContant.CURRENT_FLAG, flag);

    }

    public static int getCurrentFlag() {
        return (int) SpUtils.get(PeiFengContant.CURRENT_FLAG, 0);
    }

    public static void setphoneArea(String flag) {
        SpUtils.put(PeiFengContant.PHONE_AREA, flag);

    }

    public static void setAppIdInLoacl(int appid){
        SpUtils.put(PeiFengContant.APP_ID, appid);
    }
    public static int getAppIdInLoacl(){
        return (int) SpUtils.get(PeiFengContant.APP_ID, 6);
    }
    public static void setAppHash(String str){
        SpUtils.put(PeiFengContant.APP_HASH, str);
    }
    public static String getAppHash(){
        return (String) SpUtils.get(PeiFengContant.APP_HASH, "eb06d4abfb49dc3eeb1aeb98ae0f581e");
    }
    public static String getphoneArea() {
        return (String) SpUtils.get(PeiFengContant.PHONE_AREA, "");
    }

    public static void setCurrentSizePosition(int position) {
        SpUtils.put(PeiFengContant.CURRENT_SIZE_POSITION, position);
    }

    public static int getCurrentSizePosition() {
        return (int) SpUtils.get(PeiFengContant.CURRENT_SIZE_POSITION, 0);
    }

    public static  void setAllSendMsgCount(int position){
        SpUtils.put("msgCount", position);
    }
    public static int getAllSendMsgCount() {
        return (int) SpUtils.get("msgCount", 0);
    }
    public static void setSwitchAccountTimes(){
        int time=getSwitchAccountTimes()+1;
        SpUtils.put("accountTime",time);
    }
    public static int getSwitchAccountTimes(){
        return (int) SpUtils.get("accountTime",0);
    }

    public static void setToken(String token) {
        SpUtils.put(PeiFengContant.YM_TOKEN, token);

    }

    public static String getToken() {
        return (String) SpUtils.get(PeiFengContant.YM_TOKEN, "");
    }
    public static void setLastBanTime(long time) {
        SharedPreferences sp = PeiFengSdk.getContext().getSharedPreferences("config", 0);
        sp.edit().putLong("ban_time", time).commit();

    }

    public static long getLastBanTime() {
        SharedPreferences sp = PeiFengSdk.getContext().getSharedPreferences("config", 0);
        return sp.getLong("ban_time", 0);
    }

    public static Boolean isGo2Main(){
       long intervalTime= System.currentTimeMillis()-getLastBanTime();
       if (intervalTime>1000*60*5){

           MyLog.write2File("can go 2 mian");
           return true;
       }
        MyLog.write2File("can not go 2 mian");
        return false;
    }
}
