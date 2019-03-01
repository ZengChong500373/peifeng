package org.telegram.peifeng.bean;



import org.telegram.peifeng.utils.MyLog;

public class CurrentResultBean {
    public CurrentResultBean(int status, String msg, int currentFlag) {
        this.status = status;
        this.msg = msg;
        this.currentFlag = currentFlag;
        MyLog.write2File(msg);
    }
    public CurrentResultBean(int status, String msg ) {
        this.status = status;
        this.msg = msg;
        this.currentFlag = currentFlag;
        MyLog.write2File(msg);
    }
    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(int currentFlag) {
        this.currentFlag = currentFlag;
    }

    int status;
    String msg;
    int currentFlag;
}
