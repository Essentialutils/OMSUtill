package com.oms.chartutils.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.oms.chartutils.data.Entry;


public interface ILineScatterCandleRadarDataSet<T extends Entry> extends IBarLineScatterCandleBubbleDataSet<T> {


    boolean isVerticalHighlightIndicatorEnabled();


    boolean isHorizontalHighlightIndicatorEnabled();


    float getHighlightLineWidth();


    DashPathEffect getDashPathEffectHighlight();
}
