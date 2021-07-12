package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
