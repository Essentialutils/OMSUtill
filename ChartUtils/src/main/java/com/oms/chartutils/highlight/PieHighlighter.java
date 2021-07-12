package com.oms.chartutils.highlight;

import com.oms.chartutils.charts.PieChart;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.interfaces.datasets.IPieDataSet;


public class PieHighlighter extends PieRadarHighlighter<PieChart> {

    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {

        IPieDataSet set = mChart.getData().getDataSet();

        final Entry entry = set.getEntryForIndex(index);

        return new Highlight(index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}
