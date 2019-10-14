package com.criteo.storetailsdk.tracker.utils;

import android.text.TextUtils;

import com.criteo.storetailsdk.datatransfert.values.StoTrackActionValue;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.extras.crawl.StoCrawl;
import com.criteo.storetailsdk.extras.models.StoProduct;
import com.criteo.storetailsdk.logs.StoLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MikhailPOGORELOV on 14/04/2017.
 */

public class StoTrackerUtils {

    private static final String TAG = "StoTrackerUtils";

    public static String getRandomizedUID() {
        return md5(generateRandomString());
    }


    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }


    private static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the key word with no non alphanumeric characters and no spaces,
     * they are replaced by underscores
     *
     * @param key_word
     * @return
     */
    public static String getFormattedProductLabel(String key_word) {
        if (key_word != null) {
            String normalized = Normalizer.normalize(key_word, Normalizer.Form.NFD);
            String result = normalized.replaceAll("[^\\p{ASCII}]", "");
            result = result.replaceAll("[^A-Za-z0-9.,]", "_");


            result = result.replaceAll(",", ".");
            result = result.replaceAll("_{2,}", "_").toLowerCase();

            StoLog.d(TAG, "getFormattedProductLabel: " + result);
            return result;
        } else {
            StoLog.w(TAG, "getFormattedProductLabel: key_word is null!");
            return null;
        }
    }

    public static String getFormattedRetailPage(String... values) {
        if (values == null || values.length == 0) {
            StoLog.w(TAG, "getFormattedRetailPage: values is null");
            return null;
        }

        List<String> valuesNotEmpty = new ArrayList<>();
        for (String value: values) {
            if (value != null && !value.isEmpty())
                valuesNotEmpty.add(value);
        }

        String retailPage = TextUtils.join("/", valuesNotEmpty);
        String normalized = Normalizer.normalize(retailPage, Normalizer.Form.NFD);
        String result = normalized.replaceAll("[^\\p{ASCII}]", "");
        result = result.replaceAll("[^A-Za-z0-9/]", "_");

        result = result.replaceAll("_{2,}", "_").toLowerCase();

        StoLog.d(TAG, "getFormattedRetailPage: " + result);
        return result;
    }


    /**
     * Returns the formatted key_word corresponding to the retailSearch
     *
     * @param retailSearch
     * @return
     */
    public static String getFormattedRetailSearch(String retailSearch) {
        StoLog.d(TAG, "getFormattedRetailSearch");

        if (retailSearch != null && retailSearch.compareTo("") != 0) {
            String normalized = Normalizer.normalize(retailSearch, Normalizer.Form.NFD);
            String result = normalized.replaceAll("[^\\p{ASCII}]", "");
            result = result.replaceAll("[^A-Za-z0-9.,]", "_");

            result = "_" + result + "_";

            result = result.replaceAll(",", ".");
            result = result.replaceAll("_{2,}", "_").toLowerCase();

            StoLog.d(TAG, "getFormattedRetailSearch: " + result);
            return result;
        } else {
            StoLog.w(TAG, "getFormattedRetailSearch: key_word is null!");
            return null;
        }
    }

    /**
     * Returns the formatted String corresponding to the forcedCity
     *
     * @param forcedCity
     * @return
     */
    public static String getFormattedForcedCity(String forcedCity) {
        if (forcedCity != null) {
            String normalized = Normalizer.normalize(forcedCity, Normalizer.Form.NFD);
            String result = normalized.replaceAll("[^\\p{ASCII}]", "");
            result = result.replaceAll("[^A-Za-z0-9.,]", "_");

            result = result.replaceAll(",", ".");
            result = result.replaceAll("_{2,}", "_").toLowerCase();

            StoLog.d(TAG, "getFormattedForcedCity: " + result);
            return result;
        } else {
            StoLog.w(TAG, "getFormattedForcedCity: key_word is null!");
            return null;
        }
    }

    /**
     * Returns the formatted key_word corresponding to the error
     *
     * @param error
     * @return
     */
    public static String getFormattedError(String error) {
        StoLog.d(TAG, "getFormattedError");

        if (error != null && error.compareTo("") != 0) {
            String normalized = Normalizer.normalize(error, Normalizer.Form.NFD);
            String result = normalized.replaceAll("[^\\p{ASCII}]", "");
            result = result.replaceAll("[^A-Za-z0-9]", "_").toLowerCase();

            result = result.replaceAll("_{2,}", "_").toLowerCase();

            StoLog.d(TAG, "getFormattedError: " + result);
            return result;
        } else {
            StoLog.w(TAG, "getFormattedError: key_word is null!");
            return null;
        }
    }

    /**
     * Returns a XML file for sending POST request
     *
     * @param stoProduct
     * @return: xml document
     */
    public static Document getXMLDocument(StoProduct stoProduct, StoTrackEventValue stoTrackEventValue) {

        /*
         * Verifies if the stoProductBasket is not null
         */
        if (stoProduct == null) {
            StoLog.e(TAG, "getXMLDocument: stoProduct is null!");
            return null;
        }

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            /*
             * Root element
             */
            Element rootElement = doc.createElement("bsk");
            doc.appendChild(rootElement);

            /*
             * Items element
             */
            Element element_items = doc.createElement("itms");
            rootElement.appendChild(element_items);

            /*
             * Li element
             */
            Element element_li = doc.createElement("li");
            element_items.appendChild(element_li);

            /*
             * Product id element
             */
            Element element_product_id = doc.createElement("id");
            element_product_id.appendChild(doc.createTextNode(stoProduct.getId()));


            /*
             * Product name element
             */
            Element element_product_name = doc.createElement("name");
            element_product_name.appendChild(doc.createTextNode(getFormattedProductLabel(stoProduct.getName())));

            /*
             * Product promo element
             */
            Element element_product_promo = doc.createElement("promo");
            if (stoProduct.getPromo()) {
                element_product_promo.appendChild(doc.createTextNode("Y"));
            }else {
                element_product_promo.appendChild(doc.createTextNode("N"));
            }

            /*
             * Product quantity element
             */
            Element element_product_qty = doc.createElement("qty");


            String qty = "1";

            if (stoTrackEventValue == StoTrackEventValue.QuantityLess) {
                qty = "-1";
            }

            element_product_qty.appendChild(doc.createTextNode(qty));

            /*
             * Product price element
             */
            Element element_product_price = doc.createElement("price");

            element_product_price.appendChild(doc.createTextNode(String.valueOf(stoProduct.getPrice())));

            /*
             * Total price element
             */
            Element element_total_price = doc.createElement("tv");

            double total_price = stoProduct.getPrice();
            if (stoTrackEventValue == StoTrackEventValue.QuantityLess) {
                total_price *= -1;
            }
            element_total_price.appendChild(doc.createTextNode(String.valueOf(total_price)));


            /*
             * Adds elements to the element_li
             */
            element_li.appendChild(element_product_id);
            element_li.appendChild(element_product_name);
            element_li.appendChild(element_product_promo);
            element_li.appendChild(element_product_qty);
            element_li.appendChild(element_product_price);

            /*
             * Adds the element_total_price to the root element
             */
            rootElement.appendChild(element_total_price);

            return doc;

        } catch (ParserConfigurationException pce) {
            StoLog.e(TAG, "getXMLDocument: exception:" + pce.toString());
            if (pce.getMessage() != null) {
                StoLog.e(TAG, "getXMLDocument: exception message: " + pce.getMessage());
            } else {
                StoLog.e(TAG, "getXMLDocument: exception message is null");
            }
            pce.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a XML file for sending POST request
     *
     * @param stoProductBasketList
     * @return: xml document
     */
    public static Document getXMLDocument(List<StoProduct> stoProductBasketList, String purchaseOrderNumber) {

        if (stoProductBasketList == null) {
            StoLog.e(TAG, "getXMLDocument: stoProductBasketList is null!");
            return null;
        }
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            /*
             * Root element
             */
            Element rootElement = doc.createElement("bsk");
            doc.appendChild(rootElement);

            /*
             * Items element
             */
            Element element_items = doc.createElement("itms");
            rootElement.appendChild(element_items);


            /*
             * Adds all products to the XML
             */
            for (StoProduct stoProductBasket : stoProductBasketList) {

                /*
                 * Li element
                 */
                Element element_li = doc.createElement("li");
                element_items.appendChild(element_li);

                /*
                 * Product id element
                 */
                Element element_product_id = doc.createElement("id");
                element_product_id.appendChild(doc.createTextNode(stoProductBasket.getId()));

                /*
                 * Product name element
                 */
                Element element_product_name = doc.createElement("name");
                element_product_name.appendChild(doc.createTextNode(getFormattedProductLabel(stoProductBasket.getName())));

                /*
                 * Product promo element
                 */
                Element element_product_promo = doc.createElement("promo");
                if (stoProductBasket.getPromo())
                    element_product_promo.appendChild(doc.createTextNode("Y"));
                else
                    element_product_promo.appendChild(doc.createTextNode("N"));

                /*
                 * Product quantity element
                 */
                Element element_product_qty = doc.createElement("qty");
                element_product_qty.appendChild(doc.createTextNode(String.valueOf(stoProductBasket.getQuantity())));

                /*
                 * Product price element
                 */
                Element element_product_price = doc.createElement("price");

                /*
                 * Calculates the total price of the product (quantity * price)
                 */
                element_product_price.appendChild(doc.createTextNode(String.valueOf(stoProductBasket.getPrice())));

                /*
                 * Adds elements to the element_li
                 */
                element_li.appendChild(element_product_id);
                element_li.appendChild(element_product_name);
                element_li.appendChild(element_product_promo);
                element_li.appendChild(element_product_qty);
                element_li.appendChild(element_product_price);
            }

            /*
             * Calculates the total price of the basket
             */
            double totalPrice = 0;
            for (StoProduct stoProductBasket : stoProductBasketList) {
                StoLog.i(TAG, "price: " + stoProductBasket.getPrice() + " quantity: " + stoProductBasket.getQuantity());
                totalPrice += stoProductBasket.getPrice() * stoProductBasket.getQuantity();
            }
            StoLog.i(TAG, "getXMLDocument: total price: " + totalPrice);
            /*
             * Total price element
             */
            Element element_total_price = doc.createElement("tv");
            element_total_price.appendChild(doc.createTextNode(String.valueOf((round(totalPrice, 2)))));

            rootElement.appendChild(element_total_price);

            /**
             * Purchase order number
             */
            if (purchaseOrderNumber != null && !purchaseOrderNumber.equals("")) {
                Element element_basket_id = doc.createElement("id");
                element_basket_id.appendChild(doc.createTextNode(purchaseOrderNumber));
                rootElement.appendChild(element_basket_id);
            }

            StoLog.i(TAG, "getXMLDocument: " + doc.getXmlVersion());
            return doc;

        } catch (ParserConfigurationException e) {
            if (e.getMessage() != null) {
                StoLog.e(TAG, "getXMLDocument: " + e.getMessage());
            } else {
                StoLog.e(TAG, "getXMLDocument: " + e.toString());
            }
        }
        return null;
    }

    private static double round(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, decimals);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Converts the StoTrackEventValue to related String
     *
     * @param stoTrackEventValue
     * @return
     */
    public static String getUpdatedTrackEventValue(StoTrackEventValue stoTrackEventValue) {
        switch (stoTrackEventValue) {
            case OpenPdp:
                return "open-pdp";
            case AddToList:
                return "add-list";
            case QuantityChange:
                return "qty-chge";
            case QuantityMore:
                return "qty-more";
            case QuantityLess:
                return "qty-less";
            case AbkBtn:
                return "abk-btn";
            case BrowseProduct:
                return "browse-pdct";
            case OpenPDF:
                return "dwnld-pdf";
            case OpenVideo:
                return "video-play";
            case CloseVideo:
                return "video-close";
            default:
                return stoTrackEventValue.toString();
        }
    }


    /**
     * Converts the document to String
     *
     * @param doc
     * @return
     */
    public static String toString(Document doc) {

        if (doc == null) {
            StoLog.e(TAG, "toString: doc is null!");
            return null;
        }
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                StoLog.e(TAG, "toString: " + ex.getMessage());
            } else {
                StoLog.e(TAG, "toString: error during converting the document to string!");
            }
            return null;
        }
    }


}
