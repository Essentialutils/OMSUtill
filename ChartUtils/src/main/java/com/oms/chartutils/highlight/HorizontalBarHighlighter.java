package com.oms.chartutils.highlight;

import com.oms.chartutils.data.BarData;
import com.oms.chartutils.data.DataSet;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.interfaces.dataprovider.BarDataProvider;
import com.oms.chartutils.interfaces.datasets.IBarDataSet;
import com.oms.chartutils.interfaces.datasets.IDataSet;
import com.oms.chartutils.utils.ChartPointD;

import java.util.ArrayList;
import java.util.List;


public class HorizontalBarHighlighter extends BarHighlighter {

    public HorizontalBarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    @Override
    public Highlight getHighlight(float x, float y) {

        BarData barData = mChart.getBarData();

        ChartPointD pos = getValsForTouch(y, x);

        Highlight high = getHighlightForX((float) pos.y, y, x);
        if (high == null)
            return null;

        IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
        if (set.isStacked()) {

            return getStackedHighlight(high,
                    set,
                    (float) pos.y,
                    (float) pos.x);
        }

        ChartPointD.recycleInstance(pos);

        return high;
    }

    @Override
    protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {

        ArrayList<Highlight> highlights = new ArrayList<>();


        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.size() == 0) {

            final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
            if (closest != null) {

                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.size() == 0)
            return highlights;

        for (Entry e : entries) {
            ChartPointD pixels = mChart.getTransformer(
                    set.getAxisDependency()).getPixelForValues(e.getY(), e.getX());

            highlights.add(new Highlight(
                    e.getX(), e.getY(),
                    (float) pixels.x, (float) pixels.y,
                    dataSetIndex, set.getAxisDependency()));
        }

        return highlights;
    }

    @Override
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(y1 - y2);
    }
}
