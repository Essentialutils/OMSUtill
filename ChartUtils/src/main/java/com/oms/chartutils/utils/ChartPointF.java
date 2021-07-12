package com.oms.chartutils.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class ChartPointF extends ObjectPool.Poolable {

    public static final Parcelable.Creator<ChartPointF> CREATOR = new Parcelable.Creator<ChartPointF>() {

        public ChartPointF createFromParcel(Parcel in) {
            ChartPointF r = new ChartPointF(0, 0);
            r.my_readFromParcel(in);
            return r;
        }


        public ChartPointF[] newArray(int size) {
            return new ChartPointF[size];
        }
    };
    private static final ObjectPool<ChartPointF> pool;

    static {
        pool = ObjectPool.create(32, new ChartPointF(0, 0));
        pool.setReplenishPercentage(0.5f);
    }

    public float x;
    public float y;

    public ChartPointF() {
    }

    public ChartPointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static ChartPointF getInstance(float x, float y) {
        ChartPointF result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static ChartPointF getInstance() {
        return pool.get();
    }

    public static ChartPointF getInstance(ChartPointF copy) {
        ChartPointF result = pool.get();
        result.x = copy.x;
        result.y = copy.y;
        return result;
    }

    public static void recycleInstance(ChartPointF instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<ChartPointF> instances) {
        pool.recycle(instances);
    }

    public void my_readFromParcel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Override
    protected ObjectPool.Poolable instantiate() {
        return new ChartPointF(0, 0);
    }
}