package com.criteo.storetailsdk.view.constructions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.tracker.StoTracker;
import com.criteo.storetailsdk.view.StoFormatOptionType;
import com.criteo.storetailsdk.view.StoViewUtils;
import com.criteo.storetailsdk.view.adapters.StoAdapterListener;
import com.criteo.storetailsdk.view.models.StoBanner;
import com.criteo.storetailsdk.view.models.StoButterfly;
import com.criteo.storetailsdk.view.viewholders.BannerViewHolderInterface;

public class StoBannerViewCreator {

    private String  TAG = "StoBannerViewCreator";
    private Context context;

    /** Constructeur privé */
    private StoBannerViewCreator(Context context) {
        this.context = context;
    }

    /** Instance unique non préinitialisée */
    private static StoBannerViewCreator INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static StoBannerViewCreator getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StoBannerViewCreator(context);
        }
        return INSTANCE;
    }

    public void buildBannerView(BannerViewHolderInterface viewHolder, int totalWidth, final StoBanner format, final StoAdapterListener listener, final Drawable drawable) {
        Log.e(TAG, "ImageManager buildView");
        if (drawable != null) {
            float widthDP = StoViewUtils.convertPixelsToDp(totalWidth, context);
            float imageWidthDP = StoViewUtils.convertPixelsToDp(drawable.getIntrinsicWidth(), context);
            if (imageWidthDP < widthDP) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, drawable.getIntrinsicHeight());
                layoutParams.setMargins(0, 0, 0, 0);
                viewHolder.getParent().setPadding(0, 0, 0, 0);
                viewHolder.getParent().setLayoutParams(layoutParams);
                viewHolder.getBackground().setLayoutParams(layoutParams);
                viewHolder.getBackground().setBackgroundColor(format.getBackgroundColor());
            } else {
                double ratio = (double) drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                int height = (int) (totalWidth * ratio);
                if (height != 0) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    layoutParams.setMargins(0, 0, 0, 0);
                    viewHolder.getParent().setPadding(0, 0, 0, 0);
                    viewHolder.getParent().setLayoutParams(layoutParams);
                    viewHolder.getBackground().setLayoutParams(layoutParams);
                }
            }
        } else {
            Log.e(TAG, "ImageManager drawable == null");
        }

        viewHolder.getBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoTracker.getInstance(context).openOption(format, StoFormatOptionType.REDIRECTION);
                if (listener != null) {
                    listener.stoOpenDeeplink(format.getDeeplink());
                }
            }
        });
        viewHolder.getBackground().setImageDrawable(drawable);

        //Glide.with(context).load(format.getBackgroundImageURL(context.getResources().getBoolean(R.bool.isTablet))).centerInside().into(viewHolder.getBackground());
    }
}
