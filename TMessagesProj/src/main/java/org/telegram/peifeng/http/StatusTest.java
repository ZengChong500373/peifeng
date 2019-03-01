package org.telegram.peifeng.http;


import android.text.TextUtils;


import org.telegram.messenger.UserConfig;
import org.telegram.peifeng.utils.MyLog;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class StatusTest {
    public static void testIsStatusOk(String num,StatuListener listener){
        MyLog.write2File("testIsStatusOk num="+num);
        if (TextUtils.isEmpty(num)){
            return;
        }
        final TLRPC.TL_contacts_importContacts req = new TLRPC.TL_contacts_importContacts();
        final TLRPC.TL_inputPhoneContact inputPhoneContact = new TLRPC.TL_inputPhoneContact();
        inputPhoneContact.first_name = num;
        inputPhoneContact.last_name = "";
        inputPhoneContact.phone = "+" + num;
        req.contacts.add(inputPhoneContact);
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new RequestDelegate() {
            @Override
            public void run(TLObject response, TLRPC.TL_error error) {
                final TLRPC.TL_contacts_importedContacts res = (TLRPC.TL_contacts_importedContacts) response;
                if (res.users==null||res.users.size()==0){
                    listener.statuIsOK(false,num);

                    MyLog.write2File("testIsStatusOk 该换换号了 num="+num+"不能用");
                }else {
                    listener.statuIsOK(true,num);
                    MyLog.write2File("testIsStatusOk 状态正常");
                }
            }
        },ConnectionsManager.RequestFlagFailOnServerErrors);
    }
    public interface StatuListener {
        void statuIsOK(Boolean isOk,String registeredPhone);

    }
}
