package com.pyxis.compass.gpscompassnavigationmap.presenter;

import com.pyxis.compass.gpscompassnavigationmap.BasePresenter;
import com.pyxis.compass.gpscompassnavigationmap.listener.HowToUseListener;

public class HowToUsePre extends BasePresenter<HowToUseListener> {
    public HowToUsePre(HowToUseListener view) {
        super.getAttachView(view);
    }

    public void setIconInText(String warningStr) {
        mView.getIconText(warningStr);
    }
}
