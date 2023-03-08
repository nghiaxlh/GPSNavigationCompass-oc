package com.pyxis.compass.gpscompassnavigationmap.presenter;

import android.hardware.SensorEvent;

import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.listener.CompassListener;

public class CompassHomePre extends BasePresenter<CompassListener> {
    public CompassHomePre(CompassListener view) {
        super.getAttachView(view);
    }

    public void changeDirection(SensorEvent sensorEvent) {
        mView.setChangeDirectionCompass(sensorEvent);
    }

    public void setTextLocation(double latitude, double longitude) {
        mView.setTextForLocation(latitude, longitude);
    }

    public void openViewGoogleMaps() {
        mView.showViewGoogleMapsCompass();
    }

    public void calibrateCompass(int calibrate) {
        mView.getCalibrate(calibrate);
    }

    public void openWarning() {
        mView.showWarningCompass();
    }

    public void openLocation() {
        mView.showLocationCompass();
    }

    public void showIconLocation() {
        if (mView != null) {
            mView.showIconLocation();
        }
    }
}
