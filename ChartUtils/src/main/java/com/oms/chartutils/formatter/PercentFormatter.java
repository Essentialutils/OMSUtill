package com.oms.chartutils.formatter;

import com.oms.chartutils.components.AxisBase;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.utils.ViewPortHandler;

import java.text.DecimalFormat;


public class PercentFormatter implements IValueFormatter, IAxisValueFormatter {

    protected DecimalFormat mFormat;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }


    public PercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }


    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " %";
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " %";
    }

    public int getDecimalDigits() {
        return 1;
    }
}
