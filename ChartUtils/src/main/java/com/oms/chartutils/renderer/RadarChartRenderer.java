package com.oms.chartutils.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.oms.chartutils.animation.ChartAnimator;
import com.oms.chartutils.charts.RadarChart;
import com.oms.chartutils.data.RadarData;
import com.oms.chartutils.data.RadarEntry;
import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.interfaces.datasets.IRadarDataSet;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.ColorTemplate;
import com.oms.chartutils.utils.Utils;
import com.oms.chartutils.utils.ViewPortHandler;

public class RadarChartRenderer extends LineRadarRenderer {

    protected RadarChart mChart;


    protected Paint mWebPaint;
    protected Paint mHighlightCirclePaint;
    protected Path mDrawDataSetSurfacePathBuffer = new Path();
    protected Path mDrawHighlightCirclePathBuffer = new Path();

    public RadarChartRenderer(RadarChart chart, ChartAnimator animator,
                              ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(Color.rgb(255, 187, 115));

        mWebPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWebPaint.setStyle(Paint.Style.STROKE);

        mHighlightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Paint getWebPaint() {
        return mWebPaint;
    }

    @Override
    public void initBuffers() {


    }

    @Override
    public void drawData(Canvas c) {

        RadarData radarData = mChart.getData();

        int mostEntries = radarData.getMaxEntryCountSet().getEntryCount();

        for (IRadarDataSet set : radarData.getDataSets()) {

            if (set.isVisible()) {
                drawDataSet(c, set, mostEntries);
            }
        }
    }

    protected void drawDataSet(Canvas c, IRadarDataSet dataSet, int mostEntries) {

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);
        Path surface = mDrawDataSetSurfacePathBuffer;
        surface.reset();

        boolean hasMovedToPoint = false;

        for (int j = 0; j < dataSet.getEntryCount(); j++) {

            mRenderPaint.setColor(dataSet.getColor(j));

            RadarEntry e = dataSet.getEntryForIndex(j);

            Utils.getPosition(
                    center,
                    (e.getY() - mChart.getYChartMin()) * factor * phaseY,
                    sliceangle * j * phaseX + mChart.getRotationAngle(), pOut);

            if (Float.isNaN(pOut.x))
                continue;

            if (!hasMovedToPoint) {
                surface.moveTo(pOut.x, pOut.y);
                hasMovedToPoint = true;
            } else
                surface.lineTo(pOut.x, pOut.y);
        }

        if (dataSet.getEntryCount() > mostEntries) {

            surface.lineTo(center.x, center.y);
        }

        surface.close();

        if (dataSet.isDrawFilledEnabled()) {

            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {

                drawFilledPath(c, surface, drawable);
            } else {

                drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }

        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setStyle(Paint.Style.STROKE);


        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255)
            c.drawPath(surface, mRenderPaint);

        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
    }

    @Override
    public void drawValues(Canvas c) {

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);
        ChartPointF pIcon = ChartPointF.getInstance(0, 0);

        float yoffset = Utils.convertDpToPixel(5f);

        for (int i = 0; i < mChart.getData().getDataSetCount(); i++) {

            IRadarDataSet dataSet = mChart.getData().getDataSetByIndex(i);

            if (!shouldDrawValues(dataSet))
                continue;


            applyValueTextStyle(dataSet);

            ChartPointF iconsOffset = ChartPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

            for (int j = 0; j < dataSet.getEntryCount(); j++) {

                RadarEntry entry = dataSet.getEntryForIndex(j);

                Utils.getPosition(
                        center,
                        (entry.getY() - mChart.getYChartMin()) * factor * phaseY,
                        sliceangle * j * phaseX + mChart.getRotationAngle(),
                        pOut);

                if (dataSet.isDrawValuesEnabled()) {
                    drawValue(c,
                            dataSet.getValueFormatter(),
                            entry.getY(),
                            entry,
                            i,
                            pOut.x,
                            pOut.y - yoffset,
                            dataSet.getValueTextColor
                                    (j));
                }

                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                    Drawable icon = entry.getIcon();

                    Utils.getPosition(
                            center,
                            (entry.getY()) * factor * phaseY + iconsOffset.y,
                            sliceangle * j * phaseX + mChart.getRotationAngle(),
                            pIcon);


                    pIcon.y += iconsOffset.x;

                    Utils.drawImage(
                            c,
                            icon,
                            (int) pIcon.x,
                            (int) pIcon.y,
                            icon.getIntrinsicWidth(),
                            icon.getIntrinsicHeight());
                }
            }

