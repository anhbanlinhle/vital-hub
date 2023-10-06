package com.example.vital_hub.client.controller;

import com.example.vital_hub.client.objects.ResponseObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Controller {
    // /health/crud-single
    // /health/crud-many
    @GET("/health/get-single")
    Call<ResponseObject> getResponseObject();

    @GET("/health/get-many")
    Call<List<ResponseObject>> getResponseObjects();

    @POST("/health/post-single")
    Call<ResponseObject> postResponseObject(@Body ResponseObject object);

    @PUT("/health/put-single")
    Call<ResponseObject> putResponseObject(@Body ResponseObject object);

    @GET("/user/test-header")
    Call<ResponseObject> getHeader(@HeaderMap Map<String, String> headers);
}
