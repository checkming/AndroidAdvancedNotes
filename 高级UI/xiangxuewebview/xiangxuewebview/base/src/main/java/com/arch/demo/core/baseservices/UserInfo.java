package com.arch.demo.core.baseservices;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by duzhixiong on 2017/7/28.
 */

public class UserInfo implements Serializable {

    @NonNull
    private String uuid;
    public String birthday;
    public String sex;
    private String accessToken;
    private String refreshToken;
    //到期时间
    private String tokenExpireTime;
    private int type;
    private String name;
    private String email;
    private String phone;
    private String signature;
    private String avatarRemoteUrl;
    private ThirdAccount[] thirdAccounts;
    private String regionId;

    @NonNull
    public String getRegionId() {
        return regionId;
    }

    @NonNull
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getNickname() {
        return name;
    }

    public String getEmailAddress() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getTokenExpireTime() {
        return tokenExpireTime;
    }

    public String getHeadUrl() {
        return avatarRemoteUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setToken(String token) {
        this.accessToken = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setAvatarRemoteUrl(String avatarRemoteUrl) {
        this.avatarRemoteUrl = avatarRemoteUrl;
    }

    public void setTokenExpireTime(String tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }


    public ThirdAccount[] getThirdAccounts() {
        return thirdAccounts;
    }

    public void setThirdAccounts(ThirdAccount[] thirdAccounts) {
        this.thirdAccounts = thirdAccounts;
    }


    public class ThirdAccount implements Serializable {
        public String avatarUrl;
        public String nickName;
        public String openId;
        public int thirdType;
        public String token;
        public String unionId;
        public String email;

    }
}
