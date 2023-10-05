package com.example.vital_hub.client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Controller {
    // /health/crud-single
    // /health/crud-many
    @GET("/user/test/list-user")
    Call<List<ResponseObject>> getResponseObject();
}
