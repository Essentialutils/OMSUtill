package com.oms.chartutils.listener;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.highlight.Highlight;


public interface OnChartValueSelectedListener {


    void onValueSelected(Entry e, Highlight h);


    void onNothingSelected();
}
