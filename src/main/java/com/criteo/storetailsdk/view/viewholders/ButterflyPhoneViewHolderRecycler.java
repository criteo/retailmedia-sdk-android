package com.criteo.storetailsdk.view.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.view.autofittext.StoAutoFitButton;
import com.criteo.storetailsdk.view.views.StoProductButton;

public class ButterflyPhoneViewHolderRecycler extends ButterflyViewHolderRecyclerAbstract {
    private LinearLayout        parent;
    private RelativeLayout      background;
    private ImageView           background_img;
    private ImageView           logo;
    private StoProductButton    button1;
    private StoProductButton    button2;
    private StoProductButton    button3;
    private StoProductButton    button4;
    private ViewGroup           leftContainer;
    private ViewGroup           rightContainer;
    private View                leftSeparator;
    private View                rightSeparator;

    public ButterflyPhoneViewHolderRecycler(@NonNull View itemView) {
        super(itemView);

        parent = itemView.findViewById(R.id.sto_butterfly_parent);
        background = itemView.findViewById(R.id.sto_butterfly_bg);
        background_img = itemView.findViewById(R.id.sto_butterfly_bg_img);
        logo = itemView.findViewById(R.id.sto_butterfly_logo);
        leftContainer = itemView.findViewById(R.id.sto_butterfly_left_container);
        rightContainer = itemView.findViewById(R.id.sto_butterfly_right_container);
        leftSeparator = itemView.findViewById(R.id.sto_butterfly_left_separator);
        rightSeparator = itemView.findViewById(R.id.sto_butterfly_right_separator);
        button1 = itemView.findViewById(R.id.sto_butterfly_btn1);
        button2 = itemView.findViewById(R.id.sto_butterfly_btn2);
        button3 = itemView.findViewById(R.id.sto_butterfly_btn3);
        button4 = itemView.findViewById(R.id.sto_butterfly_btn4);
    }

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
