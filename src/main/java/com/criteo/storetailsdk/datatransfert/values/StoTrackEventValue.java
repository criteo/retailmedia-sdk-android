package com.criteo.storetailsdk.datatransfert.values;

/**
 * Enumeration of all possible values for trackEvent
 */
public enum StoTrackEventValue {

    BrowseProduct,  // Called on changing of products inside a format (browsing products)

    OpenPdp,        // Action of opening a product page

    AddToList,      // Action of adding a product to the list

    AbkBtn,         // Action of adding a product to the basket
    QuantityMore,   // Action of adding more product to the basket (click on +)
    QuantityLess,   // Action of removing product to the basket (click on -)
    QuantityChange, // Action of changing number of product to the basket (case of input)

    cta,            // Action of clicking on a button or on a link which redirects you to another page

    OpenPDF,        // Action of clicking on a button which open a PDF
    OpenVideo,      // Action of clicking on a button which open a Video
    CloseVideo,     // Action of clicking on a button which close a Video


    ConfirmDelete,
    CancelDelete,

    onBuild,
    onLoad,
    onReceive,
    onDeliver,

}
