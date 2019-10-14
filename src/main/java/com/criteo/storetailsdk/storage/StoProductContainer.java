package com.criteo.storetailsdk.storage;

import com.criteo.storetailsdk.extras.models.StoProduct;
import com.criteo.storetailsdk.logs.StoLog;

import java.util.ArrayList;
import java.util.List;

public class StoProductContainer {

    private final String TAG = "StoProductContainer";


    private List<StoProduct> stoProductList;


    public StoProductContainer() {
        this.stoProductList = new ArrayList<>();
    }


    public void setProducts(List<StoProduct> stoProducts) {
        if (stoProducts != null) {
            this.stoProductList = stoProducts;
        } else {
            this.clear();
            StoLog.e(TAG, "setProducts: stoProducts list is null! The StoProductContainer's list will be cleared");
        }
    }

    public void addProduct(StoProduct stoProduct) {
        this.stoProductList.add(stoProduct);
    }

    public boolean contains(String productID) {
        for (StoProduct stoProduct : stoProductList) {
            if (stoProduct.getId().compareTo(productID) == 0) {
                return true;
            }
        }

        return false;
    }

    public StoProduct getProductById(String productID) {
        StoLog.d(TAG, "getFormatProductById: " + productID);
        for (StoProduct stoProduct : stoProductList) {
            //StoLog.d(TAG, "getFormatProductById: " + stoProduct.getId() + ", " + productID);
            if (stoProduct.getId().compareTo(productID) == 0) {
                StoLog.d(TAG, "getFormatProductById: condition verified!" + productID);
                return stoProduct;
            }
        }
        StoLog.d(TAG, "getFormatProductById: the product hasn't been found: " + productID);
        return null;
    }

    public List<StoProduct> getStoProductList() {
        return this.stoProductList;
    }


    public void clear() {
        this.stoProductList.clear();
    }
}
