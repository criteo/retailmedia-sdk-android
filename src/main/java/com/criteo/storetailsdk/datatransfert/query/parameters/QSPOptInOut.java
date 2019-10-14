package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

public class QSPOptInOut extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPOptInOut(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_optin_optout);
        this.value = value;
    }

    public QSPOptInOut(Context context) {
        this.name = context.getString(R.string.sto_qsp_optin_optout);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

}