            ChartPointF.recycleInstance(iconsOffset);
        }

        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
        ChartPointF.recycleInstance(pIcon);
    }

    @Override
    public void drawExtras(Canvas c) {
        drawWeb(c);
    }

    protected void drawWeb(Canvas c) {

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();
        float rotationangle = mChart.getRotationAngle();

        ChartPointF center = mChart.getCenterOffsets();


        mWebPaint.setStrokeWidth(mChart.getWebLineWidth());
        mWebPaint.setColor(mChart.getWebColor());
        mWebPaint.setAlpha(mChart.getWebAlpha());

        final int xIncrements = 1 + mChart.getSkipWebLineCount();
        int maxEntryCount = mChart.getData().getMaxEntryCountSet().getEntryCount();

        ChartPointF p = ChartPointF.getInstance(0, 0);
        for (int i = 0; i < maxEntryCount; i += xIncrements) {

            Utils.getPosition(
                    center,
                    mChart.getYRange() * factor,
                    sliceangle * i + rotationangle,
                    p);

            c.drawLine(center.x, center.y, p.x, p.y, mWebPaint);
        }
        ChartPointF.recycleInstance(p);


        mWebPaint.setStrokeWidth(mChart.getWebLineWidthInner());
        mWebPaint.setColor(mChart.getWebColorInner());
        mWebPaint.setAlpha(mChart.getWebAlpha());

        int labelCount = mChart.getYAxis().mEntryCount;

        ChartPointF p1out = ChartPointF.getInstance(0, 0);
        ChartPointF p2out = ChartPointF.getInstance(0, 0);
        for (int j = 0; j < labelCount; j++) {

            for (int i = 0; i < mChart.getData().getEntryCount(); i++) {

                float r = (mChart.getYAxis().mEntries[j] - mChart.getYChartMin()) * factor;

                Utils.getPosition(center, r, sliceangle * i + rotationangle, p1out);
                Utils.getPosition(center, r, sliceangle * (i + 1) + rotationangle, p2out);

                c.drawLine(p1out.x, p1out.y, p2out.x, p2out.y, mWebPaint);


            }
        }
        ChartPointF.recycleInstance(p1out);
        ChartPointF.recycleInstance(p2out);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        float sliceangle = mChart.getSliceAngle();


        float factor = mChart.getFactor();

        ChartPointF center = mChart.getCenterOffsets();
        ChartPointF pOut = ChartPointF.getInstance(0, 0);

        RadarData radarData = mChart.getData();

        for (Highlight high : indices) {

            IRadarDataSet set = radarData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            RadarEntry e = set.getEntryForIndex((int) high.getX());

            if (!isInBoundsX(e, set))
                continue;

            float y = (e.getY() - mChart.getYChartMin());

            Utils.getPosition(center,
                    y * factor * mAnimator.getPhaseY(),
                    sliceangle * high.getX() * mAnimator.getPhaseX() + mChart.getRotationAngle(),
                    pOut);

            high.setDraw(pOut.x, pOut.y);


            drawHighlightLines(c, pOut.x, pOut.y, set);

            if (set.isDrawHighlightCircleEnabled()) {

                if (!Float.isNaN(pOut.x) && !Float.isNaN(pOut.y)) {

                    int strokeColor = set.getHighlightCircleStrokeColor();
                    if (strokeColor == ColorTemplate.COLOR_NONE) {
                        strokeColor = set.getColor(0);
                    }

                    if (set.getHighlightCircleStrokeAlpha() < 255) {
                        strokeColor = ColorTemplate.colorWithAlpha(strokeColor, set.getHighlightCircleStrokeAlpha());
                    }

                    drawHighlightCircle(c,
                            pOut,
                            set.getHighlightCircleInnerRadius(),
                            set.getHighlightCircleOuterRadius(),
                            set.getHighlightCircleFillColor(),
                            strokeColor,
                            set.getHighlightCircleStrokeWidth());
                }
            }
        }

        ChartPointF.recycleInstance(center);
        ChartPointF.recycleInstance(pOut);
    }

    public void drawHighlightCircle(Canvas c,
                                    ChartPointF point,
                                    float innerRadius,
                                    float outerRadius,
                                    int fillColor,
                                    int strokeColor,
                                    float strokeWidth) {
        c.save();

        outerRadius = Utils.convertDpToPixel(outerRadius);
        innerRadius = Utils.convertDpToPixel(innerRadius);

        if (fillColor != ColorTemplate.COLOR_NONE) {
            Path p = mDrawHighlightCirclePathBuffer;
            p.reset();
            p.addCircle(point.x, point.y, outerRadius, Path.Direction.CW);
            if (innerRadius > 0.f) {
                p.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW);
            }
            mHighlightCirclePaint.setColor(fillColor);
            mHighlightCirclePaint.setStyle(Paint.Style.FILL);
            c.drawPath(p, mHighlightCirclePaint);
        }

        if (strokeColor != ColorTemplate.COLOR_NONE) {
            mHighlightCirclePaint.setColor(strokeColor);
            mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
            mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
            c.drawCircle(point.x, point.y, outerRadius, mHighlightCirclePaint);
        }

        c.restore();
    }
}
