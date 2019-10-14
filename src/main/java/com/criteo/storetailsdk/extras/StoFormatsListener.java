package com.criteo.storetailsdk.extras;

import com.criteo.storetailsdk.view.models.StoFormat;

import java.util.List;

public interface StoFormatsListener {

    void onStoFormatsReceived(List<StoFormat> formatList, String identifier);

    void onStoFailure(String message, String identifier);
}
