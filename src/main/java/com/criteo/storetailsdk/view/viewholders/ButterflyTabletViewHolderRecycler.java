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

public class ButterflyTabletViewHolderRecycler extends ButterflyViewHolderRecyclerAbstract {

    private LinearLayout        parent;
    private RelativeLayout      background;
    private ImageView           background_img;
    private StoProductButton    button1;
    private StoProductButton    button2;
    private StoProductButton    button3;
    private StoProductButton    button4;
    private StoProductButton    button5;
    private LinearLayout        option;
    private TextView            option_text;
    private StoAutoFitButton    option_button;

    public ButterflyTabletViewHolderRecycler(@NonNull View itemView) {
        super(itemView);

        parent = itemView.findViewById(R.id.sto_butterfly_parent);
        background = itemView.findViewById(R.id.sto_butterfly_bg);
        background_img = itemView.findViewById(R.id.sto_butterfly_bg_img);
        button1 = itemView.findViewById(R.id.sto_butterfly_btn1);
        button2 = itemView.findViewById(R.id.sto_butterfly_btn2);
        button3 = itemView.findViewById(R.id.sto_butterfly_btn3);
        button4 = itemView.findViewById(R.id.sto_butterfly_btn4);
        button5 = itemView.findViewById(R.id.sto_butterfly_btn5);
        option = itemView.findViewById(R.id.sto_butterfly_option);
        option_text = itemView.findViewById(R.id.sto_butterfly_option_text);
        option_button = itemView.findViewById(R.id.sto_butterfly_option_button);
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