package com.example.vital_hub.client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Controller {
    // /health/crud-single
    // /health/crud-many
    @GET("/health/get-single")
    Call<ResponseObject> getResponseObject();

    @GET("/health/get-many")
    Call<List<ResponseObject>> getResponseObjects();

    @POST("/health/post-single")
    Call<ResponseObject> postResponseObject(@Body ResponseObject object);
}
