package org.telegram.peifeng;

import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.telegram.messenger.ApplicationLoader;

public class PeiFengContant {
    /**
     * 易码平台 请求的base url'
     */
    public static String YM_HTTP_BASE = "http://api.fxhyd.cn/UserInterface.aspx?";
    /**
     * 易码平台token
     */
    public static String YM_TOKEN = "ym_token";


    /**
     * 公司http请求的 base url
     */
    public static String SD_HTTP_BASE_URL = "http://106.14.106.240:8088/";

    /**
     * 获取手机号码
     */
    public static String SD_HTTP_PICKPHONEAREA = SD_HTTP_BASE_URL + "pickPhoneArea?deviceId=";

    public static String SD_HTTP_REGISTEREDPHONES = SD_HTTP_BASE_URL + "registeredPhones?phones=";

    public static String SD_HTTP_HEART = SD_HTTP_BASE_URL + "deviceHeartbeat?deviceId=" + getImei();
    public static String CURRENT_FLAG = "current_flag";

    public static String PHONE_AREA = "phone_area";
    public static String CURRENT_SIZE_POSITION = "current_size_position";


    public static String DYNAMIC_APPID = SD_HTTP_BASE_URL + "getAppInfo?deviceId=" + getImei();

    public static String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService(ApplicationLoader.applicationContext.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission")
        String imei = telephonyManager.getDeviceId();

        if (TextUtils.isEmpty(imei)) {
            imei = "";
        }

        return imei;
    }


    public static String GET_PHONELIST = SD_HTTP_BASE_URL + "getPhoneList?deviceId=" + getImei();

    public static String MSG_STATUS_URL=SD_HTTP_BASE_URL+"message/"+getImei()+"/";
    public static final int WAITE = 12;
    public static final int TEST3TIMES = 9;
    public static final int BANNED = 10;
    public static final int WAIT86400 = 11;
    public static final int API_ID_INVALID = 2;
    public static final int CLOSE_SELF = 3;
    public static final int TWO_STEP_CODE = 13;
    public static int PEIFENG_STATUS = -1;


    public static final String APP_ID = "APP_ID";
    public static final String APP_HASH = "APP_HASH";

    public static final int SEND_ERROR=0;
    public static final int SEND_SENDING=1;
    public static final int SEND_SUCCESS=2;
    public static final int SEND_DEFAULT=3;
    public static final int NUM_BANNED=4;

}
