package com.pyxis.compass.gpscompassnavigationmap.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.NativeExpressAdView;
import com.pyxis.compass.gpscompassnavigationmap.BaseActivity;
import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private LinearLayout mLnAboutApp;
    private LinearLayout mLnRateApp;
    private LinearLayout mLnShareApp;
    private NativeExpressAdView nativeExpressAdView;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.setting_title);

        mLnAboutApp = findViewById(R.id.ln_about_setting);
        mLnRateApp = findViewById(R.id.ln_rate_app_setting);
        mLnShareApp = findViewById(R.id.ln_share_app_setting);

        mLnAboutApp.setOnClickListener(this);
        mLnRateApp.setOnClickListener(this);
        mLnShareApp.setOnClickListener(this);
       // admobBannerUnit();
    }

//    private void admobBannerUnit() {
//        final NativeExpressAdView mAdView = new NativeExpressAdView(this);
//        final AdRequest request = new AdRequest.Builder().build();
//        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 350));
//        mAdView.setAdUnitId("ca-app-pub-5895279942217598/2490904923");
//        nativeExpressAdView.addView(mAdView);
//        mAdView.loadAd(request);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//        });
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ln_about_setting:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.ln_rate_app_setting:
                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                }
                break;
            case R.id.ln_share_app_setting:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getApplication().getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }
}
