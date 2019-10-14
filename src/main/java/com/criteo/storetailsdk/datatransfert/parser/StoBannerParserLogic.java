package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.view.models.StoBanner;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.google.gson.Gson;

import org.json.JSONObject;

class StoBannerParserLogic extends StoFormatParserLogic {
    StoBannerParserLogic(Context context, Gson gson) {
        super(context, gson);
    }

    @Override
    String getTag() {
        return "StoBannerParserLogic";
    }

    @Override
    int getStoFormatKeyResourceId() {
        return R.string.sto_response_parser_ba;
    }

    @Override
    StoFormat getStoFormat(JSONObject values) {
        return gson.fromJson(values.toString(), StoBanner.class);
    }
}
