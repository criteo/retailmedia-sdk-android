package com.criteo.storetailsdk.view.models;

import java.util.List;

/**
 * Created by MikhailPOGORELOV on 04/05/2017.
 * <p>
 * Class represents the content that must be shown in the button of the StoView
 * It contains a list of possible products for a button in the view
 * If no products are available, checks the variable isMandatory, if the variable is set on true,
 * don't show the StoView
 */

public class StoButtonContent {

    /**
     * Position of the button in the Sto view
     */
    private int position;

    /**
     * Name of the button
     */
    private String buttonName;

    /**
     * If the StoButtonContent is exclusive then, if no products from the {@link StoButtonContent} is
     * available, don't show the view
     */
    private boolean isMandatory;

    /**
     * List of product that can be shown is the Button
     */
    private List<String> productIds;

    /**
     * Id of the product with the highest priority
     */
    private String productId;

    public StoButtonContent() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public StoButtonContent(int position) {
        this.position = position;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}