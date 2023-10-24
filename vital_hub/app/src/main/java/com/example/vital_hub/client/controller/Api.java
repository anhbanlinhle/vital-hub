package com.example.vital_hub.client.controller;

import com.example.vital_hub.client.objects.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HeaderMap;

public class Api {
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    static Controller controller = retrofit.create(Controller.class);

    public static Call<ResponseObject> getSingle;
    public static Call<List<ResponseObject>> getMultiple;
    public static Call<ResponseObject> postRequest;
    public static Call<ResponseObject> putRequest;
    public static Call<ResponseObject> getHeader;
    public static Call<AuthResponseObject> getJwt;
    public static Call<Void> postRegist;

    //Friend
    public static Call<CountResponse> getTotalFriend;
    public static Call<FriendListResponse> getFriendList;

    //User profile
    public static Call<ProfileResponse> getUserProfile;
    //Others profile
    public static Call<ProfileResponse> getOthersProfile;

    //Init
    public static void initGetSingle(Map<String, String> headers) {
        getSingle = controller.getResponseObject(headers);
    }

    public static void initGetMultiple(Map<String, String> headers) {
        getMultiple = controller.getResponseObjects(headers);
    }

    public static void initPost(@HeaderMap Map<String, String> headers, ResponseObject object) {
        postRequest = controller.postResponseObject(headers, object);
    }

    public static void initPut(@HeaderMap Map<String, String> headers, ResponseObject object) {
        putRequest = controller.putResponseObject(headers, object);
    }

    public static void initGetHeader(Map<String, String> headers) {
        getHeader = controller.getHeader(headers);
    }

    public static void initJwt(Map<String, String> headers) {
        getJwt = controller.sendAccessToken(headers);
    }

    //Friend
    public static void initGetTotalFriend(Map<String, String> headers) {
        getTotalFriend = controller.getTotalFriends(headers);
    }

    public static void initGetFriendList(Map<String, String> headers, String name, Integer limit, Integer offset) {
        getFriendList = controller.getFriendList(headers, name, limit, offset);
    }
    public static void initPostRegist(Map<String, String> headers, RegistRequestObject body) {
        postRegist = controller.postRegistInfo(headers, body);
    }

    //User profile
    public static void initGetUserProfile(Map<String, String> headers) {
        getUserProfile = controller.getUserProfile(headers);
    }
    //Others profile
    public static void initGetOthersProfile(Map<String, String> headers, Long id) {
        getOthersProfile = controller.getOthersProfile(headers, id);
    }
}
