package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
