package com.pyxis.compass.gpscompassnavigationmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageCompassView extends AppCompatImageView {
    private float mDegress = 0;
    private int mX, mY;
    private boolean isDrag = false;

    public CustomImageCompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDegress(float degress) {
        mDegress = degress;
    }

    public void setDrag(boolean isMainDrag) {
        this.isDrag = isMainDrag;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!isDrag) {
            int w = canvas.getWidth();
            int h = canvas.getHeight();
            mX = w / 2;
            mY = h / 2;
        }
        canvas.rotate(mDegress, mX, mY);
        super.onDraw(canvas);
    }
}
