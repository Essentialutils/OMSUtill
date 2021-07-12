package com.oms.chartutils.formatter;

import com.oms.chartutils.data.BarEntry;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.utils.ViewPortHandler;

import java.text.DecimalFormat;


public class StackedValueFormatter implements IValueFormatter {


    private final boolean mDrawWholeStack;


    private final String mAppendix;

    private final DecimalFormat mFormat;


    public StackedValueFormatter(boolean drawWholeStack, String appendix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mAppendix = appendix;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if (!mDrawWholeStack && entry instanceof BarEntry) {

            BarEntry barEntry = (BarEntry) entry;
            float[] vals = barEntry.getYVals();

            if (vals != null) {


                if (vals[vals.length - 1] == value) {


                    return mFormat.format(barEntry.getY()) + mAppendix;
                } else {
                    return "";
                }
            }
        }


        return mFormat.format(value) + mAppendix;
    }
}
