package com.example.mausam;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?appid=3ae1ef9d907b6355e035ffca086ee245&units=metric")
    Call<OpenWeatherMap> getWeatherWithCityName(@Query("q")String name);

    @GET("weather?appid=3ae1ef9d907b6355e035ffca086ee245&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lon);
}
