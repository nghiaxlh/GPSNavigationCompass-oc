package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.interfaces;

import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.model.ForecastModel;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.modelweather.CurrentWeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterFace {
    @GET("weather?type=accurate&units=metric&lang=vi")
    Call<CurrentWeatherModel> getCurrentWeather(@Query("q") String q, @Query("appid") String appid);

    @GET("forecast.json?days=7")
    Call<List<ForecastModel>> getForeCast(@Query("key") String key, @Query("q") String q);


}
