package com.criteo.storetailsdk.datatransfert.request;

import com.criteo.storetailsdk.logs.StoLog;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class StoUserAgentInterceptor implements Interceptor{

    private final String TAG= "StoUserAgentInterceptor";

    private final String userAgent;

    public StoUserAgentInterceptor(String userAgent){
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build();
        StoLog.d(TAG,"intercept:" + userAgent);

        return chain.proceed(requestWithUserAgent);
    }
}
