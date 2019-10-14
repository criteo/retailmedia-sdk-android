package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 01/06/2017.
 */

public class QSPPageType extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPPageType(Context context) {
        this.name = context.getString(R.string.sto_qsp_page_type);
    }

    public QSPPageType(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_page_type);
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
