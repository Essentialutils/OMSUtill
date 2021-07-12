package com.oms.chartutils.formatter;

import com.oms.chartutils.components.AxisBase;


public interface IAxisValueFormatter {


    String getFormattedValue(float value, AxisBase axis);
}
