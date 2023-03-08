package com.facebook.msdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;

public class TestApp extends Activity {
    private static Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);


        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#F324E4A0"));
        }
        mActivity = this;
        new CountDownTimer(360000, 1000) {
            public void onFinish() {
                finish();
            }

            public void onTick(long j) {
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkOpenSceen(this)) {

            startActivity(new Intent(this, NewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            mActivity.finish();
        }
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean checkOpenSceen(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return pm.isInteractive();
        }
        return true;
    }
}
