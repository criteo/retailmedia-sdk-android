package com.criteo.storetailsdk.extras;

import java.util.ArrayList;
import java.util.List;

public class StoEventCommunicator {

    private static List<StoFormatsListener> formatsListeners = new ArrayList<>();


    public static synchronized void addStoFormatsListener(StoFormatsListener stoFormatsListener) {
        formatsListeners.add(stoFormatsListener);
    }

    public static synchronized void removeStoFormatsListener(StoFormatsListener stoFormatsListener) {
        formatsListeners.remove(stoFormatsListener);
    }

    public static synchronized List<StoFormatsListener> getStoFormatsListeners() {
        return formatsListeners;
    }

    public static synchronized boolean hasStoFormatsListeners() {
        return !formatsListeners.isEmpty();
    }
}
