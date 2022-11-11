package com.example.zhulinaapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @POST("ModelWorkouts/")
    Call<Mask> createPost(@Body Mask mask);

    @PUT("ModelWorkouts/{id}")
    Call<Mask> updateData(@Query("id") int id, @Body Mask mask);

    @DELETE("ModelWorkouts/{id}")
    Call<Mask> deleteData(@Query("id") int id);

}