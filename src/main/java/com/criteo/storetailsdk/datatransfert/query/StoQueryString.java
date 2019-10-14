package com.criteo.storetailsdk.datatransfert.query;

/**
 * Created by MikhailPOGORELOV on 05/04/2017.
 */

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.parameters.*;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.StoTracker;

import java.util.*;

/**
 * Class representing the queries send to the server
 * Singleton Design Pattern
 */
public class StoQueryString {

    private final String TAG = "StoQueryString";

    private String server_url;

    private QSPAvailableProducts qspAvailableProducts;
    private QSPClientId qspClientId;
    private QSPOptInOut qspOptInOut;
    private QSPSessionTime qspSessionTime;
    private QSPSessionPages qspSessionPages;
    private QSPTechBrowserV qspTechBrowserV;
    private QSPRetailName qspRetailName;
    private QSPRetailPage qspRetailPage;
    private QSPRetailShop qspRetailShop;
    private QSPRetailSearch qspRetailSearch;
    private QSPRetailPlacements qspRetailPlacements;
    private QSPTrackBrand qspTrackBrand;
    private QSPTrackOperation qspTrackOperation;
    private QSPTrackInsertion qspTrackInsertion;
    private QSPTrackCreative qspTrackCreative;
    private QSPPositionObject qspPositionObject;
    private QSPTrackTime qspTrackTime;
    private QSPTrackValue qspTrackValue;
    private QSPProductID qspProductID;
    private QSPTrackLabel qspTrackLabel;
    private QSPTrackAction qspTrackAction;
    private QSPTrackEvent qspTrackEvent;
    private QSPProductLabel qspProductLabel;
    private QSPForcedCity qspForcedCity;
    private QSPForcedLatitude qspForcedLatitude;
    private QSPForcedLongitude qspForcedLongitude;
    private QSPVerifUID qspVerifUID;
    private QSPPageType qspPageType;

    private List<StoQueryStringParam> queryStringParams;


    private static StoQueryString instance = null;

    public StoQueryString(Context context) {
        this.server_url = context.getResources().getString(R.string.sto_server_url);

        this.queryStringParams = new ArrayList<>();

        this.qspTechBrowserV = new QSPTechBrowserV(context);
        this.qspAvailableProducts = new QSPAvailableProducts(context);
        this.qspClientId = new QSPClientId(context);
        this.qspOptInOut = new QSPOptInOut(context);
        this.qspSessionTime = new QSPSessionTime(context);
        this.qspSessionPages = new QSPSessionPages(context);
        this.qspRetailName = new QSPRetailName(context);
        this.qspRetailPage = new QSPRetailPage(context);
        this.qspRetailShop = new QSPRetailShop(context);
        this.qspRetailSearch = new QSPRetailSearch(context);
        this.qspRetailPlacements = new QSPRetailPlacements(context);
        this.qspPositionObject = new QSPPositionObject(context);
        this.qspTrackBrand = new QSPTrackBrand(context);
        this.qspTrackOperation = new QSPTrackOperation(context);
        this.qspTrackInsertion = new QSPTrackInsertion(context);
        this.qspTrackCreative = new QSPTrackCreative(context);
        this.qspTrackTime = new QSPTrackTime(context);
        this.qspTrackValue = new QSPTrackValue(context);
        this.qspProductID = new QSPProductID(context);
        this.qspTrackLabel = new QSPTrackLabel(context);
        this.qspTrackAction = new QSPTrackAction(context);
        this.qspTrackEvent = new QSPTrackEvent(context);
        this.qspProductLabel = new QSPProductLabel(context);
        this.qspForcedCity = new QSPForcedCity(context);
        this.qspForcedLatitude = new QSPForcedLatitude(context);
        this.qspForcedLongitude = new QSPForcedLongitude(context);
        this.qspVerifUID = new QSPVerifUID(context);
        this.qspPageType = new QSPPageType(context);

        queryStringParams.addAll(Arrays.asList(qspTechBrowserV, qspAvailableProducts, qspClientId, qspOptInOut, qspRetailName, qspRetailPage, qspRetailSearch, qspRetailShop, qspSessionPages, qspSessionTime, qspPositionObject, qspTrackBrand,
                qspTrackCreative, qspTrackEvent, qspTrackInsertion, qspProductID, qspTrackLabel, qspTrackOperation, qspRetailPlacements, qspTrackTime, qspTrackValue,
                 qspTrackAction, qspProductLabel, qspForcedCity, qspForcedLatitude, qspForcedLongitude, qspVerifUID, qspPageType));
    }

    public synchronized static StoQueryString getInstance(Context context) {
        if (instance == null) {
            synchronized (StoQueryString.class) {
                if (instance == null) {
                    instance = new StoQueryString(context);
                }
            }
        }
        return instance;
    }

    public String getTAG() {
        return TAG;
    }

    public String getServer_url() {
        return server_url;
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }

    public QSPTechBrowserV getQspTechBrowserV() {
        return qspTechBrowserV;
    }

