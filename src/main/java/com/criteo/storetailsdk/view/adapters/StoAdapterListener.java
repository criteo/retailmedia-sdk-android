package com.criteo.storetailsdk.view.adapters;

public interface StoAdapterListener {

    // Redirect action
    void stoOpenDeeplink(String deeplink);

    // Open video action
    void stoOpenVideo(String videoURL);

    // Open PDF file action
    void stoOpenPDF(String pdfURL);
}
