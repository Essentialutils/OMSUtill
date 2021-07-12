package com.oms.chartutils.interfaces.datasets;

import com.oms.chartutils.data.BubbleEntry;


public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {


    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    float getHighlightCircleWidth();

    void setHighlightCircleWidth(float width);
}
