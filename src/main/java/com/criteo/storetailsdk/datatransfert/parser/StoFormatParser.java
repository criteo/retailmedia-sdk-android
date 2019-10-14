package com.criteo.storetailsdk.datatransfert.parser;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.view.models.StoFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

class StoFormatParser {
    private StoFormatParserLogic parser;
    private JSONObject jsonObject;

    StoFormatParser(Context context, JSONObject jsonObject) throws JSONException {

        this.jsonObject = jsonObject.getJSONObject(context.getString(R.string.sto_response_parser_creatives)).
                getJSONObject(context.getString(R.string.sto_response_parser_placements));
    }

    List<StoFormat> parse() {
        return parser.parse(this.jsonObject);
    }

    List<StoFormat> parseCustom() {
        return parser.parseCustom(this.jsonObject);
    }

    void setParser(StoFormatParserLogic parser) {
        this.parser = parser;
    }
}
