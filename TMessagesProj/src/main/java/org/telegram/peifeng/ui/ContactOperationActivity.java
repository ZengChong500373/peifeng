package org.telegram.peifeng.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.PeiFengContant;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.peifeng.utils.PeiFengServerMessage;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;


public class ContactOperationActivity extends Activity {
//589492662 15520140104

    private int currentAccount = UserConfig.selectedAccount;
    private long userid = 761587691;//734243627  589492662    761587691
    private EditText edit_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_operate);
        edit_word = findViewById(R.id.edit_word);


    }
    public void test(View view){
        PeiFengServerMessage.sendMessage(PeiFengContant.CLOSE_SELF);
    }
    public void send(View view) {
//        CharSequence message = getText();
        String message="你好";
        if (processSendingText(message)) {
            edit_word.setText("");
        } else {
            Toast.makeText(this, "send fail", Toast.LENGTH_LONG).show();
        }
    }

    public CharSequence getText() {
        return edit_word.getText();
    }

    public boolean processSendingText(CharSequence text) {
        text = AndroidUtilities.getTrimmedString(text);
        int maxLength = MessagesController.getInstance(currentAccount).maxMessageLength;
        if (text.length() != 0) {
            int count = (int) Math.ceil(text.length() / (float) maxLength);
            for (int a = 0; a < count; a++) {
                CharSequence[] message = new CharSequence[]{text.subSequence(a * maxLength, Math.min((a + 1) * maxLength, text.length()))};
                ArrayList<TLRPC.MessageEntity> entities = DataQuery.getInstance(currentAccount).getEntities(message);

                SendMessagesHelper.getInstance(currentAccount).sendMessage(message[0].toString(), userid, null, null, true, entities, null, null);
            }
            return true;
        }
        return false;
    }

    public void check(View view){
        MessageObject message = MessagesController.getInstance(currentAccount).dialogMessage.get(userid);
        Boolean isSent=message.isSent();
        MyLog.write2File("message="+message);
    }
    public void log(View view) {
        Log.e("jyh", "dialogsNeedReload=" + NotificationCenter.dialogsNeedReload);
        Log.e("jyh", "emojiDidLoaded=" + NotificationCenter.emojiDidLoaded);
        Log.e("jyh", "closeSearchByActiveAction=" + NotificationCenter.closeSearchByActiveAction);
        Log.e("jyh", "proxySettingsChanged=" + NotificationCenter.proxySettingsChanged);
        Log.e("jyh", "updateInterfaces=" + NotificationCenter.updateInterfaces);
        Log.e("jyh", "appDidLogout=" + NotificationCenter.appDidLogout);
        Log.e("jyh", "encryptedChatUpdated=" + NotificationCenter.encryptedChatUpdated);
        Log.e("jyh", "openedChatChanged=" + NotificationCenter.openedChatChanged);
        Log.e("jyh", "notificationsSettingsUpdated=" + NotificationCenter.notificationsSettingsUpdated);
        Log.e("jyh", "messageReceivedByAck=" + NotificationCenter.messageReceivedByAck);
        Log.e("jyh", "messageReceivedByServer=" + NotificationCenter.messageReceivedByServer);
        Log.e("jyh", "messageSendError=" + NotificationCenter.messageSendError);
        Log.e("jyh", "didSetPasscode=" + NotificationCenter.didSetPasscode);
        Log.e("jyh", "needReloadRecentDialogsSearch=" + NotificationCenter.needReloadRecentDialogsSearch);
        Log.e("jyh", "didLoadedReplyMessages=" + NotificationCenter.didLoadedReplyMessages);
        Log.e("jyh", "reloadHints=" + NotificationCenter.reloadHints);
        Log.e("jyh", "didUpdatedConnectionState=" + NotificationCenter.didUpdatedConnectionState);
        Log.e("jyh", "dialogsUnreadCounterChanged=" + NotificationCenter.dialogsUnreadCounterChanged);
    }

}





