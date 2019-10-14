package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by mikhailpogorelov on 27/01/2018.
 */

public class QSPPositionObject extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPPositionObject(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_position_object);
        this.value = value;
    }

    public QSPPositionObject(Context context) {
        this.name = context.getString(R.string.sto_qsp_position_object);
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
