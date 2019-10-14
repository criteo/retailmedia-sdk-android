package com.criteo.storetailsdk.tracker.updating;

import androidx.annotation.Nullable;
import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.datatransfert.values.StoTrackActionValue;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.logs.StoLog;

import java.util.List;
import java.util.Map;

/**
 * Created by MikhailPOGORELOV on 14/04/2017.
 * <p>
 * Saves data in database on each update
 */

public class QSPUpdate extends QSPUpdateAbs {

    private final String TAG = "QSPUpdate";

    public QSPUpdate() { }

    @Override
    public void updateQSPForcedCity(StoQueryString stoQueryString, String value) {
        super.updateQSPForcedCity(stoQueryString, value);
    }

    @Override
    public void updateQSPForcedLatitude(StoQueryString stoQueryString, String value) {
        super.updateQSPForcedLatitude(stoQueryString, value);
    }

    @Override
    public void updateQSPForcedLongitude(StoQueryString stoQueryString, String value) {
        super.updateQSPForcedLongitude(stoQueryString, value);
    }

    @Override
    public void updateQSPVerifUID(StoQueryString stoQueryString, final String verifUID) {
        super.updateQSPVerifUID(stoQueryString, verifUID);
    }
    /**
     * Updates optin-optout's parameter
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public synchronized void updateQSPOptinOut(StoQueryString stoQueryString, int value) {
        /**
         * 1 optin 0 optout
         */
        super.updateQSPOptinOut(stoQueryString, value);
    }

    @Override
    public void updateQSPTrackAction(StoQueryString stoQueryString, StoTrackActionValue stoTrackActionValue) {
        StoLog.i(TAG, "updateQSPTrackAction");
        super.updateQSPTrackAction(stoQueryString, stoTrackActionValue);
    }

    @Override
    public void updateQSPRetailName(StoQueryString stoQueryString, String value) {
        super.updateQSPRetailName(stoQueryString, value);
    }

    @Override
    public void updateQSPRetailPage(StoQueryString stoQueryString, String value) {
        super.updateQSPRetailPage(stoQueryString, value);
    }

    @Override
    public void updateQSPRetailSearch(StoQueryString stoQueryString, String value) {
        super.updateQSPRetailSearch(stoQueryString, value);
    }

    @Override
    public void updateQSPRetailShop(StoQueryString stoQueryString, String value) {
        super.updateQSPRetailShop(stoQueryString, value);
    }

    /**
     * Updates the TrackBrand parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackBrand(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackBrand(stoQueryString, value);
    }

    /**
     * Updates the SessionPages parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public synchronized void updateQSPSessionPages(StoQueryString stoQueryString, String value) {
        super.updateQSPSessionPages(stoQueryString, value);
    }

    /**
     * Updates the TrackOperation parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackOperation(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackOperation(stoQueryString, value);
    }

    /**
     * Updates the TrackCreative parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackCreative(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackCreative(stoQueryString, value);
    }

    /**
     * Updates the TrackInsertion parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackInsertion(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackInsertion(stoQueryString, value);
    }

    /**
     * Updates the TrackTime parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public synchronized void updateQSPTrackTime(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackTime(stoQueryString, value);
    }

    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param stoTrackEventValue
     */
    @Override
    public void updateQSPTrackEvent(StoQueryString stoQueryString, StoTrackEventValue stoTrackEventValue) {
        super.updateQSPTrackEvent(stoQueryString, stoTrackEventValue);
    }

    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackLabel(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackLabel(stoQueryString, value);
    }

    /**
     * Updates the ProductID parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public synchronized void updateQSPProductID(StoQueryString stoQueryString, String value) {
        super.updateQSPProductID(stoQueryString, value);
    }

    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public void updateQSPTrackValue(StoQueryString stoQueryString, String value) {
        super.updateQSPTrackValue(stoQueryString, value);
    }

    /**
     * Updates the ProductLabel parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    @Override
    public synchronized void updateQSPProductLabel(StoQueryString stoQueryString, String value) {
        super.updateQSPProductLabel(stoQueryString, value);
    }

    /**
     * Updates stoQueryString params with the map
     *
     * @param mapParams
     * @param stoQueryString
     */
    public void updateByMap(Map<String, Object> mapParams, StoQueryString stoQueryString) {
        StoLog.i(TAG, "updateByMap: adding following parameters");

        if (mapParams == null) {
            StoLog.e(TAG, "updateByMap: mapParams is null!");
            return;
        }

        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            String key = entry.getKey();
            StoLog.i(TAG, key + " " + entry.getValue());
            if (stoQueryString.getQSPByName(key).getName().equals(key)) {
                stoQueryString.getQSPByName(key).setValue(entry.getValue().toString());
            }
        }
    }

    /**
     * Updates stoQueryString params with the list
     *
     * @param queryStringParams
     * @param stoQueryString
     */
    public synchronized void updateByList(final @Nullable List<StoQueryStringParam> queryStringParams, final StoQueryString stoQueryString) {
        StoLog.i(TAG, "updateByList: adding following parameters starts...");
        if (queryStringParams != null) {
            for (StoQueryStringParam qsp : queryStringParams) {
                if (stoQueryString.getQSPByName(qsp.getName()) != null) {
                    StoLog.i(TAG, "updateByList: name: " + qsp.getName() + " value: " + qsp.getValue());
                    stoQueryString.getQSPByName(qsp.getName()).setValue(qsp.getValue());
                }
            }
        } else {
            StoLog.w(TAG, "updateByList: queryStringParamList is null!");
        }
        StoLog.i(TAG, "updateByList: adding following parameters finished!");
    }
}