package com.criteo.storetailsdk.view.views;

import android.content.Context;
import android.util.AttributeSet;

import com.criteo.storetailsdk.view.autofittext.StoAutoFitButton;

public class StoProductButton extends StoAutoFitButton {

    private final String TAG = "StoProductButton";


    /**
     * The id of the associated product
     */
    private String productId;


    public StoProductButton(Context context) {
        super(context);
    }

    public StoProductButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


}