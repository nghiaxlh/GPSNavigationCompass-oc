package com.pyxis.compass.gpscompassnavigationmap.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pyxis.compass.gpscompassnavigationmap.R;

public class DialogCheckInternet extends AlertDialog implements View.OnClickListener {
    private View mView;
    private TextView tvOk;

    public DialogCheckInternet(Context context) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_check_internet, null);
        setView(mView);
        initView();
    }

    private void initView() {
        tvOk = (TextView) mView.findViewById(R.id.tv_dialog_ok);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_ok:
                cancel();
                break;
        }
    }
}
