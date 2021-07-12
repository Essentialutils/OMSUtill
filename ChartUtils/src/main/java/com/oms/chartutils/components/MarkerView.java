package com.oms.chartutils.components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.oms.chartutils.charts.Chart;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.utils.ChartPointF;

import java.lang.ref.WeakReference;


public class MarkerView extends RelativeLayout implements IMarker {

    private ChartPointF mOffset = new ChartPointF();
    private final ChartPointF mOffset2 = new ChartPointF();
    private WeakReference<Chart> mWeakChart;


    public MarkerView(Context context, int layoutResource) {
        super(context);
        setupLayoutResource(layoutResource);
    }


    private void setupLayoutResource(int layoutResource) {

        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflated.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
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

        float width = getWidth();
        float height = getHeight();

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

        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());

    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {

        ChartPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        int saveId = canvas.save();

        canvas.translate(posX + offset.x, posY + offset.y);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}
