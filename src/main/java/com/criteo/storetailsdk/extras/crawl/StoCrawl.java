package com.criteo.storetailsdk.extras.crawl;

import com.criteo.storetailsdk.extras.StoAppCommunicator;
import com.criteo.storetailsdk.extras.models.StoProduct;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.storage.StoProductContainer;

import java.util.List;

public class StoCrawl {

    public static final String TAG = "StoCrawl";

    private StoProductContainer productContainer;

    private static StoCrawl instance = null;


    private StoCrawl() {
        this.productContainer = new StoProductContainer();
    }

    public static StoCrawl getInstance() {
        synchronized ((StoCrawl.class)) {
            if (instance == null) {
                StoLog.d(TAG, "Instance is null");
                instance = new StoCrawl();
            }
        }

        return instance;
    }

    public void setProducts(List<StoProduct> stoProducts) {
        this.productContainer.setProducts(stoProducts);
    }

    public boolean contains(String productID) {
        return this.productContainer.contains(productID);
    }

    public StoProduct getFormatProductById(String productID) {
        return this.productContainer.getProductById(productID);
    }

    public StoProduct getProductById(String productID){
        return StoAppCommunicator.getAppRetriever().stoGetStoProductById(productID);
    }

    /**
     * Calls the App to retrieve the list of products with the given ids' list
     *
     * @param ids: list of product to retrieve
     * @return
     */
    public List<StoProduct> requestProducts(List<String> ids) {
        List<StoProduct> stoProducts = StoAppCommunicator.getAppRetriever().stoGetStoProductsByIds(ids);
        this.productContainer.getStoProductList().addAll(stoProducts);
        return this.productContainer.getStoProductList();
    }

    public void clear() {
        this.productContainer.clear();
    }
}
