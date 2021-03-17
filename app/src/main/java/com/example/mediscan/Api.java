package com.example.mediscan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("/v1/discover")
    Call<Object> getModels(
            @Query("at") String location,
            @Query("q") String type,
            @Query("apikey") String api,
            @Query("limit") int limit);

}
