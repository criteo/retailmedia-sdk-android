package com.criteo.storetailsdk.datatransfert.error;

/**
 * Created by MikhailPOGORELOV on 05/04/2017.
 */

import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.tracker.updating.QSPUpdate;

/**
 * Class handles different types of errors
 */
interface StoErrorHandler {

    /**
     * Sends a request for onBuild error
     *
     * @param stoQueryString:
     * @param trackLabel:     description of the error
     */
    void sendOnBuildError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel);

    /**
     * Sends a request for onDeliver error
     *
     * @param stoQueryString
     * @param trackLabel:    description of the error
     */
    void sendOnDeliverError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel);


    /**
     * Sends a request for onLoad error
     *
     * @param stoQueryString
     * @param trackLabel
     */
    void sendOnLoadError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel);


    /**
     * Sends a request for onReceive error
     *
     * @param qspUpdate
     * @param stoQueryString
     * @param trackLabel
     */
    void sendOnReceiveError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel);

}
