package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.components.YAxis;
import com.oms.chartutils.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
