package com.facebook.msdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;

public class NewActivity extends Activity {
    private static Activity mActivity;
    private AdRequest mAdRequest;
    private InterstitialAd mInterAdmob;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#F324E4A0"));
        }
        mActivity = this;
        loadAdsAdmob(this);
        new CountDownTimer(30000, 1000) {
            public void onFinish() {
                finish();
            }

            public void onTick(long j) {
            }
        }.start();
    }

    private void loadAdsAdmob(final Context context) {
        mInterAdmob = new InterstitialAd(context);
        mInterAdmob.setAdUnitId(context.getString(R.string.o_interstitial_id_1));
        mAdRequest = new AdRequest.Builder().build();
        mInterAdmob.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                SharedPreferences share = getSharedPreferences(AppConstantUtils.FILE_DATA_APP, 0);
                share.edit().putInt(AppConstantUtils.TIME_ADS, share.getInt(AppConstantUtils.TIME_ADS, 1) - 1).apply();
                mInterAdmob.show();
                mActivity.finish();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mActivity.finish();
                ShowFnish();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                mActivity.finish();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mActivity.finish();
            }

        });
        mInterAdmob.loadAd(mAdRequest);
    }


    public void ShowFnish() {
        Intent intent = new Intent(this, CloseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
