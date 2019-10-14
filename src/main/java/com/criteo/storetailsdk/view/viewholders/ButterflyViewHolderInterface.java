package com.criteo.storetailsdk.view.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.criteo.storetailsdk.view.autofittext.StoAutoFitButton;
import com.criteo.storetailsdk.view.views.StoProductButton;

public interface ButterflyViewHolderInterface {
    LinearLayout        getParent();
    RelativeLayout      getBackground();
    ImageView           getBackgroundImage();
    ImageView           getLogo();
    StoProductButton    getButton1();
    StoProductButton    getButton2();
    StoProductButton    getButton3();
    StoProductButton    getButton4();
    StoProductButton    getButton5();
    ViewGroup           getLeftContainer();
    ViewGroup           getRightContainer();
    View                getLeftSeparator();
    View                getRightSeparator();
    LinearLayout        getOptionLayout();
    TextView            getOptionText();
    StoAutoFitButton    getOptionButton();
}
