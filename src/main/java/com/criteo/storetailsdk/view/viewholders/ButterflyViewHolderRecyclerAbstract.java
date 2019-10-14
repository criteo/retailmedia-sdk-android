package com.criteo.storetailsdk.view.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ButterflyViewHolderRecyclerAbstract extends RecyclerView.ViewHolder implements ButterflyViewHolderInterface {

    public ButterflyViewHolderRecyclerAbstract(@NonNull View itemView) {
        super(itemView);
    }
}
