package com.pyxis.compass.gpscompassnavigationmap.dialog;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;

import androidx.appcompat.app.AlertDialog;

public class RateUsDialogApp extends AlertDialog implements View.OnClickListener {
    private View rootView;
    private LinearLayout lnStarOne, lnStarTwo, lnStarThree, lnStarFour, lnStarFive;
    private ImageView imgBackground, imgStarone, imgStarTwo, imgStarThree, imgStarFour, imgStarFive;
    private TextView txtRateTitle;
    private RelativeLayout rlRateUs;

    private Activity mActivity;

    public RateUsDialogApp(Activity context) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_rate_app, null);
        setView(rootView);
        mActivity = context;
        initView();
    }

    public static void showDialogRateApp(Context context) {
        boolean shouldShow = false;
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long currentTime = System.currentTimeMillis();
        long lastPromptTime = sharedPreferences.getLong(AppConstantUtils.LAST_PROMPT, 0);
        if (lastPromptTime == 0) {
            lastPromptTime = currentTime;
            editor.putLong(AppConstantUtils.LAST_PROMPT, lastPromptTime);
        }
        if (!sharedPreferences.getBoolean(AppConstantUtils.DISABLED, false)) {
            int launchs = sharedPreferences.getInt(AppConstantUtils.LAUNCHES, 0) + 1;
            if (launchs > AppConstantUtils.LAUNCHER_UNTIL || currentTime > lastPromptTime + AppConstantUtils.MILLI_UNTIL) {
                shouldShow = true;
            }
            editor.putInt(AppConstantUtils.LAUNCHES, launchs);
        }

        if (shouldShow) {
            editor.putInt(AppConstantUtils.LAUNCHES, 0)
                    .putLong(AppConstantUtils.LAST_PROMPT, System.currentTimeMillis()).apply();
            new RateUsDialogApp((Activity) context).show();
        } else {
            editor.commit();
        }
    }
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AppConstantUtils.PREF_NAME, Context.MODE_PRIVATE);
    }

    private void initView() {
//        imgBackground = rootView.findViewById(R.id.img_background_rate);
        imgStarone = rootView.findViewById(R.id.img_rate_one);
        imgStarTwo = rootView.findViewById(R.id.img_rate_two);
        imgStarThree = rootView.findViewById(R.id.img_rate_three);
        imgStarFour = rootView.findViewById(R.id.img_rate_four);
        imgStarFive = rootView.findViewById(R.id.img_rate_five);
        txtRateTitle = rootView.findViewById(R.id.txt_rate_title);

        lnStarOne = rootView.findViewById(R.id.ln_rate_one);
        lnStarOne.setOnClickListener(this);
        lnStarTwo = rootView.findViewById(R.id.ln_rate_two);
        lnStarTwo.setOnClickListener(this);
        lnStarThree = rootView.findViewById(R.id.ln_rate_three);
        lnStarThree.setOnClickListener(this);
        lnStarFour = rootView.findViewById(R.id.ln_rate_four);
        lnStarFour.setOnClickListener(this);
        lnStarFive = rootView.findViewById(R.id.ln_rate_five);
        lnStarFive.setOnClickListener(this);

        rlRateUs = rootView.findViewById(R.id.rl_rate_us);
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mActivity.getPackageName())));
                return;
            } catch (Exception localException) {

                Toast toast = Toast.makeText(mActivity, "unable to find market app", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void sendFeedback() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"pyxispie@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        getContext().startActivity(Intent.createChooser(email, "Send Feedback:"));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ln_rate_one) {
            imgStarone.setImageResource(R.drawable.star_checked);
            imgStarTwo.setImageResource(R.drawable.star_uncheck);
            imgStarThree.setImageResource(R.drawable.star_uncheck);
            imgStarFour.setImageResource(R.drawable.star_uncheck);
            imgStarFive.setImageResource(R.drawable.star_uncheck);
            sendFeedback();
            dismiss();
        } else if (i == R.id.ln_rate_two) {
            imgStarone.setImageResource(R.drawable.star_checked);
            imgStarTwo.setImageResource(R.drawable.star_checked);
            imgStarThree.setImageResource(R.drawable.star_uncheck);
            imgStarFour.setImageResource(R.drawable.star_uncheck);
            imgStarFive.setImageResource(R.drawable.star_uncheck);
            sendFeedback();
            dismiss();
        } else if (i == R.id.ln_rate_three) {
            imgStarone.setImageResource(R.drawable.star_checked);
            imgStarTwo.setImageResource(R.drawable.star_checked);
            imgStarThree.setImageResource(R.drawable.star_checked);
            imgStarFour.setImageResource(R.drawable.star_uncheck);
            imgStarFive.setImageResource(R.drawable.star_uncheck);
            sendFeedback();
            dismiss();
        } else if (i == R.id.ln_rate_four) {
            imgStarone.setImageResource(R.drawable.star_checked);
            imgStarTwo.setImageResource(R.drawable.star_checked);
            imgStarThree.setImageResource(R.drawable.star_checked);
            imgStarFour.setImageResource(R.drawable.star_checked);
            imgStarFive.setImageResource(R.drawable.star_uncheck);
            sendFeedback();
            dismiss();
        } else if (i == R.id.ln_rate_five) {
            imgStarone.setImageResource(R.drawable.star_checked);
            imgStarTwo.setImageResource(R.drawable.star_checked);
            imgStarThree.setImageResource(R.drawable.star_checked);
            imgStarFour.setImageResource(R.drawable.star_checked);
            imgStarFive.setImageResource(R.drawable.star_checked);
            launchMarket();
            dismiss();
        }
    }
}

