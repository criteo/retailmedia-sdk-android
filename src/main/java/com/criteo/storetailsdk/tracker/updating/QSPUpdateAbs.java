package com.criteo.storetailsdk.tracker.updating;

/**
 * Created by MikhailPOGORELOV on 14/04/2017.
 */

import android.content.Context;
import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.datatransfert.query.parameters.*;
import com.criteo.storetailsdk.datatransfert.values.StoTrackActionValue;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;

/**
 * Abstract class which updates QueryStringParams
 * Handles the StoQueryString updates
 * <p>
 * Contains only synchronized methods
 */
public abstract class QSPUpdateAbs {

    private final String TAG = "QSPUpdateAbs";


    protected Context context;

    /**
     * Updates page type parameter in the StoQueryString
     *
     * @param stoQueryString
     */
    public synchronized void updateQSPPageType(StoQueryString stoQueryString, final String pageType) {
        if (stoQueryString.getQspPageType() == null) {
            stoQueryString.setQspPageType(new QSPPageType(context, pageType));
        } else {
            stoQueryString.getQspPageType().setValue(pageType);
        }
    }

    /**
     * Updates client id parameter (gets the device id) in the StoQueryString
     *
     * @param stoQueryString
     */
    public synchronized void updateQSPVerifUID(StoQueryString stoQueryString, final String verifUID) {
        if (stoQueryString.getQspVerifUID() == null) {
            stoQueryString.setQspVerifUID(new QSPVerifUID(context, verifUID));
        } else {
            stoQueryString.getQspVerifUID().setValue(verifUID);
        }
    }

    /**
     * Updates optin-optout's parameter
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPOptinOut(StoQueryString stoQueryString, final int value) {
        if (stoQueryString.getQspOptInOut() == null) {
            stoQueryString.setQspOptInOut(new QSPOptInOut(context, String.valueOf(value)));
        } else {
            stoQueryString.getQspOptInOut().setValue(String.valueOf(value));
        }
    }


    /**
     * Updates trackAction in the StoQueryString
     *
     * @param stoQueryString
     */
    public synchronized void updateQSPTrackAction(StoQueryString stoQueryString, StoTrackActionValue stoTrackActionValue) {
        if (stoQueryString.getQspTrackAction() == null) {
            stoQueryString.setQspTrackAction(new QSPTrackAction(context, stoTrackActionValue.toString()));
        } else {
            stoQueryString.getQspTrackAction().setValue(stoTrackActionValue.toString());
        }
    }

