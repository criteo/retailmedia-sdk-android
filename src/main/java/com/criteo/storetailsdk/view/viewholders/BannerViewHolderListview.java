package com.criteo.storetailsdk.view.viewholders;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BannerViewHolderListview implements BannerViewHolderInterface {
    public RelativeLayout             parent;
    public ImageView                background;

    @Override
    public RelativeLayout getParent() {
        return parent;
    }

    @Override
    public ImageView getBackground() {
        return background;
    }
}
