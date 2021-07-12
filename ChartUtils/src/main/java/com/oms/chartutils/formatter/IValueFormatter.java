package com.oms.chartutils.formatter;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.utils.ViewPortHandler;


public interface IValueFormatter {


    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}
