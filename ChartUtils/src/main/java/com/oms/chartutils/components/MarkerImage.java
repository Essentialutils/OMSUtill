package com.oms.chartutils.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.oms.chartutils.charts.Chart;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.FSize;

import java.lang.ref.WeakReference;


public class MarkerImage implements IMarker {

    private final Context mContext;
    private final Drawable mDrawable;

    private ChartPointF mOffset = new ChartPointF();
    private final ChartPointF mOffset2 = new ChartPointF();
    private WeakReference<Chart> mWeakChart;

    private FSize mSize = new FSize();
    private final Rect mDrawableBoundsCache = new Rect();


    public MarkerImage(Context context, int drawableResourceId) {
        mContext = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawable = mContext.getResources().getDrawable(drawableResourceId, null);
        } else {
            mDrawable = mContext.getResources().getDrawable(drawableResourceId);
        }
    }

    public void setOffset(float offsetX, float offsetY) {
        mOffset.x = offsetX;
        mOffset.y = offsetY;
    }

    @Override
    public ChartPointF getOffset() {
        return mOffset;
    }

    public void setOffset(ChartPointF offset) {
        mOffset = offset;

        if (mOffset == null) {
            mOffset = new ChartPointF();
        }
    }

    public FSize getSize() {
        return mSize;
    }

    public void setSize(FSize size) {
        mSize = size;

        if (mSize == null) {
            mSize = new FSize();
        }
    }

    public Chart getChartView() {
        return mWeakChart == null ? null : mWeakChart.get();
    }

    public void setChartView(Chart chart) {
        mWeakChart = new WeakReference<>(chart);
    }

    @Override
    public ChartPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        ChartPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = mSize.width;
        float height = mSize.height;

        if (width == 0.f && mDrawable != null) {
            width = mDrawable.getIntrinsicWidth();
        }
        if (height == 0.f && mDrawable != null) {
            height = mDrawable.getIntrinsicHeight();
        }

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX;
        } else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
            mOffset2.x = chart.getWidth() - posX - width;
        }

        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY;
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height;
        }

        return mOffset2;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {

        if (mDrawable == null) return;

        ChartPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        float width = mSize.width;
        float height = mSize.height;

        if (width == 0.f) {
            width = mDrawable.getIntrinsicWidth();
        }
        if (height == 0.f) {
            height = mDrawable.getIntrinsicHeight();
        }

        mDrawable.copyBounds(mDrawableBoundsCache);
        mDrawable.setBounds(
                mDrawableBoundsCache.left,
                mDrawableBoundsCache.top,
                mDrawableBoundsCache.left + (int) width,
                mDrawableBoundsCache.top + (int) height);

        int saveId = canvas.save();

        canvas.translate(posX + offset.x, posY + offset.y);
        mDrawable.draw(canvas);
        canvas.restoreToCount(saveId);

        mDrawable.setBounds(mDrawableBoundsCache);
    }
}
