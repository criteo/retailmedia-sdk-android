package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by mikhailpogorelov on 02/11/2017.
 */

public class QSPAvailableProducts extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPAvailableProducts(Context context) {
        this.name = context.getString(R.string.sto_qsp_available_products);
    }

    public QSPAvailableProducts(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_available_products);
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