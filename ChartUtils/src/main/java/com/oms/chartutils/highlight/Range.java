package com.oms.chartutils.highlight;


public final class Range {

    public float from;
    public float to;

    public Range(float from, float to) {
        this.from = from;
        this.to = to;
    }


    public boolean contains(float value) {

        return value > from && value <= to;
    }

    public boolean isLarger(float value) {
        return value > to;
    }

    public boolean isSmaller(float value) {
        return value < from;
    }
}