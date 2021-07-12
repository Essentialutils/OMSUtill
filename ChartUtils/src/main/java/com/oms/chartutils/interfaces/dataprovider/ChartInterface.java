package com.oms.chartutils.interfaces.dataprovider;

import android.graphics.RectF;

import com.oms.chartutils.data.ChartData;
import com.oms.chartutils.formatter.IValueFormatter;
import com.oms.chartutils.utils.ChartPointF;


public interface ChartInterface {


    float getXChartMin();


    float getXChartMax();

    float getXRange();


    float getYChartMin();


    float getYChartMax();


    float getMaxHighlightDistance();

    int getWidth();

    int getHeight();

    ChartPointF getCenterOfView();

    ChartPointF getCenterOffsets();

    RectF getContentRect();

    IValueFormatter getDefaultValueFormatter();

    ChartData getData();

    int getMaxVisibleCount();
}
