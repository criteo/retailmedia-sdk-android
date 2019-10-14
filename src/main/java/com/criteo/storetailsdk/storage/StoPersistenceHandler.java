package com.criteo.storetailsdk.storage;

import android.content.Context;
import com.criteo.storetailsdk.logs.StoLog;

/**
 * Created by mikhailpogorelov on 23/01/2018.
 */

public class StoPersistenceHandler {

    private final String TAG = "StoPersistenceHandler";

    private StoKeyValuePersistenceManager stoKeyValuePersistenceManager;


    public StoPersistenceHandler(Context context) {
        this.stoKeyValuePersistenceManager = new StoKeyValuePersistenceManager(context);
    }


    public String getVerifUID() {
        StoLog.d(TAG, "getVerifUID");
        return this.stoKeyValuePersistenceManager.getVerifUID();
    }

    public void setVerifUID(String verifUID) {
        StoLog.d(TAG, "setVerifUID: " + verifUID);
        this.stoKeyValuePersistenceManager.setVerifUID(verifUID);
    }

    public long getLastActionCallTime() {
        StoLog.d(TAG, "getLastActionCallTime");
        return this.stoKeyValuePersistenceManager.getLastActionCallTime();
    }

    public void setLastActionCallTime(long lastActionCallTime) {
        StoLog.d(TAG, "setLastActionCallTime: " + lastActionCallTime);
        this.stoKeyValuePersistenceManager.setLastActionCallTime(lastActionCallTime);
    }

    public long getLastSessionPages() {
        StoLog.d(TAG, "getLastSessionPages");
        return this.stoKeyValuePersistenceManager.getLastSessionPages();
    }

    public void setLastSessionPages(long lastSessionPages) {
        StoLog.d(TAG, "setLastSessionPages: " + lastSessionPages);
        this.stoKeyValuePersistenceManager.setLastSessionPages(lastSessionPages);
    }

}
