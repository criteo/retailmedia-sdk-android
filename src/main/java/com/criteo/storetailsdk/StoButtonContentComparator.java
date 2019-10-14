package com.criteo.storetailsdk;

import com.criteo.storetailsdk.view.models.StoButtonContent;

import java.util.Comparator;

public class StoButtonContentComparator implements Comparator<StoButtonContent> {
    @Override
    public int compare(StoButtonContent o1, StoButtonContent o2) {
        return o1.getPosition() - o2.getPosition();
    }
}