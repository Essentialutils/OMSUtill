package com.oms.chartutils.components;

import android.graphics.Paint;

import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.Utils;


public class Description extends ComponentBase {


    private String text = "Description Label";


    private ChartPointF mPosition;


    private Paint.Align mTextAlign = Paint.Align.RIGHT;

    public Description() {
        super();


        mTextSize = Utils.convertDpToPixel(8f);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(float x, float y) {
        if (mPosition == null) {
            mPosition = ChartPointF.getInstance(x, y);
        } else {
            mPosition.x = x;
            mPosition.y = y;
        }
    }


    public ChartPointF getPosition() {
        return mPosition;
    }

    public Paint.Align getTextAlign() {
        return mTextAlign;
    }

    public void setTextAlign(Paint.Align align) {
        this.mTextAlign = align;
    }
}
