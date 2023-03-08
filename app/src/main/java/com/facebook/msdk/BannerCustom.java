package com.facebook.msdk;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pyxis.compass.gpscompassnavigationmap.R;

public class BannerCustom extends RelativeLayout {
    private Context mContext;
    private AdView bannerAdmob;
    private com.facebook.ads.AdView bannerFace;

    public BannerCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setVisibility(GONE);
        showBannerAmob();
           showBannerFace();
    }

    private void showBannerFace() {
        bannerFace = new com.facebook.ads.AdView(mContext, "866465386890944_1028942280643253", AdSize.BANNER_HEIGHT_50);
        bannerFace.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
                   showBannerAmob();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                removeAllViews();
                addView(bannerFace);
                setVisibility(VISIBLE);
            }
        });
        bannerFace.loadAd();
    }

    private void showBannerAmob() {
        bannerAdmob = new AdView(mContext);
        bannerAdmob.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        bannerAdmob.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        bannerAdmob.loadAd(new AdRequest.Builder().build());
        bannerAdmob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                removeAllViews();
                addView(bannerAdmob);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                showBannerFace();
            }
        });
    }
}
