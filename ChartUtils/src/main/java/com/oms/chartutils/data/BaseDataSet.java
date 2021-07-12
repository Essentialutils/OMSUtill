package com.oms.chartutils.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;

import com.oms.chartutils.components.Legend;
import com.oms.chartutils.components.YAxis;
import com.oms.chartutils.formatter.IValueFormatter;
import com.oms.chartutils.interfaces.datasets.IDataSet;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.ColorTemplate;
import com.oms.chartutils.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {


    protected List<Integer> mColors = null;


    protected List<Integer> mValueColors = null;

    protected YAxis.AxisDependency mAxisDependency = YAxis.AxisDependency.LEFT;

    protected boolean mHighlightEnabled = true;

    protected transient IValueFormatter mValueFormatter;

    protected Typeface mValueTypeface;

    protected boolean mDrawValues = true;

    protected boolean mDrawIcons = true;

    protected ChartPointF mIconsOffset = new ChartPointF();

    protected float mValueTextSize = 17f;

    protected boolean mVisible = true;

    private String mLabel = "DataSet";
    private Legend.LegendForm mForm = Legend.LegendForm.DEFAULT;
    private float mFormSize = Float.NaN;
    private float mFormLineWidth = Float.NaN;
    private DashPathEffect mFormLineDashEffect = null;


    public BaseDataSet() {
        mColors = new ArrayList<Integer>();
        mValueColors = new ArrayList<Integer>();


        mColors.add(Color.rgb(140, 234, 255));
        mValueColors.add(Color.BLACK);
    }


    public BaseDataSet(String label) {
        this();
        this.mLabel = label;
    }


    public void notifyDataSetChanged() {
        calcMinMax();
    }


    @Override
    public List<Integer> getColors() {
        return mColors;
    }


    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }


    public void setColors(int... colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    public List<Integer> getValueColors() {
        return mValueColors;
    }


    @Override
    public int getColor() {
        return mColors.get(0);
    }


    public void setColor(int color) {
        resetColors();
        mColors.add(color);
    }

    @Override
    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }


    public void setColors(int[] colors, Context c) {

        if (mColors == null) {
            mColors = new ArrayList<>();
        }

        mColors.clear();

        for (int color : colors) {
            mColors.add(c.getResources().getColor(color));
        }
    }


    public void addColor(int color) {
        if (mColors == null)
            mColors = new ArrayList<Integer>();
        mColors.add(color);
    }


    public void setColor(int color, int alpha) {
        setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }


    public void setColors(int[] colors, int alpha) {
        resetColors();
        for (int color : colors) {
            addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }


    public void resetColors() {
        if (mColors == null) {
            mColors = new ArrayList<Integer>();
        }
        mColors.clear();
    }

    @Override
    public String getLabel() {
        return mLabel;
    }


    @Override
    public void setLabel(String label) {
        mLabel = label;
    }

    @Override
    public boolean isHighlightEnabled() {
        return mHighlightEnabled;
    }

    @Override
    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;
    }

    @Override
    public IValueFormatter getValueFormatter() {
        if (needsFormatter())
            return Utils.getDefaultValueFormatter();
        return mValueFormatter;
    }

    @Override
    public void setValueFormatter(IValueFormatter f) {

        if (f == null)
            return;
        else
            mValueFormatter = f;
    }

    @Override
    public boolean needsFormatter() {
        return mValueFormatter == null;
    }

    @Override
    public void setValueTextColors(List<Integer> colors) {
        mValueColors = colors;
    }

    @Override
    public int getValueTextColor() {
        return mValueColors.get(0);
    }

    @Override
    public void setValueTextColor(int color) {
        mValueColors.clear();
        mValueColors.add(color);
    }

    @Override
    public int getValueTextColor(int index) {
        return mValueColors.get(index % mValueColors.size());
    }

    @Override
    public Typeface getValueTypeface() {
        return mValueTypeface;
    }

    @Override
    public void setValueTypeface(Typeface tf) {
        mValueTypeface = tf;
    }

    @Override
    public float getValueTextSize() {
        return mValueTextSize;
    }

    @Override
    public void setValueTextSize(float size) {
        mValueTextSize = Utils.convertDpToPixel(size);
    }

    @Override
    public Legend.LegendForm getForm() {
        return mForm;
    }

    public void setForm(Legend.LegendForm form) {
        mForm = form;
    }

    @Override
    public float getFormSize() {
        return mFormSize;
    }

    public void setFormSize(float formSize) {
        mFormSize = formSize;
    }

    @Override
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    public void setFormLineWidth(float formLineWidth) {
        mFormLineWidth = formLineWidth;
    }

    @Override
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    @Override
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    @Override
    public boolean isDrawValuesEnabled() {
        return mDrawValues;
    }

    @Override
    public void setDrawIcons(boolean enabled) {
        mDrawIcons = enabled;
    }

    @Override
    public boolean isDrawIconsEnabled() {
        return mDrawIcons;
    }

    @Override
    public ChartPointF getIconsOffset() {
        return mIconsOffset;
    }

    @Override
    public void setIconsOffset(ChartPointF offsetDp) {

        mIconsOffset.x = offsetDp.x;
        mIconsOffset.y = offsetDp.y;
    }

    @Override
    public boolean isVisible() {
        return mVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    @Override
    public YAxis.AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    @Override
    public void setAxisDependency(YAxis.AxisDependency dependency) {
        mAxisDependency = dependency;
    }


    @Override
    public int getIndexInEntries(int xIndex) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (xIndex == getEntryForIndex(i).getX())
                return i;
        }

        return -1;
    }

    @Override
    public boolean removeFirst() {

        if (getEntryCount() > 0) {

            T entry = getEntryForIndex(0);
            return removeEntry(entry);
        } else
            return false;
    }

    @Override
    public boolean removeLast() {

        if (getEntryCount() > 0) {

            T e = getEntryForIndex(getEntryCount() - 1);
            return removeEntry(e);
        } else
            return false;
    }

    @Override
    public boolean removeEntryByXValue(float xValue) {

        T e = getEntryForXValue(xValue, Float.NaN);
        return removeEntry(e);
    }

    @Override
    public boolean removeEntry(int index) {

        T e = getEntryForIndex(index);
        return removeEntry(e);
    }

    @Override
    public boolean contains(T e) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (getEntryForIndex(i).equals(e))
                return true;
        }

        return false;
    }

    protected void copy(BaseDataSet baseDataSet) {
        baseDataSet.mAxisDependency = mAxisDependency;
        baseDataSet.mColors = mColors;
        baseDataSet.mDrawIcons = mDrawIcons;
        baseDataSet.mDrawValues = mDrawValues;
        baseDataSet.mForm = mForm;
        baseDataSet.mFormLineDashEffect = mFormLineDashEffect;
        baseDataSet.mFormLineWidth = mFormLineWidth;
        baseDataSet.mFormSize = mFormSize;
        baseDataSet.mHighlightEnabled = mHighlightEnabled;
        baseDataSet.mIconsOffset = mIconsOffset;
        baseDataSet.mValueColors = mValueColors;
        baseDataSet.mValueFormatter = mValueFormatter;
        baseDataSet.mValueColors = mValueColors;
        baseDataSet.mValueTextSize = mValueTextSize;
        baseDataSet.mVisible = mVisible;
    }
}
