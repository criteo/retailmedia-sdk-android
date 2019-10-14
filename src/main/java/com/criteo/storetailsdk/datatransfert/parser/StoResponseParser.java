package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.datatransfert.query.parameters.*;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.view.models.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by mikhailpogorelov on 06/11/2017.
 */
public class StoResponseParser {

    private final String TAG = "StoResponseParser";

    private Context context;

    private StoBannerParserLogic stoBannerParserLogic;
    private StoVignetteParserLogic stoVignetteParserLogic;
    private StoButterflyParserLogic stoButterflyParserLogic;


    public StoResponseParser(Context context) {
        this.context = context;
        Gson gson = new GsonBuilder().create();

        this.stoBannerParserLogic = new StoBannerParserLogic(this.context, gson);
        this.stoVignetteParserLogic = new StoVignetteParserLogic(this.context, gson);
        this.stoButterflyParserLogic = new StoButterflyParserLogic(this.context, gson);
    }

    /**
     * Returns a JSONObject data from the given response
     *
     * @param response
     * @return
     */
    private JSONObject getJSON(String response) {
        JSONObject jsonObject = null;

        if (response != null) {
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException je) {
                StoLog.e(TAG, "getJSON");
                if (je.getMessage() != null) {
                    StoLog.e(TAG, "getJSON: " + je.getMessage());
                }
            }
        } else {
            StoLog.e(TAG, "getJSON: response is null!");
        }
        return jsonObject;
    }

    /**
     * Gets the list of formats from the response
     *
     * @param response
     * @return
     */
    public synchronized List<StoFormat> getFormats(String response) {
        List<StoFormat> stoFormats = new ArrayList<>();

        if (response == null || response.isEmpty()) {
            StoLog.e(TAG, "getFormats: response is empty or null!");
            return stoFormats;
        }

        JSONObject jsonObject = this.getJSON(response);

        if (jsonObject == null) {
            StoLog.e(TAG, "getFormats: extracted json from the response is null!");
            return stoFormats;
        }

        StoFormatParser stoParser = null;
        try {
            stoParser = new StoFormatParser(context, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return stoFormats;
        }

        stoParser.setParser(this.stoBannerParserLogic);
        stoFormats.addAll(stoParser.parse());

        stoParser.setParser(this.stoVignetteParserLogic);
        stoFormats.addAll(stoParser.parse());

        stoParser.setParser(this.stoButterflyParserLogic);
        stoFormats.addAll(stoParser.parse());

        stoFormats.addAll(stoParser.parseCustom());

        return stoFormats;
    }

    /**
     * Returns the map of noexp parameters: tc: queryStringParams
     *
     * @param response
     * @return: List
     */
    public List<List<StoQueryStringParam>> getNoExpParams(final String response) {
        List<List<StoQueryStringParam>> queryStringParamList = new ArrayList<>();

        if (response == null) {
            StoLog.e(TAG, "getNoExpParams: response is null!");
            return queryStringParamList;
        }

        if (response.isEmpty()) {
            StoLog.e(TAG, "getNoExpParams: response is empty!");
            return queryStringParamList;
        }

        /**
         * Gets the json
         */
        JSONObject jsonObject = this.getJSON(response);

        /**
         * Verifies the jsonObject
         */
        if (jsonObject == null) {
            StoLog.e(TAG, "getFormats: extracted json from the response is null!");
            return queryStringParamList;
        }

        try {

            /**
             * Extracts the noexposed object
             */
            JSONObject unexposedObject = jsonObject.getJSONObject(context.getString(R.string.sto_response_parser_creatives)).
                    getJSONObject(context.getString(R.string.sto_response_parser_unexposed));


            /**
             * Fill in the map with the attributes of the jsonObject
             */
            Map<String, Object> unexposedMap = new HashMap<>(this.jsonObjectToMap(unexposedObject));


            for (Map.Entry entry : unexposedMap.entrySet()) {

                List<StoQueryStringParam> queryStringParams = new ArrayList<>();

                if (((JSONObject) entry.getValue()).getString("tb") != null) {
                    queryStringParams.add(new QSPTrackBrand(context, ((JSONObject) entry.getValue()).getString("tb")));
                }
                if (((JSONObject) entry.getValue()).getString("tc") != null) {
                    queryStringParams.add(new QSPTrackCreative(context, ((JSONObject) entry.getValue()).getString("tc")));
                }
                if (((JSONObject) entry.getValue()).getString("to") != null) {
                    queryStringParams.add(new QSPTrackOperation(context, ((JSONObject) entry.getValue()).getString("to")));
                }
                if (((JSONObject) entry.getValue()).getString("ti") != null) {
                    queryStringParams.add(new QSPTrackInsertion(context, ((JSONObject) entry.getValue()).getString("ti")));
                }
                queryStringParamList.add(queryStringParams);
            }

            for (int i = 0; i < queryStringParamList.size(); i++) {
                for (int j = 0; j < queryStringParamList.get(i).size(); j++) {
                    StoLog.d(TAG, "getNoExpParams: " + queryStringParamList.get(i).get(j).getName() + " " + queryStringParamList.get(i).get(j).getValue());
                }
            }
        } catch (JSONException je) {
            if (je.getMessage() != null) {
                StoLog.e(TAG, "getNoExpParams: " + je.getMessage());
            } else {
                StoLog.e(TAG, "getNoExpParams: " + je.toString() + " thrown");
            }
        }
        return queryStringParamList;
    }


    /**
     * Converts jsonObject to map
     *
     * @param jsonObject
     * @return
     */
    private Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        Map<String, Object> map = new HashMap<>();

        //noinspection finally
        try {
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, jsonObject.getJSONObject(key));
            }
            return map;
        } catch (JSONException je) {
            if (je.getMessage() != null) {
                StoLog.e(TAG, "jsonObjectToMap: " + je.getMessage());
            } else {
                StoLog.e(TAG, "jsonObjectToMap: " + je.toString() + " thrown");
            }
        }
        return map;
    }
}