    /**
     * Updates the version of the Application in the tracker
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTechBrowserV(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTechBrowserV() == null) {
            stoQueryString.setQspTechBrowserV(new QSPTechBrowserV(context, value));
        } else {
            stoQueryString.getQspTechBrowserV().setValue(value);
        }
    }


    public synchronized void updatePositionObject(StoQueryString stoQueryString, int value) {
        if (stoQueryString.getQspPositionObject() == null) {
            stoQueryString.setQspPositionObject(new QSPPositionObject(context, String.valueOf(value)));
        } else {
            stoQueryString.getQspPositionObject().setValue(String.valueOf(value));
        }
    }

    /**
     * Updates the AvailableProducts parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPAvailableProducts(StoQueryString stoQueryString, int value) {
        if (stoQueryString.getQspAvailableProducts() == null) {
            stoQueryString.setQspAvailableProducts(new QSPAvailableProducts(context, String.valueOf(value)));
        } else {
            stoQueryString.getQspAvailableProducts().setValue(String.valueOf(value));
        }
    }

    /**
     * Updates ForcedCity parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPForcedCity(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspForcedCity() == null) {
            stoQueryString.setQspForcedCity(new QSPForcedCity(context, value));
        } else {
            stoQueryString.getQspForcedCity().setValue(value);
        }
    }

    /**
     * Updates the ForcedLatitude parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPForcedLatitude(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspForcedLatitude() == null) {
            stoQueryString.setQspForcedLatitude(new QSPForcedLatitude(context, value));
        } else {
            stoQueryString.getQspForcedLatitude().setValue(value);
        }
    }

    /**
     * Updates the ForcedLongitude parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPForcedLongitude(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspForcedLongitude() == null) {
            stoQueryString.setQspForcedLongitude(new QSPForcedLongitude(context, value));
        } else {
            stoQueryString.getQspForcedLongitude().setValue(value);
        }
    }


    /**
     * Updates the retailName parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPRetailName(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspRetailName() == null) {
            stoQueryString.setQspRetailName(new QSPRetailName(context, value));
        } else {
            stoQueryString.getQspRetailName().setValue(value);
        }
    }

    /**
     * Updates the retailPage in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPRetailPage(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspRetailPage() == null) {
            stoQueryString.setQspRetailPage(new QSPRetailPage(context, value));
        } else {
            stoQueryString.getQspRetailPage().setValue(value);
        }
    }

    /**
     * Updates the RetailShop parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPRetailShop(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspRetailShop() == null) {
            stoQueryString.setQspRetailShop(new QSPRetailShop(context, value));
        } else {
            stoQueryString.getQspRetailShop().setValue(value);
        }
    }


    /**
     * Updates the retailSearchParameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPRetailSearch(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspRetailSearch() == null) {
            stoQueryString.setQspRetailSearch(new QSPRetailSearch(context, value));
        } else {
            stoQueryString.getQspRetailSearch().setValue(value);
        }
    }

    /**
     * Updates the TrackBrand parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackBrand(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackBrand() == null) {
            stoQueryString.setQspTrackBrand(new QSPTrackBrand(context, value));
        } else {
            stoQueryString.getQspTrackBrand().setValue(value);
        }
    }

    /**
     * Updates the TrackOperation parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackOperation(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackOperation() == null) {
            stoQueryString.setQspTrackOperation(new QSPTrackOperation(context, value));
        } else {
            stoQueryString.getQspTrackOperation().setValue(value);
        }
    }

    /**
     * Updates the TrackCreative parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackCreative(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackCreative() == null) {
            stoQueryString.setQspTrackCreative(new QSPTrackCreative(context, value));
        } else {
            stoQueryString.getQspTrackCreative().setValue(value);
        }
    }

    /**
     * Updates the TrackInsertion parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackInsertion(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackInsertion() == null) {
            stoQueryString.setQspTrackInsertion(new QSPTrackInsertion(context, value));
        } else {
            stoQueryString.getQspTrackInsertion().setValue(value);
        }
    }

    /**
     * Updates the TrackTime parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackTime(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackTime() == null) {
            stoQueryString.setQspTrackTime(new QSPTrackTime(context, value));
        } else {
            stoQueryString.getQspTrackTime().setValue(value);
        }
    }

    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param stoTrackEventValue
     */
    public synchronized void updateQSPTrackEvent(StoQueryString stoQueryString, StoTrackEventValue stoTrackEventValue) {
        if (stoQueryString.getQspTrackEvent() == null) {
            if (stoTrackEventValue != null) {
                stoQueryString.setQspTrackEvent(new QSPTrackEvent(context, stoTrackEventValue.toString()));
            } else {
                stoQueryString.setQspTrackEvent(new QSPTrackEvent(context, null));
            }
        } else {
            if (stoTrackEventValue != null) {
                stoQueryString.getQspTrackEvent().setValue(StoTrackerUtils.getUpdatedTrackEventValue(stoTrackEventValue));
            } else {
                stoQueryString.getQspTrackEvent().setValue(null);
            }
        }
    }

    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackLabel(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackLabel() == null) {
            stoQueryString.setQspTrackLabel(new QSPTrackLabel(context, value));
        } else {
            stoQueryString.getQspTrackLabel().setValue(value);
        }
    }

    /**
     * Updates the ProductID parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPProductID(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspProductID() == null) {
            stoQueryString.setQspProductID(new QSPProductID(context, value));
        } else {
            stoQueryString.getQspProductID().setValue(value);
        }
    }


    /**
     * Updates the RetailPlacement parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPRetailPlacement(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspRetailPlacements() == null) {
            stoQueryString.setQspRetailPlacements(new QSPRetailPlacements(context, value));
        } else {
            stoQueryString.getQspRetailPlacements().setValue(value);
        }
    }


    /**
     * Updates the TrackEvent parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPTrackValue(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspTrackValue() == null) {
            stoQueryString.setQspTrackValue(new QSPTrackValue(context, value));
        } else {
            stoQueryString.getQspTrackValue().setValue(value);
        }
    }


    /**
     * Updates the ProductLabel parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPProductLabel(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspProductLabel() == null) {
            stoQueryString.setQspProductLabel(new QSPProductLabel(context, value));
        } else {
            stoQueryString.getQspProductLabel().setValue(value);
        }
    }

    /**
     * Updates the SessionPages parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPSessionPages(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspSessionPages() == null) {
            stoQueryString.setQspSessionPages(new QSPSessionPages(context, value));
        } else {
            stoQueryString.getQspSessionPages().setValue(value);
        }
    }


    /**
     * Updates the SessionTime parameter in the StoQueryString
     *
     * @param stoQueryString
     * @param value
     */
    public synchronized void updateQSPSessionTime(StoQueryString stoQueryString, String value) {
        if (stoQueryString.getQspSessionTime() == null) {
            stoQueryString.setQspSessionTime(new QSPSessionTime(context, value));
        } else {
            stoQueryString.getQspSessionTime().setValue(value);
        }
    }
}