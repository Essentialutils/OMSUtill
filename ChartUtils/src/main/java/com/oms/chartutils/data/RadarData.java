package com.oms.chartutils.data;

import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.interfaces.datasets.IRadarDataSet;

import java.util.Arrays;
import java.util.List;


public class RadarData extends ChartData<IRadarDataSet> {

    private List<String> mLabels;

    public RadarData() {
        super();
    }

    public RadarData(List<IRadarDataSet> dataSets) {
        super(dataSets);
    }

    public RadarData(IRadarDataSet... dataSets) {
        super(dataSets);
    }

    public List<String> getLabels() {
        return mLabels;
    }

    public void setLabels(List<String> labels) {
        this.mLabels = labels;
    }

    public void setLabels(String... labels) {
        this.mLabels = Arrays.asList(labels);
    }

    @Override
    public Entry getEntryForHighlight(Highlight highlight) {
        return getDataSetByIndex(highlight.getDataSetIndex()).getEntryForIndex((int) highlight.getX());
    }
}