    public void setQspTechBrowserV(QSPTechBrowserV qspTechBrowserV) {
        this.qspTechBrowserV = qspTechBrowserV;
    }

    public QSPAvailableProducts getQspAvailableProducts() {
        return qspAvailableProducts;
    }

    public void setQspAvailableProducts(QSPAvailableProducts qspAvailableProducts) {
        this.qspAvailableProducts = qspAvailableProducts;
    }

    public QSPClientId getQspClientId() {
        return qspClientId;
    }

    public void setQspClientId(QSPClientId qspClientId) {
        this.qspClientId = qspClientId;
    }

    public QSPOptInOut getQspOptInOut() {
        return qspOptInOut;
    }

    public void setQspOptInOut(QSPOptInOut qspOptInOut) {
        this.qspOptInOut = qspOptInOut;
    }

    public QSPSessionTime getQspSessionTime() {
        return qspSessionTime;
    }

    public void setQspSessionTime(QSPSessionTime qspSessionTime) {
        this.qspSessionTime = qspSessionTime;
    }

    public QSPSessionPages getQspSessionPages() {
        return qspSessionPages;
    }

    public void setQspSessionPages(QSPSessionPages qspSessionPages) {
        this.qspSessionPages = qspSessionPages;
    }

    public QSPRetailName getQspRetailName() {
        return qspRetailName;
    }

    public void setQspRetailName(QSPRetailName qspRetailName) {
        this.qspRetailName = qspRetailName;
    }

    public QSPRetailPage getQspRetailPage() {
        return qspRetailPage;
    }

    public void setQspRetailPage(QSPRetailPage qspRetailPage) {
        this.qspRetailPage = qspRetailPage;
    }

    public QSPRetailShop getQspRetailShop() {
        return qspRetailShop;
    }

    public void setQspRetailShop(QSPRetailShop qspRetailShop) {
        this.qspRetailShop = qspRetailShop;
    }

    public QSPRetailSearch getQspRetailSearch() {
        return qspRetailSearch;
    }

    public void setQspRetailSearch(QSPRetailSearch qspRetailSearch) {
        this.qspRetailSearch = qspRetailSearch;
    }

    public QSPRetailPlacements getQspRetailPlacements() {
        return qspRetailPlacements;
    }

    public void setQspRetailPlacements(QSPRetailPlacements qspRetailPlacements) {
        this.qspRetailPlacements = qspRetailPlacements;
    }

    public QSPTrackBrand getQspTrackBrand() {
        return qspTrackBrand;
    }

    public void setQspTrackBrand(QSPTrackBrand qspTrackBrand) {
        this.qspTrackBrand = qspTrackBrand;
    }

    public QSPTrackOperation getQspTrackOperation() {
        return qspTrackOperation;
    }

    public void setQspTrackOperation(QSPTrackOperation qspTrackOperation) {
        this.qspTrackOperation = qspTrackOperation;
    }

    public QSPTrackInsertion getQspTrackInsertion() {
        return qspTrackInsertion;
    }

    public void setQspTrackInsertion(QSPTrackInsertion qspTrackInsertion) {
        this.qspTrackInsertion = qspTrackInsertion;
    }

    public QSPTrackCreative getQspTrackCreative() {
        return qspTrackCreative;
    }

    public void setQspTrackCreative(QSPTrackCreative qspTrackCreative) {
        this.qspTrackCreative = qspTrackCreative;
    }

    public QSPTrackTime getQspTrackTime() {
        return qspTrackTime;
    }

    public void setQspTrackTime(QSPTrackTime qspTrackTime) {
        this.qspTrackTime = qspTrackTime;
    }

    public QSPTrackValue getQspTrackValue() {
        return qspTrackValue;
    }

    public void setQspTrackValue(QSPTrackValue qspTrackValue) {
        this.qspTrackValue = qspTrackValue;
    }

    public QSPProductID getQspProductID() {
        return qspProductID;
    }

    public void setQspProductID(QSPProductID qspProductID) {
        this.qspProductID = qspProductID;
    }

    public QSPTrackLabel getQspTrackLabel() {
        return qspTrackLabel;
    }

    public void setQspTrackLabel(QSPTrackLabel qspTrackLabel) {
        this.qspTrackLabel = qspTrackLabel;
    }

    public QSPTrackAction getQspTrackAction() {
        return qspTrackAction;
    }

    public void setQspTrackAction(QSPTrackAction qspTrackAction) {
        this.qspTrackAction = qspTrackAction;
    }


    public QSPPositionObject getQspPositionObject() {
        return qspPositionObject;
    }

    public void setQspPositionObject(QSPPositionObject qspPositionObject) {
        this.qspPositionObject = qspPositionObject;
    }

    public QSPTrackEvent getQspTrackEvent() {
        return qspTrackEvent;
    }

    public void setQspTrackEvent(QSPTrackEvent qspTrackEvent) {
        this.qspTrackEvent = qspTrackEvent;
    }

    public QSPProductLabel getQspProductLabel() {
        return qspProductLabel;
    }

    public void setQspProductLabel(QSPProductLabel qspProductLabel) {
        this.qspProductLabel = qspProductLabel;
    }

