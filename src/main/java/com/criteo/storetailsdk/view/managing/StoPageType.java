package com.criteo.storetailsdk.view.managing;

/**
 * Created by mikhailpogorelov on 09/10/2017.
 * <p>
 * Types of pages
 */
public enum StoPageType {

    StoHomePage("home"),
    StoCategoryPage("shelve"),
    StoProductsListPage("shelve"),
    StoSearchPage("search"),
    StoProductPage("productpage"),
    StoCheckoutPage("checkout"),
    None("none");

    private final String fieldDescription;

    StoPageType(String value) {
        fieldDescription = value;
    }

    public String getDescription() {
        return fieldDescription;
    }
}