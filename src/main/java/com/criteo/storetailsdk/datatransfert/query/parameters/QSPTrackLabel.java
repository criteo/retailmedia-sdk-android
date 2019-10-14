package com.criteo.storetailsdk.datatransfert.query.parameters;

import android.content.Context;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;

/**
 * Created by MikhailPOGORELOV on 04/04/2017.
 * <p>
 * <p>
 * The Class has a property "previousValue" used with "nav" TrackAction"
 */


public class QSPTrackLabel extends StoQueryStringParam {

    private String name;
    private String value;

    public QSPTrackLabel(Context context, String value) {
        this.name = context.getString(R.string.sto_qsp_track_label);
        this.value = value;
    }

    public QSPTrackLabel(Context context) {
        this.name = context.getString(R.string.sto_qsp_track_label);
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
