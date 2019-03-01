package org.telegram.peifeng.bean;



public class AppIdBean {
    public AppIdBean(int appId, String appHash) {
        this.appId = appId;
        this.appHash = appHash;
    }

    public AppIdBean(int appId) {
       this.appId=appId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppHash() {
        return appHash;
    }

    public void setAppHash(String appHash) {
        this.appHash = appHash;
    }

    int appId;
    String appHash;




}
