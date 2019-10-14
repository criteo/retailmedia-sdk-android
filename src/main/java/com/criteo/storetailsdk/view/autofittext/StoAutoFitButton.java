package com.criteo.storetailsdk.view.autofittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import com.criteo.storetailsdk.R;

/**
 * @attr ref R.styleable.AutofitTextView_sizeToFit
 * @attr ref R.styleable.AutofitTextView_minTextSize
 * @attr ref R.styleable.AutofitTextView_precision
 */

/**
 * Class extends the button class.
 */
public class StoAutoFitButton extends AppCompatButton {

    private CharSequence text = "";
    private float textSize, minTextSize;

    public StoAutoFitButton(Context context) {
        super(context);
        init();
    }

    public StoAutoFitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StoAutoFitButton,
                0, 0);

        try {
            minTextSize = a.getDimensionPixelSize(R.styleable.StoAutoFitButton_minTextSize, -1);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        CharSequence cs = this.getText();
        if (cs != null) {
            this.text = this.getText();
            if (this.getTransformationMethod() != null) {
                this.text = this.getTransformationMethod().getTransformation(getText(), this);
            }
        }
        this.textSize = this.getTextSize();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.text = text;
        if (lengthBefore != lengthAfter) {
            setBestTextSize(getInnerWidth());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int innerWidth = getInnerWidth();
        setBestTextSize(innerWidth);
    }

    private int getInnerWidth() {
        //Compute the available inner space for text
        //consider: - padding
        //          - drawables
        Drawable[] drawable = getCompoundDrawables();//left, top, right, and bottom
        Drawable leftDrawable = drawable[0];
        Drawable rightDrawable = drawable[2];

        int ldw = 0, rdw = 0;
        if (leftDrawable != null) {
            ldw = leftDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        if (rightDrawable != null) {
            rdw = rightDrawable.getMinimumWidth() + this.getCompoundDrawablePadding();
        }

        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - ldw - rdw;
    }

    private void setBestTextSize(int innerWidth) {
        if (innerWidth <= 0) return;

        this.textSize = this.getTextSize();

        TextPaint paint = this.getPaint();
        StaticLayout sl;
        boolean found = false;
        while (!found && this.textSize > 0 && this.textSize >= this.minTextSize) {
            paint.setTextSize(this.textSize);
            sl = new StaticLayout(this.text, paint, innerWidth, Layout.Alignment.ALIGN_CENTER, 0, 0, true);
            if (sl.getLineCount() <= 1) {
                found = true;
            } else {
                this.textSize -= 1f;
            }
        }

        this.setTextSize(((this.textSize - 1f) / getResources().getDisplayMetrics().density));
    }

    public float getMinTextSize() {
        return minTextSize;
    }
}