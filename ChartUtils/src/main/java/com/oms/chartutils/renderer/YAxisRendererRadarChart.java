package com.oms.chartutils.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import com.oms.chartutils.charts.RadarChart;
import com.oms.chartutils.components.LimitLine;
import com.oms.chartutils.components.YAxis;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.Utils;
import com.oms.chartutils.utils.ViewPortHandler;

import java.util.List;

public class YAxisRendererRadarChart extends YAxisRenderer {

    private final RadarChart mChart;
    private final Path mRenderLimitLinesPathBuffer = new Path();

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart chart) {
        super(viewPortHandler, yAxis, null);

        this.mChart = chart;
    }

    @Override
    protected void computeAxisValues(float min, float max) {

        float yMin = min;
        float yMax = max;

        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);

        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }


        double rawInterval = range / labelCount;
        double interval = Utils.roundToNextSignificant(rawInterval);


        if (mAxis.isGranularityEnabled())
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;


        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10, (int) Math.log10(interval)));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {


            interval = Math.floor(10.0 * intervalMagnitude) == 0.0
                    ? interval
                    : Math.floor(10.0 * intervalMagnitude);
        }

        boolean centeringEnabled = mAxis.isCenterAxisLabelsEnabled();
        int n = centeringEnabled ? 1 : 0;


        if (mAxis.isForceLabelsEnabled()) {

            float step = (float) range / (float) (labelCount - 1);
            mAxis.mEntryCount = labelCount;

            if (mAxis.mEntries.length < labelCount) {

                mAxis.mEntries = new float[labelCount];
            }

            float v = min;

            for (int i = 0; i < labelCount; i++) {
                mAxis.mEntries[i] = v;
                v += step;
            }

            n = labelCount;


        } else {

            double first = interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval;
            if (centeringEnabled) {
                first -= interval;
            }

            double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(yMax / interval) * interval);

            double f;
            int i;

            if (interval != 0.0) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }

            n++;

            mAxis.mEntryCount = n;

            if (mAxis.mEntries.length < n) {

                mAxis.mEntries = new float[n];
            }

            for (f = first, i = 0; i < n; f += interval, ++i) {

                if (f == 0.0)
                    f = 0.0;

                mAxis.mEntries[i] = (float) f;
            }
        }


        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }

        if (centeringEnabled) {

            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (mAxis.mEntries[1] - mAxis.mEntries[0]) / 2f;

            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }

        mAxis.mAxisMinimum = mAxis.mEntries[0];
        mAxis.mAxisMaximum = mAxis.mEntries[n - 1];
        mAxis.mAxisRange = Math.abs(mAxis.mAxisMaximum - mAxis.mAxisMinimum);
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);
        float factor = mChart.getFactor();

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        float xOffset = mYAxis.getLabelXOffset();

        for (int j = from; j < to; j++) {

            float r = (mYAxis.mEntries[j] - mYAxis.mAxisMinimum) * factor;

            Utils.getPosition(center, r, mChart.getRotationAngle(), pOut);

            String label = mYAxis.getFormattedLabel(j);

            c.drawText(label, pOut.x + xOffset, pOut.y, mAxisLabelPaint);
        }
        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
    }

    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        if (limitLines == null)
            return;

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);
        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled())
                continue;

            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());

            float r = (l.getLimit() - mChart.getYChartMin()) * factor;

            Path limitPath = mRenderLimitLinesPathBuffer;
            limitPath.reset();


            for (int j = 0; j < mChart.getData().getMaxEntryCountSet().getEntryCount(); j++) {

                Utils.getPosition(center, r, sliceangle * j + mChart.getRotationAngle(), pOut);

                if (j == 0)
                    limitPath.moveTo(pOut.x, pOut.y);
                else
                    limitPath.lineTo(pOut.x, pOut.y);
            }
            limitPath.close();

            c.drawPath(limitPath, mLimitLinePaint);
        }
        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
    }
}
