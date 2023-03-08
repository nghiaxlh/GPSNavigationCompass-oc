package com.pyxis.compass.gpscompassnavigationmap.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.pyxis.compass.gpscompassnavigationmap.BaseActivity;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.listener.LocationListener;
import com.pyxis.compass.gpscompassnavigationmap.presenter.LocationPre;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobAdsUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobUtils;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class LocationActivity extends BaseActivity<LocationPre> implements LocationListener, OnMapReadyCallback {
    private Toolbar mToolbar;
    private TextView mTvLocation;
    private TextView mTvLat;
    private TextView mTvLong;
    private GoogleMap mGoogleMaps;
    private UiSettings mUiSettings;
//    private AdView nativeExpressAdView;
    private Location mLocation;
    private double mLatitude, mLongitude;
    private String mFullLocation;
    private float GOOGLE_MAPS_DEFAULT_ZOOM = 15f;
    private LinearLayout llShareLocation;

    @Override
    protected LocationPre createPresenter() {
        return new LocationPre(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        //ShowAdsInter();
        AdmobUtils.showInter(this);
        init();
//        admobBannerUnit();
    }
    private void ShowAdsInter(){
        AdmobAdsUtils.getSharedInstance().showInterstitialAd(new AdmobAdsUtils.AdCloseListener() {
            @Override
            public void onAdClosed() {
            }
        });
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar_location);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.text_location_city);

        mTvLocation = findViewById(R.id.tv_full_location);
        mTvLat = findViewById(R.id.tv_lat_location);
        mTvLong = findViewById(R.id.tv_long_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.address_map_view);
        mapFragment.getMapAsync(this);
//        nativeExpressAdView = findViewById(R.id.ads_banner_address);
        mLocation = getIntent().getParcelableExtra(AppConstantUtils.LOCATION_EXTRA);
        mFullLocation = getIntent().getStringExtra(AppConstantUtils.LOCATION_STRING_EXTRA);
        mLatitude = mLocation.getLatitude();
        mLongitude = mLocation.getLongitude();
        mPresenter.setTextAll(mFullLocation, mLatitude, mLongitude);

        llShareLocation = findViewById(R.id.ll_compass_share_location);
        llShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = mLocation;
                if (location != null) {
                    String mA = String.format(Locale.US, "%f", location.getLatitude()).replace(",", ".");
                    String mB = String.format(Locale.US, "%f", location.getLongitude()).replace(",", ".");
                    mB = String.format(Locale.US, "http://www.google.com/maps/place/%s,%s", mA, mB);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", mB);
                    startActivity(Intent.createChooser(intent, "Share Location"));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMaps = googleMap;
        mUiSettings = mGoogleMaps.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude,
                mLongitude), GOOGLE_MAPS_DEFAULT_ZOOM));
    }

    @Override
    public void setFullTextLocation(String addressFull, double latitude, double longitude) {
        mTvLocation.setText(addressFull);
        mTvLat.setText(String.valueOf(latitude));
        mTvLong.setText(String.valueOf(longitude));
    }

    private void admobBannerUnit() {
        final AdView mAdView = new AdView(this);
        final AdRequest request = new AdRequest.Builder().build();
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_id_1));
//        nativeExpressAdView.addView(mAdView);
        mAdView.loadAd(request);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }
        });
    }
}
