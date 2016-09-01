package com.jwson.calendarapp.domain;

import java.util.Date;

/**
 * Created by user on 8/29/2016.
 */
public class User {
    private String name;
    private String email;
    private String mobile;
    private Date createDate;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String email, Date createDate, boolean hasLoggedInWithPassword) {
        this.email = email;
        this.createDate = createDate;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }

    public void setHasLoggedInWithPassword(boolean hasLoggedInWithPassword) {
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }
}
