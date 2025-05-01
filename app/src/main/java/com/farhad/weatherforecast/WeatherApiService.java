package com.farhad.weatherforecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("forecast")
    Call<WeatherResponse> getWeatherData(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
