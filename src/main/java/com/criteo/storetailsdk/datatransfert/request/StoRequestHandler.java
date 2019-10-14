package com.criteo.storetailsdk.datatransfert.request;


import android.content.Context;
import com.google.gson.JsonObject;
import com.criteo.storetailsdk.datatransfert.query.StoQueryString;
import com.criteo.storetailsdk.datatransfert.response.StoResponseHandler;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.StoTrackerHelper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikhailPOGORELOV on 18/05/2018.
 */
public class StoRequestHandler {

    private static final String TAG = "StoRequestHandler";


    private static StoRequestHandler instance = null;


    private final String base_url = "https://tk.storetail.io";
    private StoApi stoApi;

    private List<Call> callList;

    private StoResponseHandler stoResponseHandler;


    private StoRequestHandler(Context context, StoTrackerHelper trackerHelper) {
        this.stoResponseHandler = StoResponseHandler.getInstance(context, trackerHelper);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        client.addInterceptor(loggingInterceptor);

        String userAgent = System.getProperty("http.agent");
        client.networkInterceptors().add(new StoUserAgentInterceptor(userAgent));

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url).
                client(client.build()).
                addConverterFactory(GsonConverterFactory.create()).
                build();


        callList = new ArrayList<>();
        this.stoApi = retrofit.create(StoApi.class);
    }


    public static StoRequestHandler getInstance(Context context, StoTrackerHelper trackerHelper) {
        synchronized (StoRequestHandler.class) {
            if (instance == null) {
                instance = new StoRequestHandler(context, trackerHelper);
            }
        }
        return instance;
    }

    public synchronized void requestGet(final StoQueryString queryString) {
        requestGet(queryString, null);
    }

    public synchronized void requestGet(final StoQueryString queryString, final String identifier) {
        if (queryString != null) {
            final String query = new String(queryString.getQueryString().toCharArray());
            final String trackActionValue = queryString.getQspTrackAction().getValue();

            final Call<JsonObject> call = stoApi.requestGet(queryString.getQueryStringParamsMap());

            callList.add(call);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        stoResponseHandler.handleResponse(response.body().toString(), query, trackActionValue, identifier);
                    } else {
                        StoLog.e(TAG, "requestGet: response's body is null!");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    stoResponseHandler.handleFailure(t.getMessage(), identifier);
                }
            });
        } else {
            StoLog.e(TAG, "requestGet: stoQueryString is null!");
        }
    }


    public synchronized void requestPost(StoQueryString queryString, String xmlString) {
        if (queryString != null && xmlString != null) {
            StoLog.d(TAG, "requestPost: body: " + xmlString);
            final String query = new String(queryString.getQueryString().toCharArray());
            final String trackActionValue = queryString.getQspTrackAction().getValue();

            final RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("text/plain; charset=utf-8"), "bk=" + xmlString);

            final Call<JsonObject> call = stoApi.requestPost(queryString.getQueryStringParamsMap(), requestBody);

            callList.add(call);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        stoResponseHandler.handleResponse(response.body().toString(), query, trackActionValue, null);
                    } else {
                        StoLog.e(TAG, "requestPost: response's body is null!");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    stoResponseHandler.handleFailure(t.getMessage(), null);
                }
            });
        } else {
            StoLog.e(TAG, "requestPost: queryString or body is null!");
        }
    }

    /**
     * Stops the requests
     */
    public void stop() {
        for (Call call : callList) {
            call.cancel();
        }
    }
}
