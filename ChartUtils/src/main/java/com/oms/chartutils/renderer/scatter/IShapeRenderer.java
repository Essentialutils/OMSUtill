package com.oms.chartutils.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.oms.chartutils.interfaces.datasets.IScatterDataSet;
import com.oms.chartutils.utils.ViewPortHandler;


public interface IShapeRenderer {


    void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                     float posX, float posY, Paint renderPaint);
}
