package org.telegram.peifeng.listener;

public interface TelegramDataCallBack<T>  {
    void onDataCome(T result);
}
