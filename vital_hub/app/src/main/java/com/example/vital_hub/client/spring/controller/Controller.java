package com.example.vital_hub.client.spring.controller;

import com.example.vital_hub.client.spring.objects.*;
import com.example.vital_hub.client.spring.objects.AuthResponseObject;
import com.example.vital_hub.client.spring.objects.CountResponse;
import com.example.vital_hub.client.spring.objects.FriendListResponse;
import com.example.vital_hub.client.spring.objects.RegistRequestObject;
import com.example.vital_hub.client.spring.objects.ResponseObject;
import com.example.vital_hub.exercises.data_container.GroupExercise;
import com.example.vital_hub.exercises.data_container.SingleExercise;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    @GET("/friend/request-list")
    Call<FriendListResponse> getFriendRequestList(@HeaderMap Map<String, String> headers, @Query("limit") Integer limit, @Query("offset") Integer offset);
    @GET("/user/search")
    Call<FriendListResponse> getSearchList(@HeaderMap Map<String, String> headers, @Query("name") String name, @Query("limit") Integer limit, @Query("offset") Integer offset);
    @POST("/friend/add")
    Call<Void> addFriend(@HeaderMap Map<String, String> headers, @Query("id") Long id);
    @PUT("/friend/accept")
    Call<Void> acceptRequest(@HeaderMap Map<String, String> headers, @Query("id") Long id);
    @DELETE("/friend/deny")
    Call<Void> denyRequest(@HeaderMap Map<String, String> headers, @Query("id") Long id);
    @DELETE("/friend/revoke")
    Call<Void> revokeRequest(@HeaderMap Map<String, String> headers, @Query("id") Long id);
    @DELETE("/friend/delete")
    Call<Void> deleteFriend(@HeaderMap Map<String, String> headers, @Query("id") Long id);

    // /competition/**

    @GET("/competition/list")
    Call<CompetitionListResponse> getCompetitionList(@HeaderMap Map<String, String> headers,@Query("isJoined") Boolean isJoined, @Query("name") String name, @Query("limit") Integer limit, @Query("offset") Integer offset);
    @GET("/competition/own-list")
    Call<CompetitionListResponse> getOwnCompetitionList(@HeaderMap Map<String, String> headers, @Query("name") String name, @Query("limit") Integer limit, @Query("offset") Integer offset);
    // /auth/**
    @POST("/auth/create-user-first-sign")
    Call<Void> postRegistInfo(@HeaderMap Map<String, String> header, @Body RegistRequestObject body);

    // /workout/**
    @GET("/workout/exercise-groups")
    Call<List<GroupExercise>> getGroupExerciseAll(@HeaderMap Map<String, String> header, @Query("suggest") Boolean suggest);

    @GET("/workout/all-exercises")
    Call<List<SingleExercise>> getSingleExercisePartial(@HeaderMap Map<String, String> header, @Query("page") Integer page, @Query("pageSize") Integer pageSize, @Query("order") String order, @Query("desc") Boolean desc);

    @GET("/workout/{id}")
    Call<SingleExercise> getSingleExerciseById(@HeaderMap Map<String, String> header, @Path("id") Long id);

    @GET("/workout/group/{id}")
    Call<List<SingleExercise>> getGroupExerciseById(@HeaderMap Map<String, String> header, @Path("id") Long id);

}