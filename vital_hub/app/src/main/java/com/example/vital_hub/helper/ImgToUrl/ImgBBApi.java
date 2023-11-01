package com.example.vital_hub.helper.ImgToUrl;

import com.example.vital_hub.client.spring.objects.*;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImgBBApi {
    @FormUrlEncoded
    @POST("1/upload")
    Call<UploadResponse> uploadImage(
            @Field("key") String apiKey,
            @Field("image") String base64Image
    );
}