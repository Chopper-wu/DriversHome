package com.blue.corelib.bean;


public class LoginUser {
    /*
    {"msg":"success","code":0,"data":{"id":17,"nickName":"aaa","birthday":"2016-09-30T16:00:00.000+00:00",
    "sex":"小哥哥","profilePhoto":"","openId":null,"wxNickName":null,"mobile":"15996323499",
    "insertTime":null,"status":true,"disable":false,"lastOnlineTime":"2021-10-01T18:22:42.000+00:00",
    "level":null,"exp":null,"totalExp":null}}
     */
    private String id;
    private String nickName;
    private String birthday;
    private String sex;
    private String profilePhoto;
    private String mobile;
    private String status;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
