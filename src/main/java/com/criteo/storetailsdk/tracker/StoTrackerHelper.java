package com.criteo.storetailsdk.tracker;

import android.content.Context;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.storage.StoPersistenceHandler;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;
import com.criteo.storetailsdk.view.managing.StoViewManager;
import com.criteo.storetailsdk.view.managing.StoPageType;

import java.util.ArrayList;

/**
 * StoTrackerHelper handles the following:
 * <p>
 * <p>
 * 1) Adds seen sto basket products
 * 2) Adds StoreTail objects
 * 3) Counts visited pages
 */
public class StoTrackerHelper {
    private final String TAG = "StoTrackerHelper";


    /**
     * List of formats for which the quantity of available products has been sent
     */
    private ArrayList<String> stoViewNbAvailProductsSent;

    /**
     * StoViewManager manages updates of views
     */
    private StoViewManager stoViewManager;

    /**
     * StoPersistenceHandler
     */
    private StoPersistenceHandler stoPersistenceHandler;

    // Session timeout
    private final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes

    // Time of the last StoTracker action call
    private long lastActionCallTime = 0;

    // Current session time
    private long currentSessionTime = 0;


    // Timestamp when the tracker has been initialized on the page
    // Called in "lod" track action
    private long startTrackTime = 0;


    // The delay of the verifUID life
    private long VERIF_UID_LIFE = 60 * 60 * 24 * 45; // 1 hour * 45 days

    /**
     * Number of session pages
     */
    private long nbSessionPages;

    /**
     * Previous page
     * Note: Used for updating the number of session pages,
     * If the previousPage equals the current page, the number of session pages won't be updated
     */
    private String previousPage = "";


    /**
     * Page used by lod track action
     */
    private StoPageType stoPageType;


    /**
     * Flag that indicates that the last "lod" was with a query
     * This way we can know if (for the next lod track action) we have to remove objects from the list of the adapter
     */


    private static StoTrackerHelper instance = null;

    private StoTrackerHelper(Context context) {
        stoViewNbAvailProductsSent = new ArrayList<>();
        nbSessionPages = 0;
        this.stoViewManager = new StoViewManager();
        this.stoPersistenceHandler = new StoPersistenceHandler(context);
    }

