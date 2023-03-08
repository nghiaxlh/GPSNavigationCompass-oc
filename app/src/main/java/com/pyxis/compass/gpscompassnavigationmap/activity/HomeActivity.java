package com.pyxis.compass.gpscompassnavigationmap.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.pyxis.compass.gpscompassnavigationmap.BaseActivity;
import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.dialog.DialogCheckInternet;
import com.pyxis.compass.gpscompassnavigationmap.dialog.DialogPolicyApp;
import com.pyxis.compass.gpscompassnavigationmap.fragment.HomeFragment;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class HomeActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION = 200;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String[] NAME_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private HomeFragment mHomeFragment;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AdmobUtils.loadAdInter(this);
        AdmobUtils.setLoadDataListener(new AdmobUtils.LoadDataListener() {
            @Override
            public void onLoadSuccess() {

            }
        });
        int state = AppConstantUtils.CheckNetwork(HomeActivity.this);
        if (state == AppConstantUtils.TYPE_NOT_INTERNET) {
            new DialogCheckInternet(HomeActivity.this).show();
        }
        DialogPolicyApp.showDialogPolicy(this, getSupportFragmentManager());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            getRequestLocationPermission();
        } else {
            init();
        }
    }

    private void init() {
        mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.Frame_layout_content);
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.Frame_layout_content, mHomeFragment).commitAllowingStateLoss();
        }
//        AdmobAdsUtils.getSharedInstance().init(getApplicationContext());
    }

    private void getRequestLocationPermission() {
        ActivityCompat.requestPermissions(this, NAME_PERMISSION, REQUEST_PERMISSION);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
