package com.pyxis.compass.gpscompassnavigationmap.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pyxis.compass.gpscompassnavigationmap.BaseActivity;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.SensorManagerCompass;
import com.pyxis.compass.gpscompassnavigationmap.listener.GoogleMapListener;
import com.pyxis.compass.gpscompassnavigationmap.presenter.GoogleMapPre;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobAdsUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.GpsCompassUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobUtils;
import com.pyxis.compass.gpscompassnavigationmap.view.CustomImageCompassView;
import com.pyxis.compass.gpscompassnavigationmap.view.CustomLineView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

public class GoogleMapsActivity extends BaseActivity<GoogleMapPre> implements OnMapReadyCallback, SensorEventListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMapListener, View.OnClickListener {

    private static final String TAG = GoogleMapsActivity.class.getSimpleName();
    private static final float MAPS_DEFAULT_ZOOM = 16f;
    private CustomImageCompassView mCustomImageCompassView;
    private GoogleMap mGoogleMap;
    private UiSettings mUiSettings;
    private Marker mMarker;
    private SensorManagerCompass mSensorManagerCompass;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private float[] mAccel = new float[]{0f, 0f, 9.8f};
    private float[] mMagnetic = new float[]{0.5f, 0f, 0f};
    private float mAzimuth;
    private Location location;
    private ImageView ivBackGoogleMaps;
    private ImageButton btnMyLocationGoogleMaps;
    private ImageButton btnRotateGoogleMaps;
    private ImageButton btnCompassGoogleMaps;
    private EditText edtAutoComplete;
    private CustomLineView mCustomLineView;
    private boolean isTurnOnRotate, isTurnOnCompass;

    @Override
    protected GoogleMapPre createPresenter() {
        return new GoogleMapPre(this);
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_google_maps);
//        ShowAdsInter();
        AdmobUtils.showInter(this);
        initView();
        mCustomImageCompassView = findViewById(R.id.iv_compass_google_map);
        ivBackGoogleMaps = findViewById(R.id.iv_back_google_map);
        edtAutoComplete = findViewById(R.id.edt_search_google_map);
        btnRotateGoogleMaps = findViewById(R.id.btn_rotate_google_map);
        btnMyLocationGoogleMaps = findViewById(R.id.btn_rotate_google_map);
        btnCompassGoogleMaps = findViewById(R.id.btn_compass_google_map);
        mCustomLineView = findViewById(R.id.line_view_google_map);

        ivBackGoogleMaps.setOnClickListener(this);
        edtAutoComplete.setOnClickListener(this);
        btnRotateGoogleMaps.setOnClickListener(this);
        btnMyLocationGoogleMaps.setOnClickListener(this);
        btnCompassGoogleMaps.setOnClickListener(this);

        mSensorManagerCompass = new SensorManagerCompass(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
        mapFragment.getMapAsync(this);
    }

    private void ShowAdsInter() {
        AdmobAdsUtils.getSharedInstance().showInterstitialAd(new AdmobAdsUtils.AdCloseListener() {
            @Override
            public void onAdClosed() {
            }
        });
    }

    private void initView() {
        isTurnOnRotate = true;
        isTurnOnCompass = true;
    }

