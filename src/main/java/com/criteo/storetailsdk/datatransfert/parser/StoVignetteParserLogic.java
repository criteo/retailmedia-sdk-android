package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.criteo.storetailsdk.view.models.StoVignette;
import com.google.gson.Gson;

import org.json.JSONObject;

class StoVignetteParserLogic extends StoFormatParserLogic {
    StoVignetteParserLogic(Context context, Gson gson) {
        super(context, gson);
    }

    @Override
    String getTag() {
        return "StoVignetteParserLogic";
    }

    @Override
    int getStoFormatKeyResourceId() {
        return R.string.sto_response_parser_vi;
    }

    @Override
    StoFormat getStoFormat(JSONObject values) {
        return gson.fromJson(values.toString(), StoVignette.class);
    }
}
