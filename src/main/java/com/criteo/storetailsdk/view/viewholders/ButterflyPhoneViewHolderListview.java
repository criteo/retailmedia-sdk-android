package com.criteo.storetailsdk.view.viewholders;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.criteo.storetailsdk.view.autofittext.StoAutoFitButton;
import com.criteo.storetailsdk.view.views.StoProductButton;

public class ButterflyPhoneViewHolderListview implements ButterflyViewHolderInterface {
    public LinearLayout        parent;
    public RelativeLayout      background;
    public ImageView           background_img;
    public ImageView           logo;
    public StoProductButton    button1;
    public StoProductButton    button2;
    public StoProductButton    button3;
    public StoProductButton    button4;
    public ViewGroup           leftContainer;
    public ViewGroup           rightContainer;
    public View                leftSeparator;
    public View                rightSeparator;

    @Override
    public LinearLayout getParent() {
        return parent;
    }

    @Override
    public RelativeLayout getBackground() {
        return background;
    }

    @Override
    public ImageView getBackgroundImage() {
        return background_img;
    }

    @Override
    public ImageView getLogo() {
        return logo;
    }

    @Override
    public StoProductButton getButton1() {
        return button1;
    }

    @Override
    public StoProductButton getButton2() {
        return button2;
    }

    @Override
    public StoProductButton getButton3() {
        return button3;
    }

    @Override
    public StoProductButton getButton4() {
        return button4;
    }

    @Override
    public StoProductButton getButton5() {
        return null;
    }

    @Override
    public ViewGroup getLeftContainer() {
        return leftContainer;
    }

    @Override
    public ViewGroup getRightContainer() {
        return rightContainer;
    }

    @Override
    public View getLeftSeparator() {
        return leftSeparator;
    }

    @Override
    public View getRightSeparator() {
        return rightSeparator;
    }

    @Override
    public LinearLayout getOptionLayout() {
        return null;
    }

    @Override
    public TextView getOptionText() {
        return null;
    }

    @Override
    public StoAutoFitButton getOptionButton() {
        return null;
    }
}
