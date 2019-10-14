package com.criteo.storetailsdk.tracker;

import android.content.Context;

import com.criteo.storetailsdk.datatransfert.error.StoErrorHandlerImp;
import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.datatransfert.query.StoQueryStringParam;
import com.criteo.storetailsdk.datatransfert.request.StoRequestHandler;
import com.criteo.storetailsdk.datatransfert.values.StoTrackActionValue;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.extras.StoAppCommunicator;
import com.criteo.storetailsdk.extras.crawl.StoCrawl;
import com.criteo.storetailsdk.extras.models.StoProduct;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.updating.QSPUpdate;
import com.criteo.storetailsdk.tracker.utils.StoTrackerUtils;
import com.criteo.storetailsdk.view.StoFormatOptionType;
import com.criteo.storetailsdk.view.managing.StoPageType;
import com.criteo.storetailsdk.view.models.StoButtonContent;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.criteo.storetailsdk.view.models.StoFormatWithButtons;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class StoTracker {

    private String TAG = "StoTracker";

    private Context                 context;
    private StoTrackerHelper        stoTrackerHelper;
    private StoRequestHandler       stoRequestHandler;
    private StoErrorHandlerImp      stoErrorHandler;

    private String                  retailName;
    private String                  retailPage;
    private String                  retailShop;
    private String                  forcedCity;
    private String                  forcedLatitude;
    private String                  forcedLongitude;
    private String                  appVersion;
    private boolean                 enableGDPR;
    private boolean                 impEqualToView;

    private static StoTracker instance = null;

    private StoTracker(Context context) {
        this.context = context;
        this.stoTrackerHelper = StoTrackerHelper.getInstance(context);
        this.stoRequestHandler = StoRequestHandler.getInstance(context, stoTrackerHelper);
        this.stoErrorHandler = new StoErrorHandlerImp(this.stoRequestHandler);
        retailName = "";
        retailPage = "";
        retailShop = "";
        forcedCity = "";
        forcedLatitude = "";
        forcedLongitude = "";
        appVersion = "";
        enableGDPR = false;
        impEqualToView = false;
    }

    public static StoTracker getInstance(Context context) {
        synchronized (StoTracker.class) {
            if (instance == null) {
                instance = new StoTracker(context);
            }
        }
        return instance;
    }

    /**
     * Initializes the main parameters of the tracker
     *
     * @param retailName
     * @param retailShop
     * @param forcedCity
     * @param forcedLatitude
     * @param forcedLongitude
     */
    public void initialize(String retailName, String retailShop, String forcedCity, String forcedLatitude, String forcedLongitude) {
        this.retailName = retailName;
        this.retailShop = retailShop;
        this.forcedCity = forcedCity;
        this.forcedLatitude = forcedLatitude;
        this.forcedLongitude = forcedLongitude;
        StoLog.d(TAG, "StoTracker initialized");
    }

    public void setApplicationVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * Enable or disable protection of user personal datas
     *
     * @param isEnabled If true don't track personal datas
     */
    public void enableGDPR(boolean isEnabled) {
        enableGDPR = isEnabled;
    }

    /**
     * Show all Criteo logs
     */
    public void enableLogs() {
        StoLog.enableLogs();
    }

    /**
     * Hide all Criteo logs
     */
    public void disableLogs() {
        StoLog.disableLogs();
    }

    /**
     * View tracking is sent when ad is completely shown
     * Impression tracking can be sent when ad is build or when ad is completely shown
     * @param isEqual true means impression and view trackers are sent when ad is visible at 100%
     *                (default) impression will be sent when ad is build and view will be sent when ad is completely shown
     */
    public void setImpressionEqualToView(boolean isEqual) { impEqualToView = isEqual; }
    public boolean isImpressionEqualToView() { return impEqualToView; }

    /**
     * Called when the home page loads
     *
     * @param availableProductsCount Corresponds to number of products displayed on page (send 0 if no product)
     */
    public synchronized void lodHomePage(int availableProductsCount) {
        if (!enableGDPR) {
            StoLog.i(TAG, "lodHomePage call...");

            stoTrackerHelper.setStoPageType(StoPageType.StoHomePage);
            retailPage = "home";

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            this.stoTrackerHelper.removeFormats();

            StoCrawl.getInstance().clear();

            stoTrackerHelper.startTrackTime();

            qspUpdate.updateQSPRetailSearch(stoQueryString, "");
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.lod);
            qspUpdate.updateQSPAvailableProducts(stoQueryString, availableProductsCount);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Called when an activity or a fragment is resumed (equivalent to page loading in browser)
     *
     * @param formattedRetailPage
     * @param stoPageType
     * @param availableProductsCount Corresponds to number of products displayed on page (send 0 if no product)
     * @param identifier
     */
    public synchronized void lod(String formattedRetailPage, StoPageType stoPageType, int availableProductsCount, String identifier) {
        if (!enableGDPR) {
            StoLog.i(TAG, "lod call..." + stoPageType.toString());

            stoTrackerHelper.setStoPageType(stoPageType);
            retailPage = "home/" + formattedRetailPage;

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            this.stoTrackerHelper.removeFormats();

            StoCrawl.getInstance().clear();

            stoTrackerHelper.startTrackTime();

            qspUpdate.updateQSPRetailSearch(stoQueryString, "");
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.lod);
            qspUpdate.updateQSPAvailableProducts(stoQueryString, availableProductsCount);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString, identifier);
        }
    }

    /**
     * Called when a product page loads
     *
     * @param formattedRetailPage
     * @param productId
     */
    public synchronized void lodProductPage(String formattedRetailPage, String productId) {
        if (!enableGDPR) {
            StoLog.i(TAG, "lodProductPage call...");

            stoTrackerHelper.setStoPageType(StoPageType.StoProductPage);
            retailPage = "home/product_page/" + formattedRetailPage;

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            this.stoTrackerHelper.removeFormats();

            StoCrawl.getInstance().clear();

            stoTrackerHelper.startTrackTime();

            qspUpdate.updateQSPRetailSearch(stoQueryString, "");
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPProductID(stoQueryString, productId);
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.lod);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Called when a activity is resumed (equivalent to page loading in browser)
     * Used for search
     *
     * @param querySearch Correspond to user's input raw
     * @param availableProductsCount Corresponds to number of products displayed on page (send 0 if no product)
     */
    public synchronized void lodSearch(String querySearch, int availableProductsCount) {
        if (!enableGDPR) {
            StoLog.d(TAG, "lodSearch call ...: " + querySearch);

            this.stoTrackerHelper.setStoPageType(StoPageType.StoSearchPage);
            retailPage = "home/search";

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            this.stoTrackerHelper.removeFormats();
            StoCrawl.getInstance().clear();

            if (querySearch == null) {
                StoLog.w(TAG, "lod(querySearch): querySearch is null! Calling lod method");
                return;
            } else {
                StoLog.d(TAG, "lod(querySearch): keyWord: " + querySearch);
            }

            this.stoTrackerHelper.startTrackTime();
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.lod);
            qspUpdate.updateQSPAvailableProducts(stoQueryString, availableProductsCount);
            qspUpdate.updateQSPRetailSearch(stoQueryString, StoTrackerUtils.getFormattedRetailSearch(querySearch));
            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Called when user opens shopping cart
     */
    public synchronized void lodCheckout() {
        if (!enableGDPR) {
            StoLog.i(TAG, "lodProductPage call...");

            stoTrackerHelper.setStoPageType(StoPageType.StoCheckoutPage);
            retailPage = "home/checkout";

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            this.stoTrackerHelper.removeFormats();

            StoCrawl.getInstance().clear();

            stoTrackerHelper.startTrackTime();

            qspUpdate.updateQSPRetailSearch(stoQueryString, "");
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.lod);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Called for each no-exp object received in load
     *
     * @param queryStringParams
     */
    public synchronized void noexp(List<StoQueryStringParam> queryStringParams) {
        if (!enableGDPR) {
            StoLog.d(TAG, "noexp");

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (queryStringParams == null || queryStringParams.isEmpty()) {
                StoLog.e(TAG, "noexp: queryStringParams is null or empty");
                this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, "noexp: queryStringParams is null or empty");
                return;
            }

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.noexp);
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateByList(queryStringParams, stoQueryString);
            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Track action call when a format has been viewed
     *
     * @param stoFormat :   format
     */
    public synchronized void imp(StoFormat stoFormat) {
        if (!enableGDPR && !stoTrackerHelper.isImpressed(stoFormat.getUniqueID())) {
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormat(qspUpdate, stoQueryString, stoFormat, "imp"))
                return;

            stoTrackerHelper.addImpressedObject(stoFormat.getUniqueID());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.imp);
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);
            qspUpdate.updateByList(stoFormat.getQueryStringParamsList(), stoQueryString);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Called when an object is shown
     *
     * @param stoFormat: Format
     */
    public synchronized void view(StoFormat stoFormat) {
        if (!enableGDPR && !stoTrackerHelper.isViewed(stoFormat.getUniqueID())) {
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormat(qspUpdate, stoQueryString, stoFormat, "view"))
                return;

            StoLog.i(TAG, "view: first show: id " + stoFormat.getUniqueID());

            stoTrackerHelper.addViewedObject(stoFormat.getUniqueID());
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.view);
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);
            qspUpdate.updateByList(stoFormat.getQueryStringParamsList(), stoQueryString);
            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    private static StoProduct getProductInList(List<StoProduct> productList, String productId) {
        for (StoProduct product : productList) {
            if (product.getId().compareTo(productId) == 0)
                return product;
        }
        return null;
    }

    /**
     * Checks if a Format with buttons has available products otherwise don't display it
     *
     * @param stoFormatWithButtons StoFormatWithButtons format
     */
    public synchronized boolean isFormatAvailable(StoFormatWithButtons stoFormatWithButtons) {
        final String TAG = "StoTracker";
        StoLog.d(TAG, "checkAvailabilityProducts");

        // Retrieve list of all Products
        List<StoProduct> stoProducts = StoCrawl.getInstance().requestProducts(stoFormatWithButtons.getProducts());

        if (stoProducts.isEmpty()) {
            StoLog.d(TAG, "checkAvailabilityProducts: stoProducts list is empty!");

            boolean hasExclusive = false;
            for (StoButtonContent buttonContent: stoFormatWithButtons.getButtonContentList()) {
                if (buttonContent.isMandatory())
                    hasExclusive = true;
            }

            stoFormatWithButtons.setButtonContentList(new ArrayList<StoButtonContent>());
            availability(stoFormatWithButtons, 0, hasExclusive);

            return false;
        }

        Boolean hasExclusive = false;
        List<StoButtonContent> buttonContentList = new ArrayList<>(stoFormatWithButtons.getButtonContentList());
        for (int i = 0; i < buttonContentList.size(); ++i) {
            StoButtonContent buttonContent = buttonContentList.get(i);
            StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " ");
            List<String> buttonProductIds = buttonContent.getProductIds();
            for (String buttonProductId : buttonProductIds) {
                StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " buttonProductId " + buttonProductId);
                StoProduct product = getProductInList(stoProducts, buttonProductId);
                if (product == null || !product.isAvailable()) {
                    StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " buttonProductId " + buttonProductId + " not available");
                    buttonProductIds.remove(buttonProductId);
                } else {
                    buttonContent.setProductId(buttonProductId);
                }
            }

            StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " productIds size = " + buttonProductIds.size());
            if (buttonProductIds.isEmpty()) {
                if (buttonContent.isMandatory()) {
                    hasExclusive = true;
                } else {
                    StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " 0 product");
                    buttonContentList.remove(buttonContent);
                    i--;
                }
            } else if (i == 0) {
                StoLog.d(TAG, "checkAvailabilityProducts button pos " + i + " select product " + buttonProductIds.get(0));
                stoFormatWithButtons.setProductIdSelected(buttonProductIds.get(0));
            }
        }

        stoFormatWithButtons.setButtonContentList(buttonContentList);
        availability(stoFormatWithButtons, buttonContentList.size(), hasExclusive);

        if (buttonContentList.isEmpty()) {
            StoLog.d(TAG, "checkAvailabilityProducts 0 button");
            return false;
        } else if (hasExclusive) {
            StoLog.d(TAG, "checkAvailabilityProducts hasMandatoryButtonUnavailable");
            return false;
        }

        return true;
    }

    /**
     * Called on creation of the format
     * Sends the number of available products for the format
     *
     * @param stoFormat: stoFormat
     * @param nbProductAvailable: Number of products available
     */
    public synchronized void availability(final StoFormat stoFormat, int nbProductAvailable) {
        this.availability(stoFormat, nbProductAvailable, false);
    }

    /**
     * Called on creation of the format
     * Sends the number of available products for the format
     *
     * @param stoFormat: stoFormat
     * @param nbProductAvailable: Number of products available
     * @param hasMandatoryButtonWithoutAvailableProduct: if a mandatory button has no product available
     */
    public synchronized void availability(final StoFormat stoFormat, int nbProductAvailable, boolean hasMandatoryButtonWithoutAvailableProduct) {
        if (!enableGDPR) {
            StoLog.i(TAG, "availability call");

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormat(qspUpdate, stoQueryString, stoFormat, "availability"))
                return;

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));

            if (!this.stoTrackerHelper.isNbAvailProdSent(stoFormat.getUniqueID())) {
                this.stoTrackerHelper.addNbAvailProductsSent(stoFormat.getUniqueID());
                qspUpdate.updateByList(stoFormat.getQueryStringParamsList(), stoQueryString);
                qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.avail);
                qspUpdate.updateQSPTrackValue(stoQueryString, String.valueOf(nbProductAvailable));
                if (hasMandatoryButtonWithoutAvailableProduct) {
                    qspUpdate.updateQSPTrackLabel(stoQueryString, "nodisplay");
                }
                qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);

                stoQueryString.printQuery(true);
                stoRequestHandler.requestGet(stoQueryString);
            } else {
                StoLog.i(TAG, "availability: the view has already been constructed");
            }
        }
    }

    /**
     * Called when a button (corresponding to a product) in the format is clicked
     *
     * @param format: Format
     * @param productId: Product id
     */
    public synchronized void browse(final StoFormat format, final String productId) {
        if (!enableGDPR) {
            StoLog.i(TAG, "browse call... " + productId);
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormatProduct(qspUpdate, stoQueryString, format, productId, "browse"))
                return;

            List<StoQueryStringParam> params = format.getQueryStringParamsList();

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.act);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.BrowseProduct);

            StoProduct product = StoCrawl.getInstance().getProductById(productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(product.getName()));

            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);

            qspUpdate.updatePositionObject(stoQueryString, format.getPosition() + 1);
            qspUpdate.updateByList(params, stoQueryString);

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * When a click on a StoFormat product view is triggered
     * @param stoFormat: Format
     * @param productId: Product id
     */
    public void openProductPage(final StoFormat stoFormat, final String productId) {
        if (!enableGDPR) {
            StoLog.i(TAG, "openProductPage call...");

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);
            if (checkFormatProduct(qspUpdate, stoQueryString, stoFormat, productId,"openProductPage"))
                return;

            List<StoQueryStringParam> params = stoFormat.getQueryStringParamsList();

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateByList(params, stoQueryString);

            StoProduct product = StoCrawl.getInstance().getProductById(productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(product.getName()));

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.act);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.OpenPdp);
            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);
            qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);
            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Click on the addList button in a product view of a Format
     *
     * @param stoFormat: Format
     * @param productId: Product id
     */
    public void addToList(final StoFormat stoFormat, final String productId) {
        if (!enableGDPR) {
            StoLog.i(TAG, "addToList call...");

            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormatProduct(qspUpdate, stoQueryString, stoFormat, productId,"addToList"))
                return;

            List<StoQueryStringParam> params = stoFormat.getQueryStringParamsList();

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateByList(params, stoQueryString);

            StoProduct product = StoCrawl.getInstance().getProductById(productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(product.getName()));

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.act);
            qspUpdate.updateQSPTrackEvent(stoQueryString, StoTrackEventValue.AddToList);
            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);
            qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);
            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Add / Add more|less / Change quantity of basket in a product view of a Format
     *
     * @param stoFormat: Format
     * @param productId: Product id
     * @param eventValue: Specific event (AbkBtn | QuantityChange | QuantityMore | QuantityLess)
     */
    public void basket(final StoFormat stoFormat, final String productId, final StoTrackEventValue eventValue) {
        if (!enableGDPR) {
            StoLog.i(TAG, "basket call...");
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormatProduct(qspUpdate, stoQueryString, stoFormat, productId, "basket"))
                return;

            List<StoQueryStringParam> params = stoFormat.getQueryStringParamsList();

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));
            qspUpdate.updateByList(params, stoQueryString);

            StoProduct product = StoCrawl.getInstance().getProductById(productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(product.getName()));

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.abk);
            qspUpdate.updateQSPTrackEvent(stoQueryString, eventValue);
            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);

            qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);

            Document documentXML = StoTrackerUtils.getXMLDocument(product, null);

            if (documentXML == null) {
                StoLog.e(TAG, "abk: documentXML is null!");
                this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, "basket: documentXML is null!");
                return;
            }

            StoLog.i(TAG, "abk: xml:" + StoTrackerUtils.toString(documentXML));

            stoQueryString.printQuery(true);
            stoRequestHandler.requestPost(stoQueryString, StoTrackerUtils.toString(documentXML));

        }
    }

    /**
     * When a Banner is clicked or the CTA Option of a Butterfly Format
     *
     * @param stoFormat: Format
     * @param optionType: Option type of format. Should be .redirection, .video or .pdf
     */
    public void openOption(final StoFormat stoFormat, final StoFormatOptionType optionType) {
        if (!enableGDPR) {
            StoLog.i(TAG, "openOption call...");
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (checkFormat(qspUpdate, stoQueryString, stoFormat, "openOption"))
                return;

            List<StoQueryStringParam> params = stoFormat.getQueryStringParamsList();

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));

            StoTrackActionValue action = null;
            StoTrackEventValue event = null;

            switch (optionType) {
                case REDIRECTION:
                    action = StoTrackActionValue.clk;
                    event = StoTrackEventValue.cta;
                    break;
                case VIDEO:
                    action = StoTrackActionValue.act;
                    event = StoTrackEventValue.OpenVideo;
                    break;
                case PDF:
                    action = StoTrackActionValue.act;
                    event = StoTrackEventValue.OpenPDF;
                    break;
                default:
            }

            if (action != null) {
                qspUpdate.updateByList(params, stoQueryString);
                qspUpdate.updateQSPTrackAction(stoQueryString, action);
                qspUpdate.updateQSPTrackEvent(stoQueryString, event);
                qspUpdate.updatePositionObject(stoQueryString, stoFormat.getPosition() + 1);
                stoQueryString.printQuery(true);
                stoRequestHandler.requestGet(stoQueryString);
            }
        }
    }

    /**
     * Has to be called, when the user clicks on the product
     *
     * Handles: fp
     *
     * @param productId   : required id of product
     * @param productLabel: required name of product
     */
    public synchronized void openNativeProductPage(final String productId, final String productLabel) {
        if (!enableGDPR) {
            StoLog.i(TAG, "openNativeProductPage call...");
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));

            if (checkNativeProduct(qspUpdate, stoQueryString, productId, productLabel, "fp"))
                return;

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.fp);
            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(productLabel));

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Has to be called, when the user add a native product to basket
     *
     * Handles: abp
     *
     * @param productId   : Required id of product
     * @param productLabel: Required name of product
     */
    public void nativeBasket(final String productId, final String productLabel) {
        if (!enableGDPR) {
            StoLog.d(TAG, "nativeBasket call...");
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));

            if (checkNativeProduct(qspUpdate, stoQueryString, productId, productLabel, "abp"))
                return;

            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.abp);
            qspUpdate.updateQSPTrackLabel(stoQueryString, productId);
            qspUpdate.updateQSPProductID(stoQueryString, productId);
            qspUpdate.updateQSPProductLabel(stoQueryString, StoTrackerUtils.getFormattedProductLabel(productLabel));

            stoQueryString.printQuery(true);
            stoRequestHandler.requestGet(stoQueryString);
        }
    }

    /**
     * Has to be called, when the user pays his basket
     *
     * Handles: buy
     *
     * @param purchaseOrderNumber : Required id of basket
     */
    public void buy(final String purchaseOrderNumber) {
        if (!enableGDPR) {
            StoQueryString stoQueryString = new StoQueryString(context);
            QSPUpdate qspUpdate = newQSPUpdate(stoQueryString);

            if (StoAppCommunicator.getAppRetriever() == null) {
                StoLog.e(TAG, "buy: StoAppCommunicator.appRetriever is null!");
                this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, "buy: StoAppCommunicator.appRetriever is null");
                return;
            }
            if (purchaseOrderNumber == null) {
                StoLog.w(TAG, "buy: purchaseOrderNumber is null!");
            }

            qspUpdate.updateQSPRetailPage(stoQueryString, "confirmed_purchase");
            qspUpdate.updateQSPTrackAction(stoQueryString, StoTrackActionValue.buy);
            qspUpdate.updateQSPTrackTime(stoQueryString, this.stoTrackerHelper.getTrackTime());
            qspUpdate.updateQSPSessionTime(stoQueryString, this.stoTrackerHelper.getSessionTime());
            qspUpdate.updateQSPSessionPages(stoQueryString, this.stoTrackerHelper.getSessionPages(stoQueryString.getQspRetailPage().getValue()));

            List<StoProduct>  productBasketList = StoAppCommunicator.getAppRetriever().stoGetBasketProducts();
            if (productBasketList == null || productBasketList.isEmpty()) {
                this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, "buy: basketProducts is null/empty");
                return;
            }

            Document documentXML = StoTrackerUtils.getXMLDocument(productBasketList, purchaseOrderNumber);
            if (documentXML == null) {
                StoLog.e(TAG, "buy: documentXML is null!");

                this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, "buy: documentXML is null!");
                return;
            }

            StoLog.i(TAG, "buy: xml:" + StoTrackerUtils.toString(documentXML));

            stoQueryString.printQuery(true);
            stoRequestHandler.requestPost(stoQueryString, StoTrackerUtils.toString(documentXML));
        }
    }

    private QSPUpdate newQSPUpdate(StoQueryString stoQueryString) {
        QSPUpdate qspUpdate = new QSPUpdate();
        qspUpdate.updateQSPRetailName(stoQueryString, retailName);
        qspUpdate.updateQSPRetailPage(stoQueryString, retailPage);
        qspUpdate.updateQSPRetailShop(stoQueryString, retailShop);
        qspUpdate.updateQSPVerifUID(stoQueryString, stoTrackerHelper.getVerifUID());
        qspUpdate.updateQSPForcedCity(stoQueryString, StoTrackerUtils.getFormattedForcedCity(forcedCity));
        qspUpdate.updateQSPForcedLatitude(stoQueryString, forcedLatitude);
        qspUpdate.updateQSPForcedLongitude(stoQueryString, forcedLongitude);
        qspUpdate.updateQSPOptinOut(stoQueryString, (enableGDPR) ? 0 : 1);
        qspUpdate.updateQSPTechBrowserV(stoQueryString, appVersion);
        StoPageType pageType = this.stoTrackerHelper.getStoPageType();
        qspUpdate.updateQSPPageType(stoQueryString, pageType.getDescription());

        return qspUpdate;
    }


    private boolean checkFormat(QSPUpdate qspUpdate, StoQueryString stoQueryString, StoFormat stoFormat, String event) {
        if (stoFormat == null) {
            StoLog.e(TAG, event + ": stoFormat is null!");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": stoFormat is null");
            return true;
        }

        if (stoFormat.getUniqueID() == null || stoFormat.getUniqueID().isEmpty()) {
            StoLog.e(TAG, event + ": uniqueId is null or empty!");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": object_id is null or empty!");
            return true;
        }

        if (stoFormat.getPosition() == -1) {
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": position of format is not set!");
            return true;
        }

        List<StoQueryStringParam> params = stoFormat.getQueryStringParamsList();
        if (params == null || params.isEmpty()) {
            StoLog.e(TAG, event + ": it's not a StoView because StoQueryStringParam list is null. No request will be sent!");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": it's not a StoView because StoQueryStringParam list is null. No request will be sent!");
            return true;
        }

        return false;
    }

    private boolean checkFormatProduct(QSPUpdate qspUpdate, StoQueryString stoQueryString, StoFormat stoFormat, String productId, String event) {
        if (checkFormat(qspUpdate, stoQueryString, stoFormat, event)) {
            return true;
        }

        if (productId == null || productId.isEmpty()) {
            StoLog.e(TAG, event + ": productId is null or empty");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": productId is null or empty");
            return true;
        }

        StoProduct product = StoCrawl.getInstance().getProductById(productId);

        if (product == null) {
            StoLog.e(TAG, event + ": product not found in StoCrawl");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": product not found in StoCrawl");
            return true;
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            StoLog.e(TAG, event + ": productName is null or empty");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": productName is null or empty");
            return true;
        }

        return false;
    }

    private boolean checkNativeProduct(QSPUpdate qspUpdate, StoQueryString stoQueryString, String productId, String productLabel, String event) {
        if (productId == null || productId.isEmpty()) {
            StoLog.e(TAG, event + ": productId is null or empty !");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": productId is null or empty !");
            return true;
        }
        if (productLabel == null || productLabel.isEmpty()) {
            StoLog.e(TAG, event + ": productName is null or empty !");
            this.stoErrorHandler.reportError(qspUpdate, stoQueryString, StoTrackEventValue.onDeliver, event + ": productName is null or empty !");
            return true;
        }
        return false;
    }
}
