package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by mikhailpogorelov on 05/02/2018.
 */

public class QSPProductID extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPProductID(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_product_id);
        this.value = value;
    }

    public QSPProductID(Context context) {
        this.name = context.getString(R.string.sto_qsp_product_id);
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
