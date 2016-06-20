package com.jwson.calendarapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jason_000 on 11/06/2016.
 */
public class UserEvents implements Serializable {

    //Auto-generated UUID
    private String id;

    private String name;
    private String description;

    //Dates
    private Date startDate;
    private Date endDate;
    private Date createDate;

    //Locations
    private String locationName;
    private Double locationLongitude;
    private Double locationLatitude;

    //Friends IDs
    private Set<String> admins;
    private Set<String> confirmedFriends;
    private Set<String> invitedFriends;

    //Misc
    private int iconId;
    private String picUrl;

    public UserEvents() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<String> admins) {
        this.admins = admins;
    }

    public Set<String> getConfirmedFriends() {
        return confirmedFriends;
    }

    public void setConfirmedFriends(Set<String> confirmedFriends) {
        this.confirmedFriends = confirmedFriends;
    }

    public Set<String> getInvitedFriends() {
        return invitedFriends;
    }

    public void setInvitedFriends(Set<String> invitedFriends) {
        this.invitedFriends = invitedFriends;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEvents that = (UserEvents) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
