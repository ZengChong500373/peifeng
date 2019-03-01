package org.telegram.peifeng.yima;


import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.PeiFengUtils;



public class YmUtils {
    private static YmUtils utils = new YmUtils();




    private YmUtils() {
    }

    public static YmUtils getInstance() {
        return utils;
    }





    public String getEffectiveValue(String str) {
        if (str.contains("|")) {
            return str.split("\\|")[1];
        }
        return "";
    }


    String url = "";

    public String getTokenUrl() {
        url = PeiFengContant.YM_HTTP_BASE + "action=login&username=847145851&password=zc5003730";
        return url;
    }

    public String getPhoneNumUrl() {
        url = PeiFengContant.YM_HTTP_BASE + "action=getmobile&token=" + PeiFengUtils.getToken() + "&itemid=3988&excludeno=";
        MyLog.write2File("url=" + url);
        return url;
    }

    public String getMsgCodeUrl(String num) {
        url = PeiFengContant.YM_HTTP_BASE + "action=getsms&token=" +PeiFengUtils.getToken() + "&itemid=3988&mobile=" + num + "&release=1";
        MyLog.write2File("url=" + url);
        return url;
    }

    public String getReleaseNumUrl(String num) {
        url = PeiFengContant.YM_HTTP_BASE + "action=release&token=" +PeiFengUtils.getToken() + "&itemid=3988&mobile=" + num;
        MyLog.write2File("url=" + url);
        return url;
    }

    public String getIgnoreUrl(String num) {
        url = PeiFengContant.YM_HTTP_BASE + "action=addignore&token=" + PeiFengUtils.getToken() + "&itemid=3988&mobile=" + num;
        MyLog.write2File("url=" + url);
        return url;
    }
}
