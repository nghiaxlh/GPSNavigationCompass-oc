package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull;

import com.pyxis.compass.gpscompassnavigationmap.R;

public class ApiKey {

    public static String QUERY(double lat, double lon) {
        return "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=" + R.string.API_WEATHER_KEY;
    }
}