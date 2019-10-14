package com.criteo.storetailsdk.view.managing;

import com.criteo.storetailsdk.logs.StoLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikhailPOGORELOV on 23/05/2017.
 * <p>
 * Manages the updates of views
 */

public class StoViewManager {

    private final String TAG = "StoViewManager";

    /**
     * List of shown format row objects
     */
    private final List<String> viewedFormatIds;

    private final List<String> impressedFormatIds;


    public StoViewManager() {
        this.viewedFormatIds = new ArrayList<>();

        this.impressedFormatIds = new ArrayList<>();
    }

    /**
     * Adds the objectID to the list of impressed objects
     *
     * @param objectId
     */
    public void addImpressedObject(final String objectId) {
        impressedFormatIds.add(objectId);
    }

    /**
     * Returns true if the object has been impressed already
     *
     * @param objectId
     * @return
     */
    public boolean isImpressed(final String objectId) {
        StoLog.i(TAG, "isImpressed");

        return impressedFormatIds.contains(objectId);
    }

    /**
     * Adds the objectID to the list of viewed objects
     *
     * @param objectID
     */
    public void addViewedObjectId(final String objectID) {
        this.viewedFormatIds.add(objectID);
    }

    /**
     * Returns true if the object has been viewed already
     *
     * @param objectID
     * @return
     */
    public boolean isViewed(final String objectID) {
        StoLog.i(TAG, "isViewed");

        return this.viewedFormatIds.contains(objectID);
    }

    public void removeFormats() {

        this.impressedFormatIds.clear();
        this.viewedFormatIds.clear();
    }
}
