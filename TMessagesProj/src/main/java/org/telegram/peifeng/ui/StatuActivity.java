package org.telegram.peifeng.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.yima.YmHttpSdk;
import org.telegram.peifeng.listener.PeiFengHttpCallBack;
import org.telegram.tgnet.ConnectionsManager;


public class StatuActivity extends Activity implements NotificationCenter.NotificationCenterDelegate{
    TextView tv_status;
    String TAG="StatuActivity";
    private int currentConnectionState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        tv_status=findViewById(R.id.tv_status);
        init();
    }

    private void init() {
        NotificationCenter.getInstance(UserConfig.selectedAccount).addObserver(this, NotificationCenter.didUpdatedConnectionState);
    }

    public void test(View view){

        YmHttpSdk.getCode("", new PeiFengHttpCallBack<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(String string) {

            }
        });
    }
    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.didUpdatedConnectionState){
            updateCurrentConnectionState();
            Log.e(TAG,"didUpdatedConnectionState");
            return;
        }
    }
   public void updateCurrentConnectionState(){
       currentConnectionState = ConnectionsManager.getInstance(UserConfig.selectedAccount).getConnectionState();
       if (currentConnectionState == ConnectionsManager.ConnectionStateConnecting){
           tv_status.setText("链接中...");
           Log.e(TAG,"链接中");
           return;
       }
       if (currentConnectionState == ConnectionsManager.ConnectionStateWaitingForNetwork){
           tv_status.setText("ConnectionStateConnectingToProxy...");
           Log.e(TAG,"ConnectionStateWaitingForNetwork");
           return;
       }
       if (currentConnectionState == ConnectionsManager.ConnectionStateConnected){
           tv_status.setText("链接上...");
           Log.e(TAG,"链接上");
           return;
       }
       if (currentConnectionState == ConnectionsManager.ConnectionStateConnectingToProxy){
           tv_status.setText("ConnectionStateConnectingToProxy...");
           Log.e(TAG,"ConnectionStateConnectingToProxy");
           return;
       }
       if (currentConnectionState == ConnectionsManager.ConnectionStateUpdating){
           tv_status.setText("ConnectionStateUpdating...");
           Log.e(TAG,"ConnectionStateUpdating");
           return;
       }

   };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.getInstance(UserConfig.selectedAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
    }
}
