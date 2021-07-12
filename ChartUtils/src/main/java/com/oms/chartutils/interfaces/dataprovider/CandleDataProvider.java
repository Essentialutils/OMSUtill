package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
