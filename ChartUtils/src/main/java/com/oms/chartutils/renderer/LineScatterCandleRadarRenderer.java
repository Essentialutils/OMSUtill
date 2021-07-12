package com.oms.chartutils.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import com.oms.chartutils.animation.ChartAnimator;
import com.oms.chartutils.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.oms.chartutils.utils.ViewPortHandler;


public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {


    private final Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }


    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {


        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());


        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());


        if (set.isVerticalHighlightIndicatorEnabled()) {


            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }


        if (set.isHorizontalHighlightIndicatorEnabled()) {


            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }
}
