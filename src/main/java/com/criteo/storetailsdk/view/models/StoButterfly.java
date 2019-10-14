package com.criteo.storetailsdk.view.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;
import com.criteo.storetailsdk.view.StoFormatOptionType;
import com.criteo.storetailsdk.view.StoFormatType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StoButterfly extends StoFormatWithButtons {
    private transient String TAG = "StoButterfly";

    @Expose()
    private String path;

    @Expose()
    private String bg_img_mobile;

    @Expose()
    private String logo_img_mobile;

    @Expose()
    private String bg_img_tablet;

    @Expose()
    private String border_color;

    @Expose()
    private String bg_color;

    @Expose()
    private String button_color;

    @Expose()
    private String button_color_selected;

    @Expose()
    private String button_txt_color;

    @Expose()
    private String button_txt_color_selected;

    @Expose()
    private StoFormatOptionType option;

    @Expose()
    private String option_file;

    @Expose()
    private String option_text;

    @Expose()
    private String option_video_link;

    @Expose()
    private String option_redirection_url;

    @Expose()
    private String option_txt_color;

    @Expose()
    private String option_button_color;

    @Expose()
    private String option_button_img;

    @Expose()
    private Boolean option_multi_bg;

    @Expose()
    private String multi_bg_img_01;

    @Expose()
    private String multi_bg_img_02;

    @Expose()
    private String multi_bg_img_03;

    @Expose()
    private String multi_bg_img_04;

    @Expose()
    private String multi_bg_img_05;

    @Expose(serialize = false, deserialize = false)
    private final String DF_LOGO_IMG_URL_PROTOCOL = "https:";

    public StoButterfly() {
        super();
    }

    @Override
    public StoFormatType getFormatType() {
        return StoFormatType.stoButterfly;
    }

    @Expose(serialize = false, deserialize = false)
    private String buildedBGImageURL;

    public String getBackgroundImageURL(boolean isTablet) {
        StoLog.d(TAG, "getBackgroundImageUrl");

        if (buildedBGImageURL == null) {
            if (isTablet) {
                if (bg_img_tablet != null && !bg_img_tablet.isEmpty())
                    buildedBGImageURL = this.DF_LOGO_IMG_URL_PROTOCOL + path + bg_img_tablet + "?" + StoTrackerUtils.generateRandomString();
                else {
                    StoLog.w(TAG, "getBackgroundImageUrl for tablet not found");
                    return null;
                }
            } else {
                if (bg_img_mobile != null && !bg_img_mobile.isEmpty())
                    buildedBGImageURL = this.DF_LOGO_IMG_URL_PROTOCOL + path + bg_img_mobile + "?" + StoTrackerUtils.generateRandomString();
                else {
                    StoLog.w(TAG, "getBackgroundImageUrl for phone not found");
                    return null;
                }
            }
        }

        return buildedBGImageURL;
    }

    @Expose(serialize = false, deserialize = false)
    private String buildedLogoImageURL;

    public String getLogoImageURL() {
        if (buildedLogoImageURL == null) {
            StoLog.d(TAG, "getLogoImageURL");
            if (logo_img_mobile != null && !logo_img_mobile.isEmpty())
                buildedLogoImageURL = this.DF_LOGO_IMG_URL_PROTOCOL + path + logo_img_mobile + "?" + StoTrackerUtils.generateRandomString();
            else {
                StoLog.w(TAG, "getLogoImageURL (phone only) not found");
                return null;
            }
        }
        return buildedLogoImageURL;
    }

    public int getBorderColor() {
        if (border_color != null && !border_color.isEmpty()) {
            return Color.parseColor(this.border_color);
        } else {
            return Color.WHITE;
        }
    }

    public int getBackgroundColor() {
        if (bg_color != null && !bg_color.isEmpty()) {
            return Color.parseColor(this.bg_color);
        } else {
            return Color.WHITE;
        }
    }

    public boolean hasMultiBackground() { return option_multi_bg; }

    @Override
    public void setOptionButtonColor(final Button optionButton) {

        String imageURL = getOptionButtonImageURL();
        if (imageURL != null) {
            Glide.with(optionButton).load(imageURL).centerCrop().into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        optionButton.setBackground(resource);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        } else {
            optionButton.setBackgroundColor(getOptionButtonColor());
            optionButton.setTextColor(getOptionTextColor());
        }
        String optionText = getOptionText();
        if (optionText != null)
            optionButton.setText(optionText);
    }

    @Override
    public List<String> getProducts() {
        List<String> productsToRetrieve = new ArrayList<>();
        for (int i = 0; i < getButtonContentList().size(); i++) {
            productsToRetrieve.addAll(getButtonContentList().get(i).getProductIds());
        }
        return productsToRetrieve;
    }

    public StoFormatOptionType getOptionType() {
        return option;
    }

    public String getOptionText() {
        if (option_text != null && !option_text.isEmpty())
            return option_text;
        return null;
    }

    public String getOptionValue() {
        switch (option) {
            case NONE:
                break;
            case REDIRECTION:
                if (option_redirection_url != null && !option_redirection_url.isEmpty())
                    return option_redirection_url;
                break;
            case VIDEO:
                if (option_video_link != null && !option_video_link.isEmpty())
                    return option_video_link;
                break;
            case PDF:
                if (option_file != null && !option_file.isEmpty())
                    return this.DF_LOGO_IMG_URL_PROTOCOL + path + option_file + "?" + StoTrackerUtils.generateRandomString();
                break;
            case LEGAL:
                return getOptionText();
        }
        return null;
    }

    @Expose(serialize = false, deserialize = false)
    private String buildedOptionButtonImageURL;

    private String getOptionButtonImageURL() {
        if (buildedOptionButtonImageURL == null) {
            if (option_button_img != null && !option_button_img.isEmpty()) {
                buildedOptionButtonImageURL = DF_LOGO_IMG_URL_PROTOCOL + path + option_button_img + "?" + StoTrackerUtils.generateRandomString();
            }
        }
        return buildedOptionButtonImageURL;
    }

    public int getOptionTextColor() {
        if (option_txt_color != null && ! option_txt_color.isEmpty()) {
            return Color.parseColor(this.option_txt_color);
        } else {
            StoLog.w(TAG, "getOptionTextColor not found");
            return Color.WHITE;
        }
    }

    private int getOptionButtonColor() {
        if (option_button_color != null && !option_button_color.isEmpty()) {
            return Color.parseColor(this.option_button_color);
        } else {
            return Color.WHITE;
        }
    }

    @Expose(serialize = false, deserialize = false)
    private Map<String, String> buildedMultiBGImageURL;

    private String getButtonImageURL(String fileName) {
        if (buildedMultiBGImageURL == null) {
            buildedMultiBGImageURL = new HashMap<>();
        }
        if (!buildedMultiBGImageURL.containsKey(fileName) && fileName != null && !fileName.isEmpty()) {
            buildedMultiBGImageURL.put(fileName, DF_LOGO_IMG_URL_PROTOCOL + path + fileName + "?" + StoTrackerUtils.generateRandomString());
        }

        return buildedMultiBGImageURL.get(fileName);
    }

    public String getMultiBackgroundImage(int index) {
        switch (index) {
            case 0:
                return getButtonImageURL(multi_bg_img_01);
            case 1:
                return getButtonImageURL(multi_bg_img_02);
            case 2:
                return getButtonImageURL(multi_bg_img_03);
            case 3:
                return getButtonImageURL(multi_bg_img_04);
            case 4:
                return getButtonImageURL(multi_bg_img_05);
        }
        return null;
    }

    public int getButtonTextColor() {
        if (button_txt_color != null && !button_txt_color.isEmpty()) {
            return Color.parseColor(this.button_txt_color);
        } else {
            StoLog.w(TAG, "getButtonTextColor not found");
            return Color.WHITE;
        }
    }

    public int getButtonSelectedTextColor() {
        if (button_txt_color_selected != null && !button_txt_color_selected.isEmpty()) {
            return Color.parseColor(this.button_txt_color_selected);
        } else {
            StoLog.w(TAG, "getButtonSelectedTextColor not found");
            return Color.WHITE;
        }
    }

    public int getButtonColor() {
        if (button_color != null && !button_color.isEmpty()) {
            return Color.parseColor(this.button_color);
        } else {
            StoLog.w(TAG, "getButtonColor not found");
            return Color.WHITE;
        }
    }

    public int getButtonSelectedColor() {
        if (button_color_selected != null && !button_color_selected.isEmpty()) {
            return Color.parseColor(this.button_color_selected);
        } else {
            StoLog.w(TAG, "getButtonSelectedColor not found");
            return Color.WHITE;
        }
    }
}
