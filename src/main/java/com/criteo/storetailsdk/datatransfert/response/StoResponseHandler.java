package com.criteo.storetailsdk.datatransfert.response;

import android.annotation.SuppressLint;
import android.content.Context;
import com.criteo.storetailsdk.datatransfert.parser.StoResponseParser;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.extras.StoEventCommunicator;
import com.criteo.storetailsdk.extras.StoFormatsListener;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.StoTracker;
import com.criteo.storetailsdk.tracker.StoTrackerHelper;
import com.criteo.storetailsdk.view.models.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class especially dedicated for the Storetail App
 * Handles the responses
 * Singleton Design Pattern
 */
public class StoResponseHandler extends StoResponseHandlerImp {

    private final static String TAG = "StoResponseHandlerImp";


    @SuppressLint("StaticFieldLeak")
    private static StoResponseHandler instance = null;

    /**
     * StoResponseHandler uses the references of the TrackerHelper of the Tracker
     */
    private StoTrackerHelper trackerHelper;


    /**
     * StoResponseParser is used for parsing of the server responses
     */
    private StoResponseParser stoResponseParser;


    private StoResponseHandler(Context context, StoTrackerHelper trackerHelper) {
        this.context = context;
        this.trackerHelper = trackerHelper;
        this.stoResponseParser = new StoResponseParser(context);
    }

    /**
     * Synchronization only for creating of the instance
     *
     * @return Singleton StoResponseHandler instance
     */
    public static StoResponseHandler getInstance(Context context, StoTrackerHelper trackerHelper) {
        if (instance == null) {
            synchronized (StoResponseHandler.class) {
                if (instance == null) {
                    instance = new StoResponseHandler(context, trackerHelper);
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized void handleResponse(final String response, final String queryString, final String trackActionValue, String identifier) {
        if (trackActionValue.compareTo("lod") != 0) {
            return;
        }

        if (response == null || response.isEmpty()) {
            StoLog.e(TAG, "handleResponse: response is null or empty!");
            return;
        }

        StoLog.d(TAG, "handleResponse: response: " + response);

        List<StoFormat> stoFormatsRep = stoResponseParser.getFormats(response);

        List<List<StoQueryStringParam>> noExpList = new ArrayList<>(stoResponseParser.getNoExpParams(response));

        for (int i = 0; i < noExpList.size(); i++) {
            StoTracker.getInstance(context).noexp(noExpList.get(i));
        }

        List<StoFormat> stoFormats = new ArrayList<>(stoFormatsRep);

        StoLog.d(TAG, "handleResponse: " + stoFormats.size());

        if (!stoFormats.isEmpty()) {
            if (StoEventCommunicator.hasStoFormatsListeners()) {
                StoLog.d(TAG, "handleResponse: returns formats to listeners");

                for (StoFormatsListener listener : StoEventCommunicator.getStoFormatsListeners()) {
                    listener.onStoFormatsReceived(stoFormats, identifier);
                }
            } else {
                // TODO : Remove this.trackerHelper.getStoViewManager().addStoFormats(stoFormats, StoTracker.getInstance(context).getStoPageType());
            }
        } else {
            StoLog.d(TAG, "handleResponse: stoFormats list is empty");
        }
    }

    @Override
    public void handleResponse(final JSONObject jsonObject, final String queryString) {
        StoLog.d(TAG, "StoResponseHandler handle response JSONObject" + jsonObject.toString());
    }

    @Override
    public void handleFailure(String message, String identifier) {
        for (StoFormatsListener listener : StoEventCommunicator.getStoFormatsListeners()) {
            listener.onStoFailure(message, identifier);
        }
        StoLog.e(TAG, "handleFailure: " + message);
    }
}
