package com.oms.chartutils.listener;

import com.oms.chartutils.data.DataSet;
import com.oms.chartutils.data.Entry;


public interface OnDrawListener {


    void onEntryAdded(Entry entry);


    void onEntryMoved(Entry entry);


    void onDrawFinished(DataSet<?> dataSet);

}
