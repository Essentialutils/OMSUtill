package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.data.CombinedData;


public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
