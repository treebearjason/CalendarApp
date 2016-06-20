package com.jwson.calendarapp.couchbase;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by jason_000 on 19/06/2016.
 */
public class CouchbaseHelper {

    private static Manager manager;

    private static CouchbaseHelper instance = null;

    public static CouchbaseHelper getInstance(){
        if(instance == null){
            instance = new CouchbaseHelper();
        }
        return instance;
    }

    public Manager getManager(final Context context) throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    public Database getDatabase(final Manager manager, String dbName) throws CouchbaseLiteException {
        return manager.getDatabase(dbName);
    }
}
