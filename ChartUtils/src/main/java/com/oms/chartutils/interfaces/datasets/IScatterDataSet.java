package com.oms.chartutils.interfaces.datasets;

import com.oms.chartutils.data.Entry;
import com.oms.chartutils.renderer.scatter.IShapeRenderer;


public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {


    float getScatterShapeSize();


    float getScatterShapeHoleRadius();


    int getScatterShapeHoleColor();


    IShapeRenderer getShapeRenderer();
}
