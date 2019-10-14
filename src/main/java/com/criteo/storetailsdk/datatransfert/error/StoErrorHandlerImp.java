package com.criteo.storetailsdk.datatransfert.error;

import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.datatransfert.request.StoRequestHandler;
import com.criteo.storetailsdk.datatransfert.values.StoTrackActionValue;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.updating.QSPUpdate;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;

/**
 * Created by mikhailpogorelov on 07/09/2017.
 * <p>
 * <p>
 * Class handles error reporting:
 * Protocol of message:
 * - platform version verifUID retailName errorMessage
 */

public class StoErrorHandlerImp implements StoErrorHandler {

    private final String TAG = "StoErrorHandlerImp";

    /**
     * StoRequestHandlerStore used for sending requests to server
     */
    private StoRequestHandler stoRequestHandler;

    /**
     * Singleton pattern
     */
    private static StoErrorHandlerImp instance = null;

    public StoErrorHandlerImp(StoRequestHandler stoRequestHandler) {
        this.stoRequestHandler = stoRequestHandler;
    }

    public static StoErrorHandlerImp getInstance(StoRequestHandler stoRequestHandler) {
        if (instance == null) {
            synchronized (StoErrorHandlerImp.class) {
                if (instance == null) {
                    instance = new StoErrorHandlerImp(stoRequestHandler);
                }
            }
        }
        return instance;
    }


    /**
     * Reports errors
     *
     * @param qspUpdate
     * @param stoQueryString
     * @param stoTrackEventValue
     * @param trackLabel
     */
    public synchronized void reportError(QSPUpdate qspUpdate, StoQueryString stoQueryString, StoTrackEventValue stoTrackEventValue, String trackLabel) {
        StoLog.i(TAG, "reportError");

        if (qspUpdate == null) {
            StoLog.e(TAG, "reportError: qspUpdate is null!");
            return;
        }

        if (stoQueryString == null) {
            StoLog.e(TAG, "reportError: stoQueryString is null!");
            return;
        }

        if (stoTrackEventValue == null) {
            StoLog.e(TAG, "reportError: stoTrackEventValue is null!");
            return;
        }

        trackLabel = StoTrackerUtils.getFormattedError(trackLabel);


        switch (stoTrackEventValue) {
            case onBuild:
                sendOnBuildError(qspUpdate, stoQueryString, trackLabel);
                break;
            case onDeliver:
                sendOnDeliverError(qspUpdate, stoQueryString, trackLabel);
                break;
            case onLoad:
                sendOnLoadError(qspUpdate, stoQueryString, trackLabel);
                break;
            case onReceive:
                sendOnReceiveError(qspUpdate, stoQueryString, trackLabel);
            default:
                StoLog.w(TAG, "reportError: unknown stoTrackEventValue");
                break;
        }
    }


    /**
     * Sets the platform version in the trackLabel
     *
     * @param trackLabel
     * @return
     */
    private String setPlatformVersion(String trackLabel) {
        return this.getPlatformVersion() + " " + trackLabel;
    }

    /**
     * Sets the verif UID
     *
     * @param verifUID
     * @param trackLabel
     * @return
     */
    private String setVerifUID(String verifUID, String trackLabel) {
        return verifUID + "_" + trackLabel;
    }


    /**
     * Returns the version of the SDK
     *
     * @return
     */
    private String getPlatformVersion() {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);

    }


    /**
     * Sets the retailName in the TrackLabel
     *
     * @param stoQueryString
     * @param trackLabel
     */
    private String setRetailerTrackLabel(StoQueryString stoQueryString, String trackLabel) {
        StoLog.i(TAG, "setRetailerTrackLabel");

        if (stoQueryString == null) {
            StoLog.e(TAG, "setRetailerTrackLabel: stoQueryString is null!");
            return trackLabel;
        }

        if (trackLabel == null) {
            StoLog.e(TAG, "setRetailerTrackLabel");
            return null;
        }

        if (stoQueryString.getQspRetailName().getValue() == null) {
            StoLog.e(TAG, "stoRetailerTrackLabel: qspRetailName is null");
            return trackLabel;
        }

        return stoQueryString.getQspRetailName().getValue() + " " + trackLabel;
    }

    /**
     * Sets the platform name to trackLabel
     *
     * @param trackLabel
     * @return
     */
    private String setPlatformName(String trackLabel) {
        return "ANDROID " + trackLabel;
    }


    /**
     * Sends a request for onBuild error
     *
     * @param stoQueryString :
     * @param trackLabel     : description of the error
     */
    @Override
    public void sendOnBuildError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel) {
        StoLog.i(TAG, "sendOnBuildError");

        if (stoQueryString != null) {
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.err);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.onBuild);
            qspUpdate.updateQSPTrackLabel(stoQueryString, trackLabel);
        } else {
            StoLog.e(TAG, "sendOnBuildError: stoQueryString is null! The error won't be reported");
            return;
        }

        if (trackLabel == null) {
            StoLog.w(TAG, "sendOnBuildError: trackLabel is null");
        }

        stoQueryString.printQuery(false);
        this.stoRequestHandler.requestGet(stoQueryString);
    }

    /**
     * Sends a request for onDeliver error
     *
     * @param stoQueryString
     * @param trackLabel     : description of the error
     */
    @Override
    public void sendOnDeliverError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel) {
        StoLog.i(TAG, "sendOnDeliverError");

        if (stoQueryString != null) {
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.err);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.onDeliver);
            qspUpdate.updateQSPTrackLabel(stoQueryString, trackLabel);

        } else {
            StoLog.e(TAG, "sendOnDeliverError: stoQueryString is null! The error won't be reported");
            return;
        }

        if (trackLabel == null) {
            StoLog.w(TAG, "sendOnDeliverError: trackLabel is null");
        }

        this.stoRequestHandler.requestGet(stoQueryString);
    }

    /**
     * Sends a request for onLoad error
     *
     * @param stoQueryString
     * @param trackLabel
     */
    @Override
    public void sendOnLoadError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel) {
        StoLog.i(TAG, "sendOnLoadError");

        if (stoQueryString != null) {
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.err);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.onLoad);
            qspUpdate.updateQSPTrackLabel(stoQueryString, trackLabel);

        } else {
            StoLog.e(TAG, "sendOnDeliverError: stoQueryString is null! The error won't be reported");
            return;
        }

        if (trackLabel == null) {
            StoLog.w(TAG, "sendOnDeliverError: trackLabel is null");
        }
        StoLog.d(TAG, "sendOnDeliverError: query: " + stoQueryString.getQueryString());
        this.stoRequestHandler.requestGet(stoQueryString);
    }

    /**
     * Sends a request for onReceive error
     *
     * @param qspUpdate
     * @param stoQueryString
     * @param trackLabel
     */
    @Override
    public void sendOnReceiveError(QSPUpdate qspUpdate, StoQueryString stoQueryString, String trackLabel) {
        StoLog.i(TAG, "sendOnReceiveError");
        if (stoQueryString != null) {
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.err);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.onReceive);
            qspUpdate.updateQSPTrackLabel(stoQueryString, trackLabel);
        } else {
            StoLog.e(TAG, "sendOnReceiveError: stoQueryString is null! The error won't be reported");
            return;
        }

        if (trackLabel == null) {
            StoLog.w(TAG, "sendOnReceiveError: trackLabel is null");
        }

        StoLog.d(TAG, "sendOnReceiveError: query: " + stoQueryString.getQueryString());

        this.stoRequestHandler.requestGet(stoQueryString);
    }
}
