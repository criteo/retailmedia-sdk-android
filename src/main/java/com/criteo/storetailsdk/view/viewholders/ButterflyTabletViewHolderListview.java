package com.criteo.storetailsdk.view.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.criteo.storetailsdk.view.autofittext.StoAutoFitButton;
import com.criteo.storetailsdk.view.views.StoProductButton;

public class ButterflyTabletViewHolderListview implements ButterflyViewHolderInterface {
    public LinearLayout        parent;
    public RelativeLayout      background;
    public ImageView           background_img;
    public StoProductButton    button1;
    public StoProductButton    button2;
    public StoProductButton    button3;
    public StoProductButton    button4;
    public StoProductButton    button5;
    public LinearLayout        option;
    public TextView            option_text;
    public StoAutoFitButton    option_button;

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
        return null;
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
        return button5;
    }

    @Override
    public ViewGroup getLeftContainer() {
        return null;
    }

    @Override
    public ViewGroup getRightContainer() {
        return null;
    }

    @Override
    public View getLeftSeparator() {
        return null;
    }

    @Override
    public View getRightSeparator() {
        return null;
    }

    @Override
    public LinearLayout getOptionLayout() {
        return option;
    }

    @Override
    public TextView getOptionText() {
        return option_text;
    }

    @Override
    public StoAutoFitButton getOptionButton() {
        return option_button;
    }
}
