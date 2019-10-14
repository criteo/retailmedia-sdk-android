package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 05/04/2017.
 */

public class QSPRetailSearch extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPRetailSearch(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_retail_search);
        this.value = value;
    }

    public QSPRetailSearch(Context context) {
        this.name = context.getString(R.string.sto_qsp_retail_search);
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