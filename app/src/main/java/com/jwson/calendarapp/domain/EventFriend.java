package com.jwson.calendarapp.domain;

import java.io.Serializable;

/**
 * Created by jason_000 on 28/02/2017.
 */

public class EventFriend implements Serializable {
    public String mobile;
    public String name;
    public String id;

    public EventFriend() {
    }

    public EventFriend(String name, String id, String mobile) {
        this.name = name;
        this.id = id;
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
