package com.oms.chartutils.renderer;

import com.oms.chartutils.utils.ViewPortHandler;


public abstract class Renderer {


    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
