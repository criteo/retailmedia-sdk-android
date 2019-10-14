package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.view.models.StoButterfly;
import com.criteo.storetailsdk.view.models.StoButtonContent;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class StoButterflyParserLogic extends StoFormatParserLogic {

    StoButterflyParserLogic(Context context, Gson gson) {
        super(context, gson);
    }

    @Override
    String getTag() {
        return "StoButterflyParserLogic";
    }

    @Override
    int getStoFormatKeyResourceId() {
        return R.string.sto_response_parser_bf;
    }

    @Override
    StoFormat getStoFormat(JSONObject values) {
        StoButterfly stoButterfly = gson.fromJson(values.toString(), StoButterfly.class);
        if (!this.setButterflyButtons(values, stoButterfly)) {
            StoLog.e(getTag(), "getStoButterfly: buttons haven't been set!");
            return null;
        }
        return stoButterfly;
    }

    /**
     * Sets buttons to stoButterfly
     *
     * @param jsonObject Response JSON object
     * @param stoButterfly newly created StoButterfly
     * @return true if everything good else false
     */
    private boolean setButterflyButtons(JSONObject jsonObject, StoButterfly stoButterfly) {
        StoLog.d(getTag(), "setButterflyButtons");

        try {
            /*
             * Getting of the object containing all the buttons of products
             */
            JSONArray productButtons = jsonObject.getJSONArray(context.getString(R.string.sto_response_parser_products));

            /*
             * If products exists
             */
            if (productButtons != null) {
                /*
                 * If at least one product exists
                 */
                if (productButtons.length() > 0) {

                    /*
                     * For each {@link JSONArray} in "products", creates a {@link StoButtonContent} object
                     */
                    for (int i = 0; i < productButtons.length(); i++) {
                        /*
                         * Gets the button content
                         */
                        StoButtonContent content = getButtonContent(productButtons.getJSONArray(i));
                        /*
                         * Verifies if the content is not null
                         */
                        if (content != null) {
                            StoLog.i(getTag(), "getStoButterfly: added an StoButtonContent: name " + content.getButtonName() + " @ position: " +
                                    content.getPosition());
                            stoButterfly.addButtonContent(content);
                        } else {
                            StoLog.e(getTag(), "getStoButterfly: content is null!");
                        }
                    }
                    return true;
                }
            }
            return false;
        } catch (JSONException je) {
            if (je.getMessage() != null) {
                StoLog.e(getTag(), "getStoButterfly: Exception: message: " + je.getMessage());
            } else {
                StoLog.e(getTag(), "getStoButterfly: Exception:" + je.toString());
            }
            return false;
        }
    }

    /**
     * Converts the {@link JSONArray} object to a {@link StoButtonContent} object
     *
     * @param jsonArray JSONArray representing products
     * @return StoButtonContent
     */
    private StoButtonContent getButtonContent(JSONArray jsonArray) {
        StoLog.i(getTag(), "getButtonContent:");

        if (jsonArray == null) {
            StoLog.e(getTag(), "getButtonContent: jsonArray is null!");
            return null;
        }

        StoButtonContent content;

        try {
            /*
             * Verifies if the jsonArray contains 4 elements
             */
            if (jsonArray.length() != 4) {
                StoLog.e(getTag(), "getButtonContent: jsonArray length is different than 4! No StoButtonContent will be returned");
                return null;
            }
            /*
             * Gets the information of the button
             */
            String position = jsonArray.getString(0);
            String buttonName = jsonArray.getString(1);
            boolean isExclusive = jsonArray.getBoolean(2);

            /*
             * Gets the list of products for the button
             */
            List<String> productList = new ArrayList<>();

            JSONArray products = jsonArray.getJSONArray(3);

            if (products != null) {
                if (products.length() != 0) {
                    for (int i = 0; i < products.length(); i++) {
                        StoLog.d(getTag(), "getButtonContent: " + products.get(i).toString() + " added");
                        productList.add(products.get(i).toString());
                    }
                } else {
                    StoLog.w(getTag(), "getButtonContent: products JSONArray is empty!");
                }
            } else {
                StoLog.w(getTag(), "getButtonContent: products JSONArray is null!");
            }


            /*
             * Creates the StoButtonContent
             */
            content = new StoButtonContent(Integer.parseInt(position));
            content.setButtonName(buttonName);
            content.setMandatory(isExclusive);
            content.setProductIds(productList);
            return content;
        } catch (JSONException je) {
            if (je.getMessage() != null) {
                StoLog.e(getTag(), "getButtonContent: JSONException: " + je.getMessage());
            } else {
                StoLog.i(getTag(), "getButtonContent: JSONException message is null");
            }
        }
        return null;
    }
}
