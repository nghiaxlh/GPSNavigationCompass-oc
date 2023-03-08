package com.pyxis.compass.gpscompassnavigationmap.presenter;

import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.listener.GoogleMapListener;

public class GoogleMapPre extends BasePresenter<GoogleMapListener> {
    public GoogleMapPre(GoogleMapListener googleMapsListener) {
        super.getAttachView(googleMapsListener);
    }

    public void getRotateCamera(float mAzimuth) {
        mView.getRotateCamera(mAzimuth);
    }
}
