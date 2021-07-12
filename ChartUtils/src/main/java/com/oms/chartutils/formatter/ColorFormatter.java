package com.oms.chartutils.formatter;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.interfaces.datasets.IDataSet;


public interface ColorFormatter {


    int getColor(int index, Entry e, IDataSet set);
}