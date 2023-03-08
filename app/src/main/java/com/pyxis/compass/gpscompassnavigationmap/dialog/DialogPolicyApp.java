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

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class DialogPolicyApp extends DialogFragment {

    public static final String DISABLE_POLICY = "DISABLE_POLICY";
    public static final String PREF_NAME_POLICY = "show_dialog";

    public DialogPolicyApp() {
    }

    public static void showDialogPolicy(Context context, FragmentManager fragmentManager) {

        boolean shouldShow = false;
        SharedPreferences preferences = getSharedPreferences(context);
        if (!preferences.getBoolean(DISABLE_POLICY, false)) {
            shouldShow = true;
        }
        if (shouldShow) {
            new DialogPolicyApp().show(fragmentManager, "policy");
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME_POLICY, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setMessage(R.string.text_message_dialog_policy_app)
                .setPositiveButton(R.string.text_button_agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSharedPreferences(getActivity()).edit().putBoolean(DISABLE_POLICY, true).apply();
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.text_button_onlie_help, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstantUtils.POLICY_URL));
                        startActivity(browserIntent);
                    }
                }).create();
    }
}
