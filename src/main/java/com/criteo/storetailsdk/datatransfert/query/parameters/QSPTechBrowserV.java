package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by mikhailpogorelov on 29/01/2018.
 */

public class QSPTechBrowserV extends StoQueryStringParam {

    private String name;
    private String value;


    public QSPTechBrowserV(Context context) {
        this.name = context.getString(R.string.sto_qsp_techbrowserv);
    }

    public QSPTechBrowserV(Context context, String value) {
        this.value = context.getString(R.string.sto_qsp_techbrowserv);
        this.value = value;
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
