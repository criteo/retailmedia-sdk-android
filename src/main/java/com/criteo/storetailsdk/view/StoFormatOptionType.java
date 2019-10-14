package com.criteo.storetailsdk.view;

import com.google.gson.annotations.SerializedName;

public enum StoFormatOptionType {
    @SerializedName("no option")
    NONE,
    @SerializedName("redirection")
    REDIRECTION,
    @SerializedName("video")
    VIDEO,
    @SerializedName("pdf")
    PDF,
    @SerializedName("legal")
    LEGAL
}