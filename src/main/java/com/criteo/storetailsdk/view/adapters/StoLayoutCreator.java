package com.criteo.storetailsdk.view.adapters;

import android.view.View;

import com.criteo.storetailsdk.view.models.StoFormat;

public interface StoLayoutCreator {

    View createLayout(final StoAdapterTrackEvents listener, final StoFormat format, String productId);
}
