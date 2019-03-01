package org.telegram.peifeng.utils;




import android.text.TextUtils;

import android.util.LongSparseArray;


import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;

import org.telegram.peifeng.bean.PhonesBean;
import org.telegram.peifeng.listener.PeiFengHttpCallBack;

import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;



public class ContactUtils {
    public static LongSparseArray<Long> registedNum = new LongSparseArray<>();

    private static final ContactUtils ourInstance = new ContactUtils();


    public static ContactUtils getInstance() {
        return ourInstance;
    }








    public ArrayList<TLRPC.TL_inputPhoneContact> getListUser(Long startNum, Long endNum) {
        MyLog.write2File("ContactUtils getListUser start="+startNum+" end="+endNum);
        if (TextUtils.isEmpty(startNum+"")||TextUtils.isEmpty(endNum+"")) {
            return null;
        }
        ArrayList<TLRPC.TL_inputPhoneContact> demo =new ArrayList<>();
        TLRPC.TL_inputPhoneContact inputPhoneContact;
        int count = 1;
        for (Long i = startNum; i < endNum; i++) {
            inputPhoneContact = new TLRPC.TL_inputPhoneContact();
            inputPhoneContact.first_name = i + "";
            inputPhoneContact.last_name = "";
            inputPhoneContact.phone = "86"+i + "";
            inputPhoneContact.client_id = count;
            demo.add(inputPhoneContact);
            count++;
        }
        return demo;
    }

    public ArrayList<TLRPC.TL_inputPhoneContact> getListUser(PhonesBean bean) {
           if (bean==null||TextUtils.isEmpty(bean.getMsg())){
               return null;
           }
        ArrayList<TLRPC.TL_inputPhoneContact> demo =new ArrayList<>();
        TLRPC.TL_inputPhoneContact inputPhoneContact;
        for (int i=0;i<bean.getList().size();i++){
            inputPhoneContact = new TLRPC.TL_inputPhoneContact();
            inputPhoneContact.first_name = bean.getList().get(i) + "";
            inputPhoneContact.last_name = "";
            inputPhoneContact.phone = bean.getList().get(i) + "";
            inputPhoneContact.client_id = i+1;
            demo.add(inputPhoneContact);
        }
        return demo;
    }





    public void addNum2SparseArray(String num) {
        if (TextUtils.isEmpty(num)) {
            return;
        }
        long phoneNum = Long.parseLong(num.trim());
        registedNum.put(phoneNum, phoneNum);
    }


    public void cleanSparseArray() {
        registedNum.clear();
    }

    public void SparseArray2String(PeiFengHttpCallBack<String> callBack) {
        if (registedNum == null || registedNum.size() == 0) {
            callBack.onSuccess("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < registedNum.size(); i++) {
            Long key = registedNum.keyAt(i);
            Long phoneNum = registedNum.get(key);
            if (i == registedNum.size() - 1) {
                buffer.append(phoneNum);
                callBack.onSuccess(buffer.toString());
            } else {
                buffer.append(phoneNum + ",");
            }
        }
    }

    public void getTelegramregisteredPhones(ArrayList<TLRPC.User> users) {
        if (users == null) {
            return;
        }
        for (TLRPC.User user : users) {
            addNum2SparseArray(user.phone);
        }
    }



    public TLRPC.User getCurrentUser() {
        TLRPC.User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId());
        return user;
    }

    public String getNo86Phone(String num) {
        String str = "";
        if (num.contains("+86")) {
            str = num.split("\\+86")[1];
        }
        return str.replaceAll(" ", "");
    }


}
