package com.pyxis.compass.gpscompassnavigationmap.activity;

import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.pyxis.compass.gpscompassnavigationmap.BaseActivity;
import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.listener.HowToUseListener;
import com.pyxis.compass.gpscompassnavigationmap.presenter.HowToUsePre;
import com.pyxis.compass.gpscompassnavigationmap.utils.AdmobAdsUtils;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class HowToUseActivity extends BaseActivity<HowToUsePre> implements SensorEventListener, HowToUseListener {

    private Toolbar mToolbar;
    private TextView mTvMagnetic;
    private TextView mTvDecription;
    private int mCurrentMagnetic;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected HowToUsePre createPresenter() {
        return new HowToUsePre(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        //ShowAdsInter();
        init();
    }

    private void ShowAdsInter() {
        AdmobAdsUtils.getSharedInstance().showInterstitialAd(new AdmobAdsUtils.AdCloseListener() {
            @Override
            public void onAdClosed() {
            }
        });
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar_how_to_use);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_calibrate);

        mTvMagnetic = findViewById(R.id.tv_magnetic_how_to_use);
        mTvDecription = findViewById(R.id.tv_decription_warning_how_to_use);
        String warningStr = getString(R.string.text_decription_notification_warning);
        mPresenter.setIconInText(warningStr);
        mCurrentMagnetic = getIntent().getIntExtra(AppConstantUtils.CALIBRATE_MAGENTIC_COMPASS, 0);
        setTextForMagneticCompass(mCurrentMagnetic);
    }

    private void setTextForMagneticCompass(int magnetic) {
        if (magnetic == 1) {
            mTvMagnetic.setText(R.string.magnetic_calibrate);
            mTvMagnetic.setTextColor(ContextCompat.getColor(this, R.color.color_low));
        } else if (magnetic == 2) {
            mTvMagnetic.setText(R.string.medium_calibrate);
            mTvMagnetic.setTextColor(ContextCompat.getColor(this, R.color.color_medium));
        } else {
            mTvMagnetic.setText(R.string.hight_calibrate);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        switch (sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                switch (i) {
                    case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                        setTextForMagneticCompass(SensorManager.SENSOR_STATUS_ACCURACY_LOW);
                        break;
                    case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                        setTextForMagneticCompass(SensorManager.SENSOR_STATUS_ACCURACY_LOW);
                        break;
                    case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                        setTextForMagneticCompass(SensorManager.SENSOR_STATUS_ACCURACY_LOW);
                        break;
                }
        }
    }

    @Override
    public void getIconText(String s) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_warning);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(s);
        int index = spannableString.toString().indexOf("@");
        spannableString.setSpan(imageSpan, index, index + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvDecription.setText(spannableString);
    }
}
