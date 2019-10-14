package com.criteo.storetailsdk.datatransfert.response;

import android.content.Context;
import com.criteo.storetailsdk.logs.StoLog;
import org.json.JSONObject;

/**
 * Created by MikhailPOGORELOV on 13/04/2017.
 */

/**
 * Implementation of StoResponseHandlerAbs
 * Handles Responses
 * Singleton Design Pattern
 */
public class StoResponseHandlerImp extends StoResponseHandlerAbs {

    public static final String TAG = "StoResponseHandlerImp";

    protected Context context;

    /**
     * Handles a String Response
     *
     * @param response
     */
    @Override
    public void handleResponse(final String response, final String queryString, final String trackActionValue, String identifier) {
        StoLog.d(TAG, "handleResponse for identifier : " + identifier);
    }

    /**
     * Handles a JSONObject Response
     *
     * @param jsonObject
     */
    @Override
    public void handleResponse(final JSONObject jsonObject, final String queryString) {
        StoLog.d(TAG, "handleResponse");
    }

    /**
     * Handles the error message
     *
     * @param message
     */
    @Override
    public void handleFailure(String message, String identifier) {
        StoLog.d(TAG, "handleFailure: " + message);
    }
}
