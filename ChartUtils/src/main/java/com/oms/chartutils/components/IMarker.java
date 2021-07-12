package com.oms.chartutils.components;

import android.graphics.Canvas;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.utils.ChartPointF;

public interface IMarker {


    ChartPointF getOffset();


    ChartPointF getOffsetForDrawingAtPoint(float posX, float posY);


    void refreshContent(Entry e, Highlight highlight);


    void draw(Canvas canvas, float posX, float posY);
}
