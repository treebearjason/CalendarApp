package com.jwson.calendarapp.utils;

import com.jwson.calendarapp.BuildConfig;

/**
 * Created by user on 8/23/2016.
 */
public final class Constants {

    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_USERS_FRIENDS = "userFriends";
    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_USER_FRIENDS = FIREBASE_URL+"/"+ FIREBASE_LOCATION_USERS_FRIENDS;

    public static final String NGROK_URL = "http://87413d22.ngrok.io/sms";
}

