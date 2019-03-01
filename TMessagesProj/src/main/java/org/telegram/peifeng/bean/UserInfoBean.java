package org.telegram.peifeng.bean;

public class UserInfoBean {
    String phoneNum;
    String msg;

    public UserInfoBean() {
        this.exist = false;
    }

    long id;
    /**
     *存在就可以发消息
     * 不存在就不发消息
     * */
    Boolean exist;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }
}
