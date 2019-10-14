package com.criteo.storetailsdk.view.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.criteo.storetailsdk.R;

public class BannerViewHolderRecycler extends RecyclerView.ViewHolder implements BannerViewHolderInterface {
    private RelativeLayout      parent;
    private ImageView           background;

    public BannerViewHolderRecycler(@NonNull View itemView) {
        super(itemView);

        parent = itemView.findViewById(R.id.sto_banner_parent);
        background = itemView.findViewById(R.id.sto_banner_background);
    }

    @Override
    public RelativeLayout getParent() {
        return parent;
    }

    @Override
    public ImageView getBackground() {
        return background;
    }
}
