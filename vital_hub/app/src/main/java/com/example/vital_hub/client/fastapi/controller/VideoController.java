package com.example.vital_hub.client.fastapi.controller;

import com.example.vital_hub.client.fastapi.objects.PushUpResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoController {
    @Multipart
    @POST("pushup")
    Call<PushUpResponse> sendVideo(@Part MultipartBody.Part video);
}
