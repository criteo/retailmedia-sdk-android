package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 25/04/2017.
 */

public class QSPVerifUID extends StoQueryStringParam {

    private final String name;
    private String value;

    public QSPVerifUID(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_verif_uid);
        this.value = value;
    }

    public QSPVerifUID(Context context) {
        this.name = context.getString(R.string.sto_qsp_verif_uid);
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