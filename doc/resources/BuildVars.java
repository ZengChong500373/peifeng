/*
 * This is the source code of Telegram for Android v. 4.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package org.telegram.messenger;
import android.content.Context;
import android.content.SharedPreferences;

public class BuildVars {


    public static boolean DEBUG_VERSION = false;
    public static boolean DEBUG_PRIVATE_VERSION = false;
    public static boolean LOGS_ENABLED = false;
    public static boolean CHECK_UPDATES = false;
    public static int BUILD_VERSION = 1358;
    public static String BUILD_VERSION_STRING = "4.9.1";
//    public static int APP_ID = 379007; //obtain your own APP_ID at https://core.telegram.org/api/obtaining_api_id
//    public static String APP_HASH = "4870ee254110ca91ddc12dfc17b3c643"; //obtain your own APP_HASH at https://core.telegram.org/api/obtaining_api_id
//    public static String HOCKEY_APP_HASH = "bf6708167ec74166ab0ff18123ecdb16";
//    public static String HOCKEY_APP_HASH_DEBUG = "bf6708167ec74166ab0ff18123ecdb16";
//    public static String PLAYSTORE_APP_URL = "";


    public static int APP_ID = 370656; //obtain your own APP_ID at https://core.telegram.org/api/obtaining_api_id  手机号码 17040012171
    public static String APP_HASH = "46b53761282142c2cefb8042c6315732"; //obtain your own APP_HASH at https://core.telegram.org/api/obtaining_api_id
    public static String HOCKEY_APP_HASH = "bf6708167ec74166ab0ff18123ecdb16";
    public static String HOCKEY_APP_HASH_DEBUG = "bf6708167ec74166ab0ff18123ecdb16";
    public static String PLAYSTORE_APP_URL = "";

    static {
        if (ApplicationLoader.applicationContext != null) {
            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE);
            LOGS_ENABLED = sharedPreferences.getBoolean("logsEnabled", DEBUG_VERSION);
        }
    }
}
