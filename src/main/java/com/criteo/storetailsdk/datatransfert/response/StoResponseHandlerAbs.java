package com.criteo.storetailsdk.datatransfert.response;

/**
 * Created by MikhailPOGORELOV on 05/04/2017.
 */

import org.json.JSONObject;

/**
 * Class handling the responses from the server
 * Singleton Design pattern
 */
public abstract class StoResponseHandlerAbs {

    /**
     * Handles a String Response
     *
     * @param response
     */
    abstract void handleResponse(final String response, final String queryString, final String trackActionValue, String identifier);

    /**
     * Handles a String Response
     *
     * @param jsonObject
     */
    abstract void handleResponse(final JSONObject jsonObject, final String queryString);


    /**
     * Handles the error message
     *
     * @param message
     */
    abstract void handleFailure(final String message, String identifier);

}

