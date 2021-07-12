package com.oms.chartutils.interfaces.dataprovider;

import com.oms.chartutils.components.YAxis.AxisDependency;
import com.oms.chartutils.data.BarLineScatterCandleBubbleData;
import com.oms.chartutils.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);

    boolean isInverted(AxisDependency axis);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
