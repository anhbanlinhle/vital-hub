package com.example.vital_hub.client.controller;

import com.example.vital_hub.client.objects.*;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Controller {
    // /health/crud-single
    // /health/crud-many
    @GET("/health/get-single")
    Call<ResponseObject> getResponseObject(@HeaderMap Map<String, String> headers);

    @GET("/health/get-many")
    Call<List<ResponseObject>> getResponseObjects(@HeaderMap Map<String, String> headers);

    @POST("/health/post-single")
    Call<ResponseObject> postResponseObject(@HeaderMap Map<String, String> headers, @Body ResponseObject object);

    @PUT("/health/put-single")
    Call<ResponseObject> putResponseObject(@HeaderMap Map<String, String> headers, @Body ResponseObject object);

    @GET("/user/test-header")
    Call<ResponseObject> getHeader(@HeaderMap Map<String, String> headers);

    @GET("/auth/sign-in")
    Call<AuthResponseObject>  sendAccessToken(@HeaderMap Map<String, String> headers);

    // /friend/**

    @GET("/friend/total")
    Call<CountResponse> getTotalFriends(@HeaderMap Map<String, String> headers);
    @GET("/friend/list")
    Call<FriendListResponse> getFriendList(@HeaderMap Map<String, String> headers, @Query("name") String name, @Query("limit") Integer limit, @Query("offset") Integer offset);

    @POST("/auth/create-user-first-sign")
    Call<Void> postRegistInfo(@HeaderMap Map<String, String> header, @Body RegistRequestObject body);
}
