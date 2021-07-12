package com.oms.chartutils.renderer;

import android.graphics.Canvas;

import com.oms.chartutils.charts.RadarChart;
import com.oms.chartutils.components.XAxis;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.Utils;
import com.oms.chartutils.utils.ViewPortHandler;

public class XAxisRendererRadarChart extends XAxisRenderer {

    private final RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);

        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final ChartPointF drawLabelAnchor = ChartPointF.getInstance(0.5f, 0.25f);

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {

            String label = mXAxis.getValueFormatter().getFormattedValue(i, mXAxis);

            float angle = (sliceangle * i + mChart.getRotationAngle()) % 360f;

            Utils.getPosition(center, mChart.getYRange() * factor
                    + mXAxis.mLabelRotatedWidth / 2f, angle, pOut);

            drawLabel(c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f,
                    drawLabelAnchor, labelRotationAngleDegrees);
        }

        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
        ChartPointF.recycleInstance(drawLabelAnchor);
    }


    @Override
    public void renderLimitLines(Canvas c) {
        // this space intentionally left blank
    }
}
