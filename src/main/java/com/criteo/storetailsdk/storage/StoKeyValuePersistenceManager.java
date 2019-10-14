package com.criteo.storetailsdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.criteo.storetailsdk.logs.StoLog;

/**
 * Created by mikhailpogorelov on 23/01/2018.
 */

public class StoKeyValuePersistenceManager {

    private final String TAG = "StoKeyValuePersistenceManager";

    private Context context;

    // Name of the storetail lib settings
    private final String STORETAIL_SDK_PREFERENCES = "storetail_sdk_prefs";

    // VerifUID key
    private final String sto_verif_uid_key = "verif_uid_key";

    // LastActionCallTime key
    private final String sto_last_action_call_key = "lastactioncall_key";

    private final String sto_last_session_pages_key = "lastsessionpages_key";

    private SharedPreferences sharedPreferences;


    public StoKeyValuePersistenceManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(STORETAIL_SDK_PREFERENCES, Context.MODE_PRIVATE);
    }


    /**
     * Retrieves the verifUID
     *
     * @return
     */
    public synchronized String getVerifUID() {
        StoLog.d(TAG, "getVerifUID");

        if (sharedPreferences.contains(sto_verif_uid_key)) {
            return sharedPreferences.getString(sto_verif_uid_key, "");
        }
        return null;
    }


    public synchronized void setVerifUID(String verifUID) {
        StoLog.d(TAG, "setVerifUID: " + verifUID);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sto_verif_uid_key, verifUID);
        editor.apply();
    }


    public synchronized long getLastActionCallTime() {
        StoLog.d(TAG, "getLastActionCallTime");

        if (sharedPreferences.contains(sto_last_action_call_key)) {
            return sharedPreferences.getLong(sto_last_action_call_key, -1);
        }
        return -1;
    }
    
    public synchronized void setLastActionCallTime(long lastActionCallTime) {
        StoLog.d(TAG, "setLastActionCallTime: " + lastActionCallTime);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(sto_last_action_call_key, lastActionCallTime);
        editor.apply();
    }

    public synchronized long getLastSessionPages() {
        StoLog.d(TAG, "getLastSessionPages");

        if (sharedPreferences.contains(sto_last_session_pages_key)) {
            return sharedPreferences.getLong(sto_last_session_pages_key, 0);
        }
        return 0;
    }

    public synchronized void setLastSessionPages(long lastSessionPages) {
        StoLog.d(TAG, "setLastSessionPages: " + lastSessionPages);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(sto_last_session_pages_key, lastSessionPages);
        editor.apply();
    }



}
