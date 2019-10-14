package com.criteo.storetailsdk.datatransfert.request;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface StoApi {


    @GET("/x?")
    Call<JsonObject> requestGet(@QueryMap Map<String, String> queryStringParams);

    @POST("/x?")
    Call<JsonObject> requestPost(@QueryMap Map<String, String> queryStringParams, @Body RequestBody xml);
}