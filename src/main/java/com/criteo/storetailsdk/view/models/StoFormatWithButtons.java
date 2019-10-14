package com.criteo.storetailsdk.view.models;

import android.widget.Button;

import com.criteo.storetailsdk.StoButtonContentComparator;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class StoFormatWithButtons extends StoFormat {

    @Expose(serialize = false, deserialize = false)
    private transient String productIdSelected;
    /**
     * List of {@link StoButtonContent} contains information for each button in the view
     */
    @Expose(serialize = false, deserialize = false)
    private final transient List<StoButtonContent> buttonContentList;

    StoFormatWithButtons() {
        super();

        this.buttonContentList = new ArrayList<>();
        this.productIdSelected = null;
    }

    public abstract List<String> getProducts();
    public abstract void setOptionButtonColor(Button optionButton);

    public String getProductIdSelected() {
        if (productIdSelected == null && buttonContentList.size() > 0) {
            productIdSelected = buttonContentList.get(0).getProductId();
        }
        return productIdSelected;
    }
    public void setProductIdSelected(String productId) { productIdSelected = productId; }

    public void addButtonContent(StoButtonContent buttonContent) {
        this.buttonContentList.add(buttonContent);
    }

    public List<StoButtonContent> getButtonContentList() {
        return buttonContentList;
    }
    public void setButtonContentList(List<StoButtonContent> contentList) {
        this.buttonContentList.clear();
        this.buttonContentList.addAll(contentList);
        /*
         * Sort buttons by position
         */
        Collections.sort(this.buttonContentList, new StoButtonContentComparator());
    }
}