    public QSPForcedCity getQspForcedCity() {
        return qspForcedCity;
    }

    public void setQspForcedCity(QSPForcedCity qspForcedCity) {
        this.qspForcedCity = qspForcedCity;
    }

    public QSPForcedLatitude getQspForcedLatitude() {
        return qspForcedLatitude;
    }

    public void setQspForcedLatitude(QSPForcedLatitude qspForcedLatitude) {
        this.qspForcedLatitude = qspForcedLatitude;
    }

    public QSPForcedLongitude getQspForcedLongitude() {
        return qspForcedLongitude;
    }

    public void setQspForcedLongitude(QSPForcedLongitude qspForcedLongitude) {
        this.qspForcedLongitude = qspForcedLongitude;
    }

    public QSPVerifUID getQspVerifUID() {
        return qspVerifUID;
    }

    public void setQspVerifUID(QSPVerifUID qspVerifUID) {
        this.qspVerifUID = qspVerifUID;
    }

    public QSPPageType getQspPageType() {
        return qspPageType;
    }

    public void setQspPageType(QSPPageType qspPageType) {
        this.qspPageType = qspPageType;
    }

    /**
     * Clears the StoQueryString
     */
    public void clear() {
        for (StoQueryStringParam qsp : queryStringParams) {
            qsp.setValue("");
        }
        StoLog.i(TAG, "StoQueryStringParams cleared");
    }


    /**
     * Returns list of current parameters
     *
     * @return
     */
    public List<StoQueryStringParam> getQueryStringParams() {
        return queryStringParams;
    }


    /**
     * Returns the map params <Name, Value>
     *
     * @return
     */
    public Map<String, String> getQueryStringParamsMap() {
        //StoLog.d(TAG, "getQueryStringParamsMap");

        Map<String, String> params = new HashMap<>();

        for (StoQueryStringParam param : this.queryStringParams) {
            if (param.getValue() != null) {
                //StoLog.d(TAG, "getQueryStringParamsMap: " + param.getName() + " ,value: " + param.getValue());
                params.put(param.getName(), param.getValue());
            }
        }


        return params;
    }


    /**
     * Returns the query string
     *
     * @return
     */
    public String getQueryString() {
        // Adds the server_url in the beginning of the query
        String queryString = "" + server_url;

        List<StoQueryStringParam> params;
        params = getQueryStringParams();

        // Concatenation
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i) != null) {
                if (params.get(i).getValue() != null) {
                    if (params.get(i).getValue().compareTo("") != 0) {
                        if (i == 0) {
                            queryString = concatQueryStringParam(true, queryString, params.get(i));
                        } else {
                            queryString = concatQueryStringParam(false, queryString, params.get(i));
                        }
                    } else {
                        StoLog.w(TAG, "getQueryString: parameter's value is empty!");
                    }
                } else {
                    //StoLog.d(TAG, i + " value is null");
                }
            } else {
                //StoLog.d(TAG, i + " is null");
            }
        }

        //Additional operation on the query string
        // Removes spaces
        queryString = queryString.replace(" ", "+");

        return queryString;
    }

    /**
     * Concatenates the parameter to the query if it has a value
     *
     * @param queryString
     * @param q
     */
    private String concatQueryStringParam(boolean isFirst, String queryString, StoQueryStringParam q) {
        //StoLog.d(TAG, "concatQueryStringParam: " + "name : " + q.getName() + " value " + q.getValue());
        if (!q.getValue().equals("")) {
            // Adds the separator if need
            if (!isFirst) queryString += "&";
            queryString += q.getName() + "=" + q.getValue();
        }

        return queryString;
    }

    /**
     * Returns a StoQueryStringParam by name
     *
     * @param name
     * @return
     */
    public StoQueryStringParam getQSPByName(String name) {
        for (StoQueryStringParam qsp : getQueryStringParams()) {
            if (qsp != null) {
                if (qsp.getName().equals(name)) {
                    return qsp;
                }
            }
        }
        return null;
    }


    public int countUsedParameters() {
        int res = 0;
        List<StoQueryStringParam> params = getQueryStringParams();
        for (StoQueryStringParam qsp : params) {
            if (qsp.getValue() != null) {
                res++;
            }
        }
        return res;
    }

    /**
     * Prints query in the logcat
     */
    public void printQuery(boolean detailsOn) {
        StoLog.i(TAG, "printQuery:" + getQueryString());
        /**
         * Prints name and value of each {@link StoQueryStringParam}
         */

        String trackAction = this.qspTrackAction.getValue();

        int nbParams = 0;
        if (detailsOn) {
            for (StoQueryStringParam queryStringParam : this.queryStringParams) {
                if (queryStringParam.getValue() != null) {
                    nbParams++;
                    StoLog.i(TAG, "printQuery: " + trackAction + " name: " + queryStringParam.getName() + " value: " + queryStringParam.getValue());
                }
            }
        }
        StoLog.i(TAG, "printQuery: " + trackAction + " nbParams: " + nbParams);
    }
}
