package com.example.vital_hub.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Api {
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    static Controller controller = retrofit.create(Controller.class);

    public static Call<ResponseObject> getSingle = controller.getResponseObject();
    public static Call<List<ResponseObject>> getMultiple = controller.getResponseObjects();
    public static Call<ResponseObject> postRequest;
    public static Call<ResponseObject> putRequest;
    public static Call<ResponseObject> getHeader;

    public static void initPost(ResponseObject object) {
        postRequest = controller.postResponseObject(object);
    }

    public static void initPut(ResponseObject object) {
        putRequest = controller.putResponseObject(object);
    }

    public static void initGetHeader(Map<String, String> headers) {
        getHeader = controller.getHeader(headers);
    }

}
