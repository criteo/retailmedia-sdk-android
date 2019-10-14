package com.criteo.storetailsdk.view.models;

import com.criteo.storetailsdk.view.StoFormatType;
import com.google.gson.annotations.Expose;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.logs.StoLog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

/**
 * Created by mikhailpogorelov on 09/10/2017.
 */

public abstract class StoFormat implements Comparable<StoFormat> {

    private transient final String TAG = "StoFormat";

    /**
     * Unique ID
     */
    @Expose(serialize = false, deserialize = false)
    transient String uniqueID;

    /**
     * Position of the View
     * Set from the values
     */
    @Expose(serialize = false, deserialize = false)
    transient int position;


    /**
     * List of {@link StoQueryStringParam} which is attached to the view
     */
    List<StoQueryStringParam> queryStringParamsList;

    public StoFormat() {
        this.uniqueID = this.getClass().toString() + ":" + UUID.randomUUID().toString();
        this.position = -1;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<StoQueryStringParam> getQueryStringParamsList() {
        return queryStringParamsList;
    }

    public void setQueryStringParamsList(List<StoQueryStringParam> queryStringParamsList) {
        this.queryStringParamsList = queryStringParamsList;
    }

    public abstract StoFormatType getFormatType();


    /**
     * Returns attributes with their values
     *
     * @return
     */
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
            if (Modifier.isPublic(field.getModifiers())) {
            result.append("  ");
                try {
                    result.append(field.getName());
                    result.append(": ");
                    //requires access to private field:

                    result.append(field.get(this));
                } catch (IllegalAccessException ex) {
                    StoLog.d(TAG, ex.toString());
                }
                result.append(newLine);
            }
        }
        result.append("  ");
        result.append("positions: " + position);
        result.append(newLine);

        result.append("}");

        return result.toString();
    }

    @Override
    public int compareTo(StoFormat stoFormat) {
        if (getPosition() > stoFormat.getPosition())
            return 1;
        else if (getPosition() < stoFormat.getPosition())
            return -1;
        return 0;
    }
}
