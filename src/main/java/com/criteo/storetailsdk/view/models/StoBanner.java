package com.criteo.storetailsdk.view.models;

import android.graphics.Color;
import android.util.Log;

import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;
import com.criteo.storetailsdk.view.StoFormatType;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Field;

public class StoBanner extends StoFormat {

    private transient final String TAG = "StoBanner";

    @Expose()
    private String path;

    @Expose()
    private String bg_img_mobile;

    @Expose()
    private String bg_img_tablet;

    @Expose()
    private String option_redirection_url;

    @Expose()
    private String bg_color;

    @Expose(serialize = false, deserialize = false)
    private final String DF_LOGO_IMG_URL_PROTOCOL = "https:";

    public StoBanner() {
        super();
    }

    @Override
    public StoFormatType getFormatType() {
        return StoFormatType.stoBanner;
    }

    @Expose(serialize = false, deserialize = false)
    private String buildedBGImageURL;

    public String getBackgroundImageURL(boolean isTablet) {
        if (buildedBGImageURL == null) {
            if (isTablet) {
                if (bg_img_tablet != null && !bg_img_tablet.isEmpty()) {
                    buildedBGImageURL = this.DF_LOGO_IMG_URL_PROTOCOL + path + bg_img_tablet + "?" + StoTrackerUtils.generateRandomString();
                } else {
                    StoLog.w(TAG, "getLogoImageUrl: no logo url found!");
                    return null;
                }
            } else {
                if (bg_img_mobile != null && !bg_img_mobile.isEmpty()) {
                    buildedBGImageURL = this.DF_LOGO_IMG_URL_PROTOCOL + path + bg_img_mobile + "?" + StoTrackerUtils.generateRandomString();
                } else {
                    StoLog.w(TAG, "getLogoImageUrl: no logo url found!");
                    return null;
                }
            }
        }
        return buildedBGImageURL;
    }

    public String getDeeplink() {
        return option_redirection_url;
    }

    public int getBackgroundColor() {
        if (bg_color != null && ! bg_color.isEmpty()) {
            return Color.parseColor(this.bg_color);
        } else {
            StoLog.w(TAG, "getBackgroundColor not found");
            return Color.WHITE;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                Log.d(TAG, ex.toString());
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
