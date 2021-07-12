package com.oms.chartutils.interfaces.datasets;

import com.oms.chartutils.data.Entry;


public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {


    int getHighLightColor();
}
