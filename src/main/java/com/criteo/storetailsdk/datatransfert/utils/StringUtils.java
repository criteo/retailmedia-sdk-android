package com.criteo.storetailsdk.datatransfert.utils;

import java.util.List;
import java.util.Map;

public class StringUtils {
    private static String newLine = System.getProperty("line.separator");
    private static String indentation = "  ";

    public static String getIndentation(int count) {
        StringBuilder buf = new StringBuilder(indentation.length() * count);
        for (int bakCount = count; bakCount > 0; bakCount--) {
            buf.append(indentation);
        }
        return buf.toString();
    }

    public static String toString(Map<String, Object> object, int level) {
        StringBuilder result = new StringBuilder();
        String indent = getIndentation(level);

        result.append(indent + "{" + newLine);
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            result.append(indent + "key(\""+key+"\") & ("+value.getClass().getName()+") value(");
            String subresult;
            if (value instanceof List) {
                subresult = newLine+ toString((List) value, level + 1);
            }
            else if(value instanceof Map) {
                subresult = newLine + toString((Map)value, level + 1);
            }
            else {
                if (value instanceof String) {
                    subresult = "\"" + value.toString() + "\"";
                } else {
                    subresult = value.toString();
                }
                subresult += ")";
            }

            result.append(subresult);
            result.append(",");
            result.append(newLine);
        }
        result.append(indent);
        result.append("}");
        result.append(newLine);
        return result.toString();
    }

    public static String toString(List array, int level) {
        StringBuilder result = new StringBuilder();
        String indent = getIndentation(level);

        result.append(indent + "[" + newLine);
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            String subresult;
            if (value instanceof List) {
                subresult = toString((List) value, level + 1);
            }
            else if(value instanceof Map) {
                subresult = toString((Map)value, level + 1);
            }
            else {
                if (value instanceof String) {
                    subresult = indent+indentation+"index : "+i+" ("+value.getClass().getName()+") = \"" + value.toString() + "\"";
                } else {
                    subresult = indent+indentation+"index : "+i+" ("+value.getClass().getName()+") = "+ value.toString();
                }
            }
            result.append(subresult);
            result.append(",");
            result.append(newLine);
        }
        result.append(indent+"]");
        return result.toString();
    }
}
