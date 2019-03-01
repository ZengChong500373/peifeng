package org.telegram.peifeng.listener;

public interface PeiFengHttpCallBack<T> {
    void onSuccess(T result);
    void onFail(String string);
}
