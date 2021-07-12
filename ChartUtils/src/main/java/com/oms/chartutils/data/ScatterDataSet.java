package com.oms.chartutils.data;

import com.oms.chartutils.charts.ScatterChart;
import com.oms.chartutils.interfaces.datasets.IScatterDataSet;
import com.oms.chartutils.renderer.scatter.ChevronDownShapeRenderer;
import com.oms.chartutils.renderer.scatter.ChevronUpShapeRenderer;
import com.oms.chartutils.renderer.scatter.CircleShapeRenderer;
import com.oms.chartutils.renderer.scatter.CrossShapeRenderer;
import com.oms.chartutils.renderer.scatter.IShapeRenderer;
import com.oms.chartutils.renderer.scatter.SquareShapeRenderer;
import com.oms.chartutils.renderer.scatter.TriangleShapeRenderer;
import com.oms.chartutils.renderer.scatter.XShapeRenderer;
import com.oms.chartutils.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet {


    protected IShapeRenderer mShapeRenderer = new SquareShapeRenderer();
    private float mShapeSize = 15f;
    private float mScatterShapeHoleRadius = 0f;


    private int mScatterShapeHoleColor = ColorTemplate.COLOR_NONE;

    public ScatterDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    public static IShapeRenderer getRendererForShape(ScatterChart.ScatterShape shape) {

        switch (shape) {
            case SQUARE:
                return new SquareShapeRenderer();
            case CIRCLE:
                return new CircleShapeRenderer();
            case TRIANGLE:
                return new TriangleShapeRenderer();
            case CROSS:
                return new CrossShapeRenderer();
            case X:
                return new XShapeRenderer();
            case CHEVRON_UP:
                return new ChevronUpShapeRenderer();
            case CHEVRON_DOWN:
                return new ChevronDownShapeRenderer();
        }

        return null;
    }

    @Override
    public DataSet<Entry> copy() {
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < mEntries.size(); i++) {
            entries.add(mEntries.get(i).copy());
        }
        ScatterDataSet copied = new ScatterDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(ScatterDataSet scatterDataSet) {
        super.copy(scatterDataSet);
        scatterDataSet.mShapeSize = mShapeSize;
        scatterDataSet.mShapeRenderer = mShapeRenderer;
        scatterDataSet.mScatterShapeHoleRadius = mScatterShapeHoleRadius;
        scatterDataSet.mScatterShapeHoleColor = mScatterShapeHoleColor;
    }

    @Override
    public float getScatterShapeSize() {
        return mShapeSize;
    }

    public void setScatterShapeSize(float size) {
        mShapeSize = size;
    }

    public void setScatterShape(ScatterChart.ScatterShape shape) {
        mShapeRenderer = getRendererForShape(shape);
    }

    @Override
    public IShapeRenderer getShapeRenderer() {
        return mShapeRenderer;
    }

    public void setShapeRenderer(IShapeRenderer shapeRenderer) {
        mShapeRenderer = shapeRenderer;
    }

    @Override
    public float getScatterShapeHoleRadius() {
        return mScatterShapeHoleRadius;
    }

    public void setScatterShapeHoleRadius(float holeRadius) {
        mScatterShapeHoleRadius = holeRadius;
    }

    @Override
    public int getScatterShapeHoleColor() {
        return mScatterShapeHoleColor;
    }

    public void setScatterShapeHoleColor(int holeColor) {
        mScatterShapeHoleColor = holeColor;
    }
}