    private void getRequestLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2 * 1000);
        mLocationRequest.setFastestInterval(2 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mGoogleMap = googleMap;
        getRequestLocation();
        mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);
        getDeviceLocation();
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            List<Location> locationList = locationResult.getLocations();
            Log.d(TAG, "mLocationCallback()");
            for (Location location : locationList) {
                GoogleMapsActivity.this.location = location;
                if (mMarker != null) {
                    mMarker.remove();
                }
                MarkerOptions mOptions = new MarkerOptions();
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_map));
                mOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                mOptions.anchor(0.5f, 0.5f);
                mOptions.flat(true);
                mMarker = mGoogleMap.addMarker(mOptions);

            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManagerCompass.registerAccelerometerListener(this);
        mSensorManagerCompass.registerMagneListener(this);
        mSensorManagerCompass.registerOriListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManagerCompass.unregisterListener(this);
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccel = GpsCompassUtils.lowPass(sensorEvent.values, mAccel);
            mSensorManagerCompass.setGravity(mAccel);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagnetic = GpsCompassUtils.lowPass(sensorEvent.values,
                    mMagnetic);
            mSensorManagerCompass.setGeoMagnetic(mMagnetic);
        }

        mSensorManagerCompass.loadAzimuth();
        float newAzimuth = mSensorManagerCompass.getAzimuthSensor();
        if (mAzimuth != newAzimuth) {
            mAzimuth = newAzimuth;
            if (mMarker != null) {
                mMarker.setRotation(mAzimuth);
            }
            if (isTurnOnRotate) {
                if (isTurnOnCompass) {
                    mCustomImageCompassView.setDegress(-mAzimuth);
                }
                mPresenter.getRotateCamera(mAzimuth);
            } else {
                mCustomImageCompassView.setDegress(0);
            }
            mCustomImageCompassView.invalidate();
        }
    }

    @Override
    public void getRotateCamera(float azimuth) {
        if (mGoogleMap == null || location == null) {
            return;
        }
        Log.d(TAG, "getRotateCamera()");
        CameraPosition oldPos = mGoogleMap.getCameraPosition();
        CameraPosition pos = CameraPosition.builder(oldPos).target(new LatLng(location.getLatitude(),
                location.getLongitude()))
                .bearing(azimuth)
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onMyLocationButtonClick() {
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    GoogleMapsActivity.this.location = location;
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(GoogleMapsActivity.this.location.getLatitude(),
                                    GoogleMapsActivity.this.location.getLongitude()), MAPS_DEFAULT_ZOOM));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_google_map:
                finish();
                break;
            case R.id.edt_search_google_map:
                findPlace();
                break;
            case R.id.btn_rotate_google_map:
                if (isTurnOnRotate) {
                    getTurnOffRotateGoogleMap();
                } else {
                    getTurnOnRotateGoogleMap();
                }
                break;
            case R.id.btn_my_location:
                getDeviceLocation();
                break;
            case R.id.btn_compass_google_map:
                if (isTurnOnCompass) {
                    getTurnOffCompass();
                } else {
                    getTurnOnCompass();
                }
                break;
        }
    }

    private void getTurnOffCompass() {
        isTurnOnCompass = false;
        mCustomImageCompassView.setVisibility(View.INVISIBLE);
        mCustomLineView.setVisibility(View.INVISIBLE);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        btnCompassGoogleMaps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_compass_off));
    }

    private void getTurnOnCompass() {
        isTurnOnCompass = true;
        mCustomImageCompassView.setVisibility(View.VISIBLE);
        mCustomLineView.setVisibility(View.VISIBLE);
        btnCompassGoogleMaps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_compass_on));
    }

    private void getTurnOnRotateGoogleMap() {
        isTurnOnRotate = true;
        btnRotateGoogleMaps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_button_auto_rotate_on));
    }

    private void getTurnOffRotateGoogleMap() {
        isTurnOnRotate = false;
        mPresenter.getRotateCamera(0);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        btnRotateGoogleMaps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_button_auto_rotate_off));
    }

    public void findPlace() {
        try {
            startActivityForResult(new PlaceAutocomplete.IntentBuilder(2).build(this), 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e2) {
            e2.printStackTrace();
        }
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1) {
            return;
        }
        if (resultCode == -1) {
            this.mGoogleMap.clear();
            Place place = PlaceAutocomplete.getPlace(this, data);
            LatLng latNeed = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            this.edtAutoComplete.setText(place.getName());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latNeed, 15.0f);
            this.mGoogleMap.addMarker(new MarkerOptions().title(place.getName() + "").snippet(place.getAddress() + "").position(latNeed));
            this.mGoogleMap.animateCamera(cameraUpdate);
            //route(this.latCurrent, latNeed);
        } else if (resultCode == 2) {
            Log.e(TAG, PlaceAutocomplete.getStatus(this, data).getStatusMessage());
        } else if (resultCode != 0) {
        }
    }
}
