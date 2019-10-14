package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 01/06/2017.
 */

public class QSPProductLabel extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPProductLabel(Context context) {
        this.name = context.getString(R.string.sto_qsp_product_label);
    }

    public QSPProductLabel(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_product_label);
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
