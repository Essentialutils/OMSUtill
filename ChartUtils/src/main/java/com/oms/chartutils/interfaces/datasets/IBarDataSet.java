package com.oms.chartutils.interfaces.datasets;

import com.oms.chartutils.data.BarEntry;
import com.oms.chartutils.utils.Fill;

import java.util.List;


public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {

    List<Fill> getFills();

    Fill getFill(int index);


    boolean isStacked();


    int getStackSize();


    int getBarShadowColor();


    float getBarBorderWidth();


    int getBarBorderColor();


    int getHighLightAlpha();


    String[] getStackLabels();
}
