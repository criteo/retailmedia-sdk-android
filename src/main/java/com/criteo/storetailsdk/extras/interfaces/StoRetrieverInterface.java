package com.criteo.storetailsdk.extras.interfaces;

import com.criteo.storetailsdk.extras.models.StoProduct;

import java.util.List;

/**
 * Created by MikhailPOGORELOV on 11/05/2017.
 * <p>
 * Interface defines main methods for communication with the retailer Application
 */

public interface StoRetrieverInterface {

    /**
     * Returns a product by id
     *
     * @param product_id: the id of the product you want to retrieve
     * @return :
     */
    StoProduct stoGetStoProductById(final String product_id);

    /**
     * Returns the list of product by ids
     *
     * @param ids
     * @return
     */
    List<StoProduct> stoGetStoProductsByIds(final List<String> ids);

    /**
     * Returns the list of product of the basket
     *
     * @return {List<StoProduct>}
     */
    List<StoProduct> stoGetBasketProducts();
}