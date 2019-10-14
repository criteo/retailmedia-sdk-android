package com.criteo.storetailsdk.view.adapters;

import com.criteo.storetailsdk.view.models.StoFormat;

public interface StoAdapterTrackEvents {

    void openProductPage(StoFormat format);

    void addToWishList(StoFormat format);

    void addToBasket(StoFormat format);

    void addToBasketMore(StoFormat format);

    void addToBasketLess(StoFormat format);

    void basketQuantityChange(StoFormat format);
}
