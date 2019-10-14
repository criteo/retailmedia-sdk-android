package com.criteo.storetailsdk.view.managing;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.view.StoFormatType;
import com.criteo.storetailsdk.view.adapters.StoImageManagerListener;
import com.criteo.storetailsdk.view.models.StoBanner;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.criteo.storetailsdk.view.models.StoVignette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoImageManager {
    private Context context;
    private Map<String, Drawable> images;
    private boolean isTablet;
    private int nbImagesDownloaded;
    private StoImageManagerListener listener;

    public StoImageManager(Context context, StoImageManagerListener listener) {
        this.context = context;
        this.images = new HashMap<>();
        this.isTablet = context.getResources().getBoolean(R.bool.isTablet);
        this.nbImagesDownloaded = 0;
        this.listener = listener;
    }

    public void clear() {
        images.clear();
        nbImagesDownloaded = 0;
    }

    public Drawable getImage(String url) {
        return images.containsKey(url) ? images.get(url) : null;
    }

    public synchronized void downloadImagesBeforeReload(List<StoFormat> stoFormats, boolean isRecyclerView) {

        images.clear();
        nbImagesDownloaded = 0;

        int nbImagesToBeDownload = 0;

        for (StoFormat format: stoFormats) {
            if (format.getFormatType() == StoFormatType.stoBanner ||
                    format.getFormatType() == StoFormatType.stoVignette) {
                nbImagesToBeDownload++;
            }
        }
        final int finalNbImagesToBeDownload = nbImagesToBeDownload;

        Log.e("ImageManager", "nbImagesToBeDownload == " + nbImagesToBeDownload);
        if (finalNbImagesToBeDownload == 0) {
            listener.reloadView();
        } else {
            for (StoFormat format: stoFormats) {
                if (format.getFormatType() == StoFormatType.stoBanner) {
                    StoBanner stoBanner = (StoBanner) format;
                    final String path = stoBanner.getBackgroundImageURL(isTablet);
                    Glide.with(this.context)
                            .asDrawable()
                            .load(path)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    nbImagesDownloaded++;
                                    Log.e("ImageManager", "onResourceReady nbImagesDownloaded = " + nbImagesDownloaded + " w " + resource.getIntrinsicWidth() + " h " + resource.getIntrinsicHeight());
                                    StoImageManager.this.images.put(path, resource);

                                    if (finalNbImagesToBeDownload == nbImagesDownloaded) {
                                        listener.reloadView();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                } else if (format.getFormatType() == StoFormatType.stoVignette) {
                    StoVignette stoVignette = (StoVignette) format;
                    final String path = stoVignette.getBackgroundImageURL(isRecyclerView);
                    Log.e("ImageManager", "Download -> " + path);
                    Glide.with(this.context)
                            .asDrawable()
                            .load(path)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    nbImagesDownloaded++;
                                    Log.e("ImageManager", "onResourceReady nbImagesDownloaded = " + nbImagesDownloaded + " w " + resource.getIntrinsicWidth() + " h " + resource.getIntrinsicHeight());
                                    StoImageManager.this.images.put(path, resource);

                                    if (finalNbImagesToBeDownload == nbImagesDownloaded) {
                                        listener.reloadView();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            }
        }
    }
}
