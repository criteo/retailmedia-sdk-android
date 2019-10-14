package com.criteo.storetailsdk.view.models;

import com.criteo.storetailsdk.datatransfert.utils.StringUtils;
import com.criteo.storetailsdk.view.StoFormatType;
import com.google.gson.annotations.Expose;

import java.util.Map;

public class StoFormatCustom extends StoFormat {

    @Expose(serialize = false, deserialize = false)
    private final String DF_LOGO_IMG_URL_PROTOCOL = "https:";

    @Expose(serialize = false, deserialize = false)
    private String formatName;

    @Expose()
    private String path;

    @Expose(serialize = false, deserialize = false)
    private Map<String, Object> values;


    public StoFormatCustom() {
        super();
    }

    @Override
    public StoFormatType getFormatType() {
        return StoFormatType.stoCustom;
    }

    public String getFormatName() { return formatName; }

    public void setFormatName(String formatName) { this.formatName = formatName; }

    public String getPath() { return DF_LOGO_IMG_URL_PROTOCOL + path; }

    public Map<String, Object> getValues() { return values; }
    public void setValues(Map<String, Object> map) { values = map; }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(newLine);
        result.append("StoFormatCustom {");
        result.append(newLine);
        result.append("  formatName: " + getFormatName() + ",");
        result.append(newLine);
        result.append("  path: " + getPath() + ",");
        result.append(newLine);

        result.append("  values: ");
        result.append(newLine);
        result.append(StringUtils.toString(values, 2));
        result.append("}");

        return result.toString();
    }
}