package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.interfaces;

import com.google.gson.JsonElement;
import com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.model.ForecastModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AapixuInterface {
    @GET("forecast.json?days=7")
    Call<ForecastModel> getForeCast(@Query("key") String key, @Query("q") String q);

    @GET("forecast.json?days=7")
    Call<JsonElement> getJsonForecast(@Query("key") String key, @Query("q") String q);
}
