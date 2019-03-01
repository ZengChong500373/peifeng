package org.telegram.peifeng.message;

import android.util.LongSparseArray;

import org.telegram.peifeng.bean.PhonesBean;
import org.telegram.peifeng.bean.UserInfoBean;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.List;


public class MessageUtils {
    public static LongSparseArray<UserInfoBean> list2Sparse(PhonesBean phonesBean) {
        List<String> list = phonesBean.getList();
        String msg = phonesBean.getMsg();
        LongSparseArray<UserInfoBean> longSparseArray = new LongSparseArray<>();
        UserInfoBean userInfoBean;
        for (int i = 0; i < list.size(); i++) {
            userInfoBean = new UserInfoBean();
            long key = Long.parseLong(list.get(i));
            userInfoBean.setPhoneNum(key + "");
            userInfoBean.setMsg(msg);
            longSparseArray.put(key, userInfoBean);
        }
        return longSparseArray;
    }

    public static LongSparseArray<UserInfoBean> calculateData(LongSparseArray<UserInfoBean> longSparseArray, ArrayList<TLRPC.User> users) {
        for (int i = 0; i < users.size(); i++) {
            long key = Long.parseLong(users.get(i).phone);
            int id = users.get(i).id;
            UserInfoBean bean = longSparseArray.get(key);
            if (bean != null) {
                bean.setExist(true);
                bean.setId(id);
            }
        }
        return longSparseArray;
    }
}
