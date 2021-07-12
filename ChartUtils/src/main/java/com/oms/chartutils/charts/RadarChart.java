package com.oms.chartutils.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.oms.chartutils.components.YAxis;
import com.oms.chartutils.components.YAxis.AxisDependency;
import com.oms.chartutils.data.RadarData;
import com.oms.chartutils.highlight.RadarHighlighter;
import com.oms.chartutils.renderer.RadarChartRenderer;
import com.oms.chartutils.renderer.XAxisRendererRadarChart;
import com.oms.chartutils.renderer.YAxisRendererRadarChart;
import com.oms.chartutils.utils.Utils;


public class RadarChart extends PieRadarChartBase<RadarData> {


    protected YAxisRendererRadarChart mYAxisRenderer;
    protected XAxisRendererRadarChart mXAxisRenderer;
    private float mWebLineWidth = 2.5f;
    private float mInnerWebLineWidth = 1.5f;
    private int mWebColor = Color.rgb(122, 122, 122);
    private int mWebColorInner = Color.rgb(122, 122, 122);
    private int mWebAlpha = 150;
    private boolean mDrawWeb = true;
    private int mSkipWebLineCount = 0;
    private YAxis mYAxis;

    public RadarChart(Context context) {
        super(context);
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mYAxis = new YAxis(AxisDependency.LEFT);
        mYAxis.setLabelXOffset(10f);

        mWebLineWidth = Utils.convertDpToPixel(1.5f);
        mInnerWebLineWidth = Utils.convertDpToPixel(0.75f);

        mRenderer = new RadarChartRenderer(this, mAnimator, mViewPortHandler);
        mYAxisRenderer = new YAxisRendererRadarChart(mViewPortHandler, mYAxis, this);
        mXAxisRenderer = new XAxisRendererRadarChart(mViewPortHandler, mXAxis, this);

        mHighlighter = new RadarHighlighter(this);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        mYAxis.calculate(mData.getYMin(AxisDependency.LEFT), mData.getYMax(AxisDependency.LEFT));
        mXAxis.calculate(0, mData.getMaxEntryCountSet().getEntryCount());
    }

    @Override
    public void notifyDataSetChanged() {
        if (mData == null)
            return;

        calcMinMax();

        mYAxisRenderer.computeAxis(mYAxis.mAxisMinimum, mYAxis.mAxisMaximum, mYAxis.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        if (mLegend != null && !mLegend.isLegendCustom())
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;


        if (mXAxis.isEnabled())
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        mXAxisRenderer.renderAxisLabels(canvas);

        if (mDrawWeb)
            mRenderer.drawExtras(canvas);

        if (mYAxis.isEnabled() && mYAxis.isDrawLimitLinesBehindDataEnabled())
            mYAxisRenderer.renderLimitLines(canvas);

        mRenderer.drawData(canvas);

        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        if (mYAxis.isEnabled() && !mYAxis.isDrawLimitLinesBehindDataEnabled())
            mYAxisRenderer.renderLimitLines(canvas);

        mYAxisRenderer.renderAxisLabels(canvas);

        mRenderer.drawValues(canvas);

        mLegendRenderer.renderLegend(canvas);

        drawDescription(canvas);

        drawMarkers(canvas);
    }


    public float getFactor() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f) / mYAxis.mAxisRange;
    }


    public float getSliceAngle() {
        return 360f / (float) mData.getMaxEntryCountSet().getEntryCount();
    }

    @Override
    public int getIndexForAngle(float angle) {


        float a = Utils.getNormalizedAngle(angle - getRotationAngle());

        float sliceangle = getSliceAngle();

        int max = mData.getMaxEntryCountSet().getEntryCount();

        int index = 0;

        for (int i = 0; i < max; i++) {

            float referenceAngle = sliceangle * (i + 1) - sliceangle / 2f;

            if (referenceAngle > a) {
                index = i;
                break;
            }
        }

        return index;
    }


    public YAxis getYAxis() {
        return mYAxis;
    }

    public float getWebLineWidth() {
        return mWebLineWidth;
    }

    public void setWebLineWidth(float width) {
        mWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidthInner() {
        return mInnerWebLineWidth;
    }

    public void setWebLineWidthInner(float width) {
        mInnerWebLineWidth = Utils.convertDpToPixel(width);
    }

    public int getWebAlpha() {
        return mWebAlpha;
    }

    public void setWebAlpha(int alpha) {
        mWebAlpha = alpha;
    }

    public int getWebColor() {
        return mWebColor;
    }

    public void setWebColor(int color) {
        mWebColor = color;
    }

    public int getWebColorInner() {
        return mWebColorInner;
    }

    public void setWebColorInner(int color) {
        mWebColorInner = color;
    }

    public void setDrawWeb(boolean enabled) {
        mDrawWeb = enabled;
    }

    public int getSkipWebLineCount() {
        return mSkipWebLineCount;
    }

    public void setSkipWebLineCount(int count) {

        mSkipWebLineCount = Math.max(0, count);
    }

    @Override
    protected float getRequiredLegendOffset() {
        return mLegendRenderer.getLabelPaint().getTextSize() * 4.f;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled() ?
                mXAxis.mLabelRotatedWidth :
                Utils.convertDpToPixel(10f);
    }

    @Override
    public float getRadius() {
        RectF content = mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2f, content.height() / 2f);
    }


    public float getYChartMax() {
        return mYAxis.mAxisMaximum;
    }


    public float getYChartMin() {
        return mYAxis.mAxisMinimum;
    }


    public float getYRange() {
        return mYAxis.mAxisRange;
    }
}
