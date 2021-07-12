package com.oms.chartutils.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.data.LineDataSet;
import com.oms.chartutils.formatter.IFillFormatter;


public interface ILineDataSet extends ILineRadarDataSet<Entry> {


    LineDataSet.Mode getMode();


    float getCubicIntensity();

    @Deprecated
    boolean isDrawCubicEnabled();

    @Deprecated
    boolean isDrawSteppedEnabled();


    float getCircleRadius();


    float getCircleHoleRadius();


    int getCircleColor(int index);


    int getCircleColorCount();


    boolean isDrawCirclesEnabled();


    int getCircleHoleColor();


    boolean isDrawCircleHoleEnabled();


    DashPathEffect getDashPathEffect();


    boolean isDashedLineEnabled();


    IFillFormatter getFillFormatter();
}