package com.criteo.storetailsdk.view.constructions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.criteo.storetailsdk.tracker.StoTracker;
import com.criteo.storetailsdk.view.StoFormatOptionType;
import com.criteo.storetailsdk.view.adapters.StoAdapterListener;
import com.criteo.storetailsdk.view.models.StoVignette;
import com.criteo.storetailsdk.view.viewholders.BannerViewHolderInterface;

public class StoVignetteViewCreator {

    private String  TAG = "StoVignetteViewCreator";
    private Context context;

    /** Constructeur privé */
    private StoVignetteViewCreator(Context context) {
        this.context = context;
    }

    /** Instance unique non préinitialisée */
    private static StoVignetteViewCreator INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static StoVignetteViewCreator getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StoVignetteViewCreator(context);
        }
        return INSTANCE;
    }

    public void buildView(BannerViewHolderInterface viewHolder, final StoVignette format, final StoAdapterListener listener, final Drawable drawable, boolean hasMultipleRow, int nativeCellHeight) {
        Log.e(TAG, "ImageManager buildView");
        if (drawable != null) {
            Log.e(TAG, "ImageManager drawable not null");
            if (!hasMultipleRow) {
                double ratio = (double) drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                int height = (int) (viewHolder.getParent().getWidth() * ratio);
                if (height != 0) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    layoutParams.setMargins(0, 0, 0, 0);
                    viewHolder.getParent().setPadding(0, 0, 0, 0);
                    viewHolder.getParent().setLayoutParams(layoutParams);
                    viewHolder.getBackground().setLayoutParams(layoutParams);
                    viewHolder.getParent().requestLayout();
                    viewHolder.getBackground().requestLayout();
                }
            } else {
                RelativeLayout.LayoutParams parentLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                parentLayoutParams.setMargins(0, 0, 0, 0);

                viewHolder.getParent().setPadding(0, 0, 0, 0);
                viewHolder.getParent().setLayoutParams(parentLayoutParams);
                viewHolder.getParent().requestLayout();

                if (nativeCellHeight != -1) {
                    RelativeLayout.LayoutParams bgLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, nativeCellHeight);

                    viewHolder.getBackground().setLayoutParams(bgLayoutParams);
                    viewHolder.getBackground().setScaleType(ImageView.ScaleType.MATRIX);
                    viewHolder.getBackground().requestLayout();
                }
            }
        }else {
            Log.e(TAG, "ImageManager drawable == null");
        }

        viewHolder.getParent().setBackgroundColor(format.getBackgroundColor());
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
    }
}
