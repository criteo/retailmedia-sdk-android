package com.criteo.storetailsdk.view.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageScaleView extends ImageView {

    private int backupScaleType = -1;

    public ImageScaleView(Context context) {
        this(context, null);
    }

    public ImageScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        if (drawable != null && backupScaleType == -1)
            backupScaleType = getScaleType().ordinal();
    }

    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {

        Drawable drawable = getDrawable();
        if (drawable != null) {
            float frameWidth = frameRight - frameLeft;
            float frameHeight = frameBottom - frameTop;

            float originalImageWidth = (float) getDrawable().getIntrinsicWidth();
            float originalImageHeight = (float) getDrawable().getIntrinsicHeight();

            if ((frameWidth > originalImageWidth) || (frameHeight > originalImageHeight)) {
                setAdjustViewBounds(false);
            } else {
                setAdjustViewBounds(true);
            }

            if (backupScaleType != -1 && ScaleType.values()[backupScaleType] != getScaleType()) {
                setScaleType(ScaleType.values()[backupScaleType]);
            }

            float fitHorizontallyScaleFactor = originalImageWidth/ frameWidth;
            float fitVerticallyScaleFactor = originalImageHeight / frameHeight;

            float usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor);

            float newImageWidth = originalImageWidth * usedScaleFactor;

            Matrix matrix = getImageMatrix();
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0);
            matrix.postTranslate((frameWidth - newImageWidth) / 2, 0);
            setImageMatrix(matrix);
        }
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}