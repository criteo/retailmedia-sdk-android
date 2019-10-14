package com.criteo.storetailsdk.extras;

import com.criteo.storetailsdk.extras.interfaces.*;

/**
 * StoAppCommunicator is using a class of the Application that makes operation on products
 */

public class StoAppCommunicator {

    private static StoRetrieverInterface appRetriever = null;

    public static synchronized StoRetrieverInterface getAppRetriever() {
        return appRetriever;
    }

    public static void registerRetriever(StoRetrieverInterface stoRetrieverInterface) {
        appRetriever = stoRetrieverInterface;
    }
}
