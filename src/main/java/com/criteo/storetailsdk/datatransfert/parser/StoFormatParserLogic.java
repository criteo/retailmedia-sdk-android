package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.datatransfert.query.parameters.*;
import com.criteo.storetailsdk.datatransfert.utils.JsonUtils;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.view.models.StoFormatCustom;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

abstract class StoFormatParserLogic {
    private String TAG = "StoFormatParserLogic";

    protected Context context;
    protected Gson gson;

    public StoFormatParserLogic(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
    }
    abstract String getTag();
    abstract int getStoFormatKeyResourceId();
    abstract StoFormat getStoFormat(JSONObject jsonObject);

    public List<StoFormat> parse(JSONObject jsonObject) {
        List<StoFormat> result = new ArrayList<>();

        try {
            Object object = jsonObject.get(context.getString(getStoFormatKeyResourceId()));
            if (object instanceof JSONObject) {
                JSONObject stoFormatJson = (JSONObject) object;
                JSONObject values = stoFormatJson.getJSONObject(context.getString(R.string.sto_response_parser_values));
                if (values == null) {
                    StoLog.e(TAG, "parse: getJSONObject values is null");
                    return result;
                }
                StoFormat stoFormat = getStoFormat(values);
                if (stoFormat != null) {
                    Map<String, Object> queryStringParams = getQueryStringParams(stoFormatJson, context.getString(getStoFormatKeyResourceId()));

                    if (queryStringParams == null || queryStringParams.isEmpty()) {
                        StoLog.e(TAG, "parse: queryStringParams map is empty!");
                        return result;
                    }

                    stoFormat.setQueryStringParamsList(getParams(queryStringParams));
                    result.add(stoFormat);
                } else {
                    StoLog.w(TAG, "parse: stoFormat is null");
                }
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (!jsonArray.isNull(i)) {
                            JSONObject stoFormatJson = jsonArray.getJSONObject(i);
                            JSONObject values = stoFormatJson.getJSONObject(context.getString(R.string.sto_response_parser_values));
                            StoFormat stoFormat = getStoFormat(values);
                            if (stoFormat != null) {
                                Map<String, Object> queryStringParams = getQueryStringParams(stoFormatJson, context.getString(getStoFormatKeyResourceId()));

                                if (queryStringParams == null || queryStringParams.isEmpty()) {
                                    StoLog.e(TAG, "parse: queryStringParams map is empty!");
                                    continue;
                                }

                                stoFormat.setQueryStringParamsList(getParams(queryStringParams));

                                result.add(stoFormat);
                            } else {
                                StoLog.w(TAG, "parse: stoFormat is null");
                            }
                        }
                    }
                } else {
                    StoLog.i(TAG, "parse: stoFormat list is empty");
                }
            } else {
                StoLog.w(TAG, "parse: jsonObject is not an instance of JSONArray");
            }
        } catch (JSONException e) {
            if (e.getMessage() != null) {
                StoLog.e(TAG, "parse: Exception: message " + e.getMessage());
            } else {
                StoLog.e(TAG, "parse: Exception: " + e.toString());
            }
        }

        return result;
    }

    public List<StoFormat> parseCustom(JSONObject jsonObject) {
        List<StoFormat> result = new ArrayList<>();

        for (Iterator<String> it = jsonObject.keys(); it.hasNext();) {
            String formatName = it.next();
            if (!formatName.contentEquals(context.getString(R.string.sto_response_parser_ba)) &&
                    !formatName.contentEquals(context.getString(R.string.sto_response_parser_bf)) &&
                    !formatName.contentEquals(context.getString(R.string.sto_response_parser_vi))) {
                try {
                    Object object = jsonObject.get(formatName);
                    if (object instanceof JSONArray) {
                        JSONArray stoFormatsJson = (JSONArray)object;
                        for (int i = 0; i < stoFormatsJson.length(); ++i) {
                            JSONObject stoFormatJson = stoFormatsJson.getJSONObject(i);
                            StoFormat format = parseCustomFormat(stoFormatJson, formatName);
                            if (format != null) {
                                result.add(format);
                            }
                        }
                    } else if (object instanceof JSONObject) {
                        JSONObject stoFormatJson = (JSONObject)object;
                        StoFormat format = parseCustomFormat(stoFormatJson, formatName);
                        if (format != null) {
                            result.add(format);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private StoFormat parseCustomFormat(JSONObject stoFormatJson, String formatName) throws JSONException{
        JSONObject valuesJson = stoFormatJson.getJSONObject(context.getString(R.string.sto_response_parser_values));

        StoFormatCustom stoFormatCustom = gson.fromJson(valuesJson.toString(), StoFormatCustom.class);
        stoFormatCustom.setFormatName(formatName);

        valuesJson.remove("path");

        stoFormatCustom.setValues(JsonUtils.jsonToMap(valuesJson));

        Map<String, Object> queryStringParams = getQueryStringParams(stoFormatJson, formatName);
        if (queryStringParams == null || queryStringParams.isEmpty()) {
            StoLog.e(TAG, "parseCustom: queryStringParams map is empty!");
            return null;
        }

        stoFormatCustom.setQueryStringParamsList(getParams(queryStringParams));
        return stoFormatCustom;
    }

    /**
     * Returns a HashMap with tracking parameters for the product view
     *
     * @param jsonObject stoFormat values json object
     * @param retailPlacement: name of the format(MHC1, MHC2, MBF, MBT)
     * @return HashMap with tracking parameters for the product view
     */
    private Map<String, Object> getQueryStringParams(JSONObject jsonObject, String retailPlacement) {
        //StoLog.i(TAG, "getQueryStringParams: object: " + jsonObject.toString());

        Map<String, Object> map = new HashMap<>();

        if (jsonObject == null) {
            StoLog.e(TAG, "getQueryStringParams: object is null!");
            return new HashMap<>();
        }

        map.put("re", retailPlacement);

        try {
            /*
             * Getting parameters
             */
            Iterator<String> keysItr = jsonObject.keys();

            String key;
            while (keysItr.hasNext()) {
                key = keysItr.next();
                /*
                 * Don't get "values" attribute
                 */
                if (jsonObject.get(key) instanceof String) {
                    if (jsonObject.get(key) != null) {
                        if (!(jsonObject.get(key)).equals(context.getString(R.string.sto_response_parser_values))) {
                            map.put(key, jsonObject.get(key));
                        } else {
                            StoLog.w(TAG, "getQueryStringParams: jsonObject.get(key) doesn't equal to value");
                        }
                    } else {
                        StoLog.w(TAG, "getQueryStringParams: jsonObject.get(key) is null");
                    }
                } else {
                    StoLog.w(TAG, "getQueryStringParams: jsonObject.get(key) is not a String");
                }
            }
            return map;
        } catch (JSONException je) {
            if (je.getMessage() != null) {
                StoLog.e(TAG, "getQueryStringParams: error: " + je.getMessage());
            } else {
                StoLog.e(TAG, "getQueryStringParams: error message");
            }
        }
        return map;
    }

    private synchronized List<StoQueryStringParam> getParams(Map<String, Object> mapParams) {

        if (mapParams == null) {
            StoLog.e(TAG, "getParams: mapParams is null!");
            return new ArrayList<>();
        }

        /*
         * List of parameters to return
         */
        List<StoQueryStringParam> queryStringParamList = new ArrayList<>();

        /*
         * Reusable variable
         */
        String key;
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            key = entry.getKey();
            if (key != null) {
                if (key.equals(context.getString(R.string.sto_qsp_track_brand))) {
                    //StoLog.i(TAG, "getParams: adding parameter: " + context.getString(R.string.sto_qsp_track_brand) + " " + entry.getValue().toString());
                    queryStringParamList.add(new QSPTrackBrand(context, entry.getValue().toString()));
                } else if (key.equals(context.getString(R.string.sto_qsp_track_operation))) {
                    //StoLog.i(TAG, "getParams: adding parameter: " + context.getString(R.string.sto_qsp_track_operation) + " " + entry.getValue().toString());
                    queryStringParamList.add(new QSPTrackOperation(context, entry.getValue().toString()));
                } else if (key.equals(context.getString(R.string.sto_qsp_track_creative))) {
                    //StoLog.i(TAG, "getParams: adding parameter: " + context.getString(R.string.sto_qsp_track_creative) + " " + entry.getValue().toString());
                    queryStringParamList.add(new QSPTrackCreative(context, entry.getValue().toString()));
                } else if (key.equals(context.getString(R.string.sto_qsp_track_insertion))) {
                    //StoLog.i(TAG, "getParams: adding parameter: " + context.getString(R.string.sto_qsp_track_insertion) + " " + entry.getValue().toString());
                    queryStringParamList.add(new QSPTrackInsertion(context, entry.getValue().toString()));
                } else if (key.equals(context.getString(R.string.sto_qsp_retail_placements))) {
                    //StoLog.i(TAG, "getParams: adding parameter: " + context.getString(R.string.sto_qsp_retail_placements) + " " +
                    //entry.getValue().toString());
                    queryStringParamList.add(new QSPRetailPlacements(context, entry.getValue().toString()));
                } else {
                    StoLog.w(TAG, "getParams: parameter is not handled!");
                }
            } else {
                StoLog.e(TAG, "getParams: key is null");
            }
        }
        return queryStringParamList;
    }
}
