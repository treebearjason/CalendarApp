package com.jwson.calendarapp.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 8/29/2016.
 */
public class User implements Serializable{
    private String uId;
    private String name;
    private String email;
    private String mobile;
    private Long createDate;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String email, Long createDate, boolean hasLoggedInWithPassword) {
        this.email = email;
        this.createDate = createDate;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }

    public void setHasLoggedInWithPassword(boolean hasLoggedInWithPassword) {
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }
}
