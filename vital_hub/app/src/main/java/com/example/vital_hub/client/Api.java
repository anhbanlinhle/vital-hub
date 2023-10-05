package com.example.vital_hub.client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Api {
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static Controller controller = retrofit.create(Controller.class);

    public static Call<ResponseObject> getSingle = controller.getResponseObject();
    public static Call<List<ResponseObject>> getMultiple = controller.getResponseObjects();
    public static Call<ResponseObject> postRequest;

    public static void initPost(ResponseObject object) {
        postRequest = controller.postResponseObject(object);
    }

}
