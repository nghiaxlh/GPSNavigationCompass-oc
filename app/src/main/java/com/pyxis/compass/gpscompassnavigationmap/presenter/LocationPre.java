package com.pyxis.compass.gpscompassnavigationmap.presenter;

import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.listener.LocationListener;

public class LocationPre extends BasePresenter<LocationListener> {

    public LocationPre(LocationListener view) {
        super.getAttachView(view);
    }

    public void setTextAll(String addressFull, double latitude, double longitude) {
        mView.setFullTextLocation(addressFull, latitude, longitude);
    }
}
