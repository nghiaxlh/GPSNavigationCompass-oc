package com.pyxis.compass.gpscompassnavigationmap.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.pyxis.compass.gpscompassnavigationmap.R;
import com.pyxis.compass.gpscompassnavigationmap.utils.AppConstantUtils;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogRatingApp extends DialogFragment {

    public static void showDialogRateApp(Context context, FragmentManager fragmentManager) {
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
            new DialogRatingApp().show(fragmentManager, "rate");
        } else {
            editor.commit();
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AppConstantUtils.PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_title_dialog_rate_app)
                .setIcon(R.drawable.ic_emoticon)
                .setMessage(R.string.text_message_dialog_rate_app)
                .setPositiveButton(R.string.text_button_rate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doPositiveClick();
                    }
                })
                .setNegativeButton(R.string.text_button_rate_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doNegativeClick();
                    }
                })
                .setNeutralButton(R.string.text_button_no_thanks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doNeutralClick();

                    }
                }).create();
    }

    private void doNeutralClick() {
        getSharedPreferences(Objects.requireNonNull(getActivity())).edit().putBoolean(AppConstantUtils.DISABLED, true).apply();
        getDialog().dismiss();
    }

    private void doNegativeClick() {
        getDialog().dismiss();
    }

    private void doPositiveClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Objects.requireNonNull(getActivity()).getPackageName())));
        getSharedPreferences(getActivity()).edit().putBoolean(AppConstantUtils.DISABLED, true).apply();
        getDialog().dismiss();
    }
}
