package com.oms.chartutils.interfaces.datasets;

import com.oms.chartutils.data.RadarEntry;


public interface IRadarDataSet extends ILineRadarDataSet<RadarEntry> {


    boolean isDrawHighlightCircleEnabled();


    void setDrawHighlightCircleEnabled(boolean enabled);

    int getHighlightCircleFillColor();


    int getHighlightCircleStrokeColor();

    int getHighlightCircleStrokeAlpha();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    float getHighlightCircleStrokeWidth();

}
