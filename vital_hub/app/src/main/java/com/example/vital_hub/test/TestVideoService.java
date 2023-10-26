package com.example.vital_hub.test;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TestVideoService {
    @Multipart
    @POST("pushup")
    Call<ServerResponse> sendVideo(@Part MultipartBody.Part video);

    static class ServerResponse {
        @SerializedName("count")
        private int count;

        public int getCount() {
            return count;
        }
    }
}
