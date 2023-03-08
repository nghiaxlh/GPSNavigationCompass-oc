package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull.forecast7day.interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    public static String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/";
    public static String API_XU_WEATHER = "http://api.apixu.com/v1/";
    public static String API_DARK_SKY="https://api.darksky.net/forecast/";
    public static Retrofit currentRetrofit;
    public static Retrofit forecastRetrofit;
    private static Retrofit forecastHourly;

    public static Retrofit getRestClient() {
        if (currentRetrofit == null) {
            currentRetrofit = new Retrofit.Builder().baseUrl(OPEN_WEATHER_MAP_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return currentRetrofit;

    }

    public static Retrofit getApiXu() {
        if (forecastRetrofit == null) {

            forecastRetrofit = new Retrofit.Builder().baseUrl(API_XU_WEATHER).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return forecastRetrofit;
    }
    public static Retrofit getApiDark(){
        if (forecastHourly==null){

            forecastHourly=new Retrofit.Builder().baseUrl(API_DARK_SKY).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return forecastHourly;
    }

    public static ApiInterFace getApiInterFace() {
        return getRestClient().create(ApiInterFace.class);
    }

    public static AapixuInterface getForeCastInter() {
        return getApiXu().create(AapixuInterface.class);
    }

    public static ApiDarkSky getForeCastHour() {
        return getApiDark().create(ApiDarkSky.class);
    }

    public static ApiDarkDaily getForeCastDaily(){
        return getApiDark().create(ApiDarkDaily.class);
    }



}
