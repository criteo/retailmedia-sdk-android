package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 04/04/2017.
 */

public class QSPTrackAction extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPTrackAction(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_track_action);
        this.value = value;
    }

    public QSPTrackAction(Context context) {
        this.name = context.getString(R.string.sto_qsp_track_action);
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
