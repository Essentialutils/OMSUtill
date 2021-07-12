package com.oms.chartutils.formatter;

import com.oms.chartutils.interfaces.dataprovider.LineDataProvider;
import com.oms.chartutils.interfaces.datasets.ILineDataSet;


public interface IFillFormatter {


    float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider);
}
