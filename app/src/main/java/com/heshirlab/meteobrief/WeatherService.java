package com.heshirlab.meteobrief;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherService {

    private static WeatherService mesInstance;
    private final Retrofit mesRetrofit;
    private static final String BASE_URL = "http://api.openweathermap.org";


    private WeatherService() {
        mesRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static WeatherService getInstance() {
        if (mesInstance == null) {
            mesInstance = new WeatherService();
        }
        return mesInstance;
    }

    public MessagesApi getMessagesApi() {
        return mesRetrofit.create(MessagesApi.class);
    }

}
