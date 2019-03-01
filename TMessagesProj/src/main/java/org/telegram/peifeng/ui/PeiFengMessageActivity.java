package org.telegram.peifeng.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.telegram.messenger.R;


import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.http.SdMessageHttpMethods;
import org.telegram.peifeng.message.PeiFengMessageManager;
import org.telegram.peifeng.utils.TelegramUtils;


public class PeiFengMessageActivity extends Activity {
//761587691 665655715

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_peifeng);
    }

    public void getcontact(View view) {
       PeiFengMessageManager.getInstance().start();
    }

    public void sendOk(View view) {
        TelegramUtils.sendMsgBroad("你好", 646922935);
    }

    public void sendFail(View view) {
//       int type=TelegramUtils.sendType(761587691);
//        Log.e("hello","type="+type);

    }
}
