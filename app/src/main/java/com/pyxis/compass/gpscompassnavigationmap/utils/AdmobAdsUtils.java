package com.pyxis.compass.gpscompassnavigationmap.utils;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.pyxis.compass.gpscompassnavigationmap.R;

public class AdmobAdsUtils {
    private final String APP_ID = "ca-app-pub-6462057201321072~3487624927";
    private InterstitialAd interstitialAd;
    private static AdmobAdsUtils sharedInstance;
    private AdCloseListener adCloseListener;
    private boolean isReloaded = false;

    public static AdmobAdsUtils getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new AdmobAdsUtils();
        }
        return sharedInstance;
    }

    public void init(Context context) {
        MobileAds.initialize(context, APP_ID);
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_id_1));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
                loadInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (isReloaded == false) {
                    isReloaded = true;
                    loadInterstitialAd();
                }
            }
        });
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        if (interstitialAd != null && !interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public void showInterstitialAd(AdCloseListener adCloseListener) {
        if (canShowInterstitialAd()) {
            isReloaded = false;
            this.adCloseListener = adCloseListener;
            interstitialAd.show();

        } else {
            loadInterstitialAd();
            adCloseListener.onAdClosed();
        }
    }

    private boolean canShowInterstitialAd() {
        return interstitialAd != null && interstitialAd.isLoaded();
    }
}