    public static StoTrackerHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (StoTrackerHelper.class) {
                instance = new StoTrackerHelper(context);
            }
        }
        return instance;
    }

    /**
     * @return The type of page currently used by the "lod" track action
     */
    public StoPageType getStoPageType() {
        return this.stoPageType;
    }

    public void setStoPageType(StoPageType stoPageType) {
        this.stoPageType = stoPageType;
    }

    /**
     * Verifies if the number of available products has been sent for the format
     *
     * @param object_id
     * @return: true if the number of available product for the formats has been sent
     */
    public boolean isNbAvailProdSent(final String object_id) {
        for (String id : this.stoViewNbAvailProductsSent) {
            if (id.equals(object_id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the object has been viewed already
     *
     * @param objectId
     * @return
     */
    public boolean isViewed(final String objectId) {
        return this.stoViewManager.isViewed(objectId);
    }

    /**
     * Returns true if the object has been impressed already
     *
     * @param objectId
     * @return
     */
    public boolean isImpressed(final String objectId) {
        return this.stoViewManager.isImpressed(objectId);
    }

    /**
     * Adds the objectId to the list of viewed objects
     *
     * @param objectId
     */
    public void addViewedObject(final String objectId) {
        if (objectId == null) {
            StoLog.e(TAG, "addViewedObject: objectId is null!");
            return;
        }

        this.stoViewManager.addViewedObjectId(objectId);
    }

    /**
     * Adds the object_id to the list if the number of available products for the format has
     * been sent
     *
     * @param object_id
     */
    public void addNbAvailProductsSent(final String object_id) {
        this.stoViewNbAvailProductsSent.add(object_id);
    }

    /**
     * Adds the objectId to the list of impressed objects
     *
     * @param objectId
     */
    public void addImpressedObject(final String objectId) {
        this.stoViewManager.addImpressedObject(objectId);
    }

    /**
     * Increments the number of visited pages
     */
    private void incSessionPages() {
        this.nbSessionPages = this.stoPersistenceHandler.getLastSessionPages() + 1;
        this.stoPersistenceHandler.setLastSessionPages(nbSessionPages);
    }

    /**
     * Resets the number of session pages
     */
    private void resetSessionPages() {
        this.nbSessionPages = 0;
        this.stoPersistenceHandler.setLastSessionPages(nbSessionPages);
    }

    /**
     * @return: the number of visited pages during the session
     */

    public String getSessionPages(String retailPage) {
        StoLog.d("updateSession", "getSessionPages");

        /**
         * Verifies if the retailPage is not null
         */
        if (retailPage == null) {
            StoLog.d("updateSession", "getSessionPages: retailPage is null!");
            return "0";
        }

        /**
         * Verifies if the current page equals to the previous page
         */
        if (retailPage.compareTo(previousPage) == 0) {
            StoLog.d("updateSession", "getSessionPages: retail page is the same as the previous one");
        } else {
            StoLog.d("updateSession", "getSessionPages: retail page is different to the previous one");
            this.incSessionPages();
        }

        StoLog.d("updateSession", "getSessionPages: " + this.nbSessionPages);

        /**
         * Sets the previous page
         */
        previousPage = retailPage;

        return String.valueOf(this.nbSessionPages);
    }


    /**
     * Returns the sessionTime
     *
     * @return
     */
    public String getSessionTime() {
        long currentTime = System.currentTimeMillis();

        // Verifies if the call of the last action is not 0
        if (this.lastActionCallTime == 0) {
            //  StoLog.i("updateSession", "getSessionTime: lastActionTime == 0");
            this.lastActionCallTime = currentTime;
        }
        long lastCall = stoPersistenceHandler.getLastActionCallTime();

        /**
         * Verifies if a lastActionCallTime has been saved
         */
        if (lastCall > 0) {
            if ((currentTime - lastCall) <= this.SESSION_TIMEOUT) {
                this.currentSessionTime += currentTime - lastCall;
            } else {
                /**
                 * Resets the number of session pages
                 */
                this.resetSessionPages();
                /**
                 * Sets the currentSessionTime on 0
                 */
                this.currentSessionTime = 0;
            }
        } else {
            /**
             * Resets the number of session pages
             */
            this.resetSessionPages();
            /**
             * Sets the currentSessionTime on 0
             */
            this.currentSessionTime = 0;
        }

        // Converts the currentSessionTime to String
        String currentSessionTimeString = String.valueOf(currentSessionTime);

        // Sets the time of the last action call
        this.lastActionCallTime = currentTime;

        /**
         * Saves the time of the last call
         */
        stoPersistenceHandler.setLastActionCallTime(currentTime);

        StoLog.i("updateSession", "getSessionTime: sessionTime: " + currentSessionTimeString);

        return currentSessionTimeString;
    }

    /**
     * Starts the track time
     */
    public synchronized void startTrackTime() {
        StoLog.i(TAG, "startTrackTime");

        /**
         * Sets the startTrack time
         */
        this.startTrackTime = System.currentTimeMillis();
    }

    /**
     * Returns the track time
     * Time between the lod call and other actions
     *
     * @return
     */
    public synchronized String getTrackTime() {
        StoLog.i(TAG, "getTrackTime");

        /**
         * Calculates the trackTime
         */
        long trackTime = System.currentTimeMillis() - this.startTrackTime;

        StoLog.d(TAG, "getTrackTime: " + trackTime);


        return String.valueOf(trackTime);
    }

    public synchronized String getVerifUID() {
        StoLog.i(TAG, "getVerifUID");

        /**
         * Verifies if the verifUID is expired
         */
        if (isVerifUIDExpired()) {
            StoLog.d(TAG, "getVerifUID: verifUID is expired!");
            return updateVerifUID();

        } else {
            String lastVerifUID = getLastVerifUID();

            if (lastVerifUID != null) {
                StoLog.d(TAG, "getVerifUID: returns last verif uid:" + lastVerifUID);
                return lastVerifUID;
            } else {
                return updateVerifUID();
            }
        }
    }

    /**
     * Verifies if the verif uid is expired
     *
     * @return
     */
    private synchronized boolean isVerifUIDExpired() {
        StoLog.d(TAG, "isVerifUIDExpired");

        long currentTime = System.currentTimeMillis();

        long lastActionCall = stoPersistenceHandler.getLastActionCallTime();

        if (lastActionCall != -1 && lastActionCall != 0) {

            if ((currentTime - lastActionCall) != 0) {
                StoLog.d(TAG, "isVerifUIDExpired: " + !(((currentTime - lastActionCall) / 1000) <= VERIF_UID_LIFE));
                return !(((currentTime - lastActionCall) / 1000) <= VERIF_UID_LIFE);
            } else {
                return false;
            }

        } else {
            StoLog.d(TAG, "isVerifUIDExpired: verifUID is not expired: diff: " + ((currentTime - lastActionCall) / 1000));
            StoLog.d(TAG, "isVerifUIDExpired: verifUID is not expired: VERIF_UID_LIFE: " + VERIF_UID_LIFE);
            return false;
        }
    }

    private String getLastVerifUID() {
        StoLog.d(TAG, "getLastVerifUID");
        return this.stoPersistenceHandler.getVerifUID();
    }


    private String updateVerifUID() {
        StoLog.d(TAG, "updateVerifUID");

        // Generates the verif UID
        String uid = StoTrackerUtils.getRandomizedUID();
        // Saves the verif uid
        this.stoPersistenceHandler.setVerifUID(uid);
        StoLog.i(TAG, "updateVerifUID: " + uid);
        return uid;
    }


    public void removeFormats() {
        this.stoViewManager.removeFormats();
    }

}
