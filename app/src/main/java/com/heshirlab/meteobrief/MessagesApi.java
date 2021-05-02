package com.heshirlab.meteobrief;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MessagesApi {

    @GET("/data/2.5/weather?&units=metric&appid=56d4ee9c40dc59f6833c5544f6a35e1d")
    Call<PostPojo> getWeatherForCity(@Query("lat") double latitude, @Query("lon") double longitude);
}




