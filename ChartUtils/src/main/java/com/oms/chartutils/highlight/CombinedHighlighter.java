package com.oms.chartutils.highlight;

import com.oms.chartutils.data.BarData;
import com.oms.chartutils.data.BarLineScatterCandleBubbleData;
import com.oms.chartutils.data.ChartData;
import com.oms.chartutils.data.DataSet;
import com.oms.chartutils.interfaces.dataprovider.BarDataProvider;
import com.oms.chartutils.interfaces.dataprovider.CombinedDataProvider;
import com.oms.chartutils.interfaces.datasets.IDataSet;

import java.util.List;


public class CombinedHighlighter extends ChartHighlighter<CombinedDataProvider> implements IHighlighter {


    protected BarHighlighter barHighlighter;

    public CombinedHighlighter(CombinedDataProvider chart, BarDataProvider barChart) {
        super(chart);


        barHighlighter = barChart.getBarData() == null ? null : new BarHighlighter(barChart);
    }

    @Override
    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {

        mHighlightBuffer.clear();

        List<BarLineScatterCandleBubbleData> dataObjects = mChart.getCombinedData().getAllData();

        for (int i = 0; i < dataObjects.size(); i++) {

            ChartData dataObject = dataObjects.get(i);


            if (barHighlighter != null && dataObject instanceof BarData) {
                Highlight high = barHighlighter.getHighlight(x, y);

                if (high != null) {
                    high.setDataIndex(i);
                    mHighlightBuffer.add(high);
                }
            } else {

                for (int j = 0, dataSetCount = dataObject.getDataSetCount(); j < dataSetCount; j++) {

                    IDataSet dataSet = dataObjects.get(i).getDataSetByIndex(j);


                    if (!dataSet.isHighlightEnabled())
                        continue;

                    List<Highlight> highs = buildHighlights(dataSet, j, xVal, DataSet.Rounding.CLOSEST);
                    for (Highlight high : highs) {
                        high.setDataIndex(i);
                        mHighlightBuffer.add(high);
                    }
                }
            }
        }

        return mHighlightBuffer;
    }


}
