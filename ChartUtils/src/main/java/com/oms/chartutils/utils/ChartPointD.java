package com.oms.chartutils.utils;

import java.util.List;


public class ChartPointD extends ObjectPool.Poolable {

    private static final ObjectPool<ChartPointD> pool;

    static {
        pool = ObjectPool.create(64, new ChartPointD(0, 0));
        pool.setReplenishPercentage(0.5f);
    }

    public double x;
    public double y;

    private ChartPointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static ChartPointD getInstance(double x, double y) {
        ChartPointD result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(ChartPointD instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<ChartPointD> instances) {
        pool.recycle(instances);
    }

    protected ObjectPool.Poolable instantiate() {
        return new ChartPointD(0, 0);
    }

    public String toString() {
        return "ChartPointD, x: " + x + ", y: " + y;
    }
}