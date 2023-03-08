package com.pyxis.compass.gpscompassnavigationmap.activity.weatherfull;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddressUtils {
    private Context context;
    private Geocoder geocoder;

    public AddressUtils(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context);
    }

    public String getLocationNameCompass(LatLng latLng) {
        String name = "Waiting";
        try {
            List<Address> addresses = this.geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                return ((Address) addresses.get(0)).getAddressLine(0);
            }
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return name;
        }
    }
}
