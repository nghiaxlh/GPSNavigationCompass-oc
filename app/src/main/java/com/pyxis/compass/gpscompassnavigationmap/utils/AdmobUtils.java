package com.pyxis.compass.gpscompassnavigationmap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pyxis.compass.gpscompassnavigationmap.R;

public class AdmobUtils {
    public static InterstitialAd mInterAdAdmob;
    public static com.facebook.ads.InterstitialAd mInterAdFaceBook;
    public static AdmobUtils INSTANCE = null;
    private static LoadDataListener loadDataListener;
    public interface LoadDataListener {
        void onLoadSuccess();
    }

    public static void loadAdInter(Context mContext) {
        loadInterAdmob(mContext);
        loadAdInterFaceBook(mContext);
    }

    public static AdmobUtils getInstance() {
        if (INSTANCE != null)
            return INSTANCE;
        AdmobUtils admobUtils = new AdmobUtils();
        return admobUtils;
    }

    public static void setLoadDataListener(LoadDataListener listener) {
        loadDataListener = listener;
    }

    public static LoadDataListener getLoadDataListener() {
        return loadDataListener;
    }

    private static void loadInterAdmob(final Context mContext) {
        mInterAdAdmob = new InterstitialAd(mContext);
        mInterAdAdmob.setAdUnitId(mContext.getString(R.string.interstitial_id_1));
        mInterAdAdmob.loadAd(new AdRequest.Builder().build());
        mInterAdAdmob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                    loadDataListener.onLoadSuccess();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadInterAdmob(mContext);
            }
        });
    }

//    public static boolean showInter(Context mContext) {
//        SharedPreferences share = mContext.getSharedPreferences(AppConstantUtils.FILE_DATA_APP, 0);
//            if (System.currentTimeMillis() / 1000 - share.getLong(AppConstantUtils.LAST_TIME_INSIDE, 0) / 1000 > share.getInt(AppConstantUtils.TIME_SHOW_BETTWEN_IN_APP, 15)) {
//                if (mInterAdAdmob != null && mInterAdAdmob.isLoaded()) {
//                    mInterAdAdmob.show();
//                    share.edit().putLong(AppConstantUtils.LAST_TIME_INSIDE, System.currentTimeMillis()).apply();
//                    return true;
//                }
//            }
//        return false;
//    }
public static boolean showInter(Context mContext) {
    SharedPreferences share = mContext.getSharedPreferences(AppConstantUtils.FILE_DATA_APP, 0);
        if (System.currentTimeMillis() / 1000 - share.getLong(AppConstantUtils.LAST_TIME_INSIDE, 0) / 1000 > share.getInt(AppConstantUtils.TIME_SHOW_BETTWEN_IN_APP, 15)) {
            if (mInterAdAdmob != null && mInterAdAdmob.isLoaded()) {
                mInterAdAdmob.show();
                share.edit().putLong(AppConstantUtils.LAST_TIME_INSIDE, System.currentTimeMillis()).apply();
                return true;
            }else
             if (mInterAdFaceBook != null && mInterAdFaceBook.isAdLoaded()) {
                mInterAdFaceBook.show();
                share.edit().putLong(AppConstantUtils.LAST_TIME_INSIDE, System.currentTimeMillis()).apply();
                loadAdInterFaceBook(mContext);
                return true;
            }
        }
//    else {
//        if (System.currentTimeMillis() / 1000 - share.getLong(AppConstantUtils.LAST_TIME_INSIDE, 0) / 1000 > share.getInt(AppConstantUtils.TIME_SHOW_BETTWEN_IN_APP, 2)) {
//            if (mInterAdFaceBook != null && mInterAdFaceBook.isAdLoaded()) {
//                mInterAdFaceBook.show();
//                share.edit().putLong(AppConstantUtils.LAST_TIME_INSIDE, System.currentTimeMillis()).apply();
//                loadAdInterFaceBook(mContext);
//                return true;
//            } else if (mInterAdAdmob != null && mInterAdAdmob.isLoaded()) {
//                mInterAdAdmob.show();
//                share.edit().putLong(AppConstantUtils.LAST_TIME_INSIDE, System.currentTimeMillis()).apply();
//                return true;
//            }
//        }
//    }
    return false;
}

    private static void loadAdInterFaceBook(final Context mContext) {
        mInterAdFaceBook = new com.facebook.ads.InterstitialAd(mContext, "866465386890944_866466390224177");
        mInterAdFaceBook.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                loadDataListener.onLoadSuccess();
                mInterAdFaceBook.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        mInterAdFaceBook.loadAd();
    }
}
