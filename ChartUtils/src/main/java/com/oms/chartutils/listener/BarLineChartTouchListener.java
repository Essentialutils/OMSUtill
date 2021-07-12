package com.oms.chartutils.listener;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.oms.chartutils.charts.BarLineChartBase;
import com.oms.chartutils.charts.HorizontalBarChart;
import com.oms.chartutils.data.BarLineScatterCandleBubbleData;
import com.oms.chartutils.data.Entry;
import com.oms.chartutils.highlight.Highlight;
import com.oms.chartutils.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.oms.chartutils.interfaces.datasets.IDataSet;
import com.oms.chartutils.utils.ChartPointF;
import com.oms.chartutils.utils.Utils;
import com.oms.chartutils.utils.ViewPortHandler;


public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<?
        extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>> {


    private final Matrix mSavedMatrix = new Matrix();
    private final ChartPointF mTouchStartPoint = ChartPointF.getInstance(0, 0);
    private final ChartPointF mTouchPointCenter = ChartPointF.getInstance(0, 0);
    private final ChartPointF mDecelerationCurrentPoint = ChartPointF.getInstance(0, 0);
    private final ChartPointF mDecelerationVelocity = ChartPointF.getInstance(0, 0);
    private final float mMinScalePointerDistance;
    private Matrix mMatrix = new Matrix();
    private float mSavedXDist = 1f;
    private float mSavedYDist = 1f;
    private float mSavedDist = 1f;
    private IDataSet mClosestDataSetToTouch;
    private VelocityTracker mVelocityTracker;
    private long mDecelerationLastTime = 0;
    private float mDragTriggerDist;


    public BarLineChartTouchListener(BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends
            IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart, Matrix touchMatrix, float dragTriggerDistance) {
        super(chart);
        this.mMatrix = touchMatrix;

        this.mDragTriggerDist = Utils.convertDpToPixel(dragTriggerDistance);

        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);
    }

    private static void midPoint(ChartPointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.x = (x / 2f);
        point.y = (y / 2f);
    }

    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private static float getXDist(MotionEvent e) {
        float x = Math.abs(e.getX(0) - e.getX(1));
        return x;
    }

    private static float getYDist(MotionEvent e) {
        float y = Math.abs(e.getY(0) - e.getY(1));
        return y;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        if (mTouchMode == NONE) {
            mGestureDetector.onTouchEvent(event);
        }

        if (!mChart.isDragEnabled() && (!mChart.isScaleXEnabled() && !mChart.isScaleYEnabled()))
            return true;


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                startAction(event);

                stopDeceleration();

                saveTouchStart(event);

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                if (event.getPointerCount() >= 2) {

                    mChart.disableScroll();

                    saveTouchStart(event);


                    mSavedXDist = getXDist(event);


                    mSavedYDist = getYDist(event);


                    mSavedDist = spacing(event);

                    if (mSavedDist > 10f) {

                        if (mChart.isPinchZoomEnabled()) {
                            mTouchMode = PINCH_ZOOM;
                        } else {
                            if (mChart.isScaleXEnabled() != mChart.isScaleYEnabled()) {
                                mTouchMode = mChart.isScaleXEnabled() ? X_ZOOM : Y_ZOOM;
                            } else {
                                mTouchMode = mSavedXDist > mSavedYDist ? X_ZOOM : Y_ZOOM;
                            }
                        }
                    }


                    midPoint(mTouchPointCenter, event);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mTouchMode == DRAG) {

                    mChart.disableScroll();

                    float x = mChart.isDragXEnabled() ? event.getX() - mTouchStartPoint.x : 0.f;
                    float y = mChart.isDragYEnabled() ? event.getY() - mTouchStartPoint.y : 0.f;

                    performDrag(event, x, y);

                } else if (mTouchMode == X_ZOOM || mTouchMode == Y_ZOOM || mTouchMode == PINCH_ZOOM) {

                    mChart.disableScroll();

                    if (mChart.isScaleXEnabled() || mChart.isScaleYEnabled())
                        performZoom(event);

                } else if (mTouchMode == NONE
                        && Math.abs(distance(event.getX(), mTouchStartPoint.x, event.getY(),
                        mTouchStartPoint.y)) > mDragTriggerDist) {

                    if (mChart.isDragEnabled()) {

                        boolean shouldPan = !mChart.isFullyZoomedOut() ||
                                !mChart.hasNoDragOffset();

                        if (shouldPan) {

                            float distanceX = Math.abs(event.getX() - mTouchStartPoint.x);
                            float distanceY = Math.abs(event.getY() - mTouchStartPoint.y);


                            if ((mChart.isDragXEnabled() || distanceY >= distanceX) &&
                                    (mChart.isDragYEnabled() || distanceY <= distanceX)) {

                                mLastGesture = ChartGesture.DRAG;
                                mTouchMode = DRAG;
                            }

                        } else {

                            if (mChart.isHighlightPerDragEnabled()) {
                                mLastGesture = ChartGesture.DRAG;

                                if (mChart.isHighlightPerDragEnabled())
                                    performHighlightDrag(event);
                            }
                        }

                    }

                }
                break;

            case MotionEvent.ACTION_UP:

                final VelocityTracker velocityTracker = mVelocityTracker;
                final int pointerId = event.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, Utils.getMaximumFlingVelocity());
                final float velocityY = velocityTracker.getYVelocity(pointerId);
                final float velocityX = velocityTracker.getXVelocity(pointerId);

                if (Math.abs(velocityX) > Utils.getMinimumFlingVelocity() ||
                        Math.abs(velocityY) > Utils.getMinimumFlingVelocity()) {

                    if (mTouchMode == DRAG && mChart.isDragDecelerationEnabled()) {

                        stopDeceleration();

                        mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();

                        mDecelerationCurrentPoint.x = event.getX();
                        mDecelerationCurrentPoint.y = event.getY();

                        mDecelerationVelocity.x = velocityX;
                        mDecelerationVelocity.y = velocityY;

                        Utils.postInvalidateOnAnimation(mChart);

                    }
                }

                if (mTouchMode == X_ZOOM ||
                        mTouchMode == Y_ZOOM ||
                        mTouchMode == PINCH_ZOOM ||
                        mTouchMode == POST_ZOOM) {


                    mChart.calculateOffsets();
                    mChart.postInvalidate();
                }

                mTouchMode = NONE;
                mChart.enableScroll();

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                endAction(event);

                break;
            case MotionEvent.ACTION_POINTER_UP:
                Utils.velocityTrackerPointerUpCleanUpIfNecessary(event, mVelocityTracker);

                mTouchMode = POST_ZOOM;
                break;

            case MotionEvent.ACTION_CANCEL:

                mTouchMode = NONE;
                endAction(event);
                break;
        }


        mMatrix = mChart.getViewPortHandler().refresh(mMatrix, mChart, true);

        return true;
    }

    private void saveTouchStart(MotionEvent event) {

        mSavedMatrix.set(mMatrix);
        mTouchStartPoint.x = event.getX();
        mTouchStartPoint.y = event.getY();

        mClosestDataSetToTouch = mChart.getDataSetByTouchPoint(event.getX(), event.getY());
    }

    private void performDrag(MotionEvent event, float distanceX, float distanceY) {

        mLastGesture = ChartGesture.DRAG;

        mMatrix.set(mSavedMatrix);

        OnChartGestureListener l = mChart.getOnChartGestureListener();


        if (inverted()) {


            if (mChart instanceof HorizontalBarChart) {
                distanceX = -distanceX;
            } else {
                distanceY = -distanceY;
            }
        }

        mMatrix.postTranslate(distanceX, distanceY);

        if (l != null)
            l.onChartTranslate(event, distanceX, distanceY);
    }

    private void performZoom(MotionEvent event) {

        if (event.getPointerCount() >= 2) {

            OnChartGestureListener l = mChart.getOnChartGestureListener();


            float totalDist = spacing(event);

            if (totalDist > mMinScalePointerDistance) {


                ChartPointF t = getTrans(mTouchPointCenter.x, mTouchPointCenter.y);
                ViewPortHandler h = mChart.getViewPortHandler();


                if (mTouchMode == PINCH_ZOOM) {

                    mLastGesture = ChartGesture.PINCH_ZOOM;

                    float scale = totalDist / mSavedDist;

                    boolean isZoomingOut = (scale < 1);

                    boolean canZoomMoreX = isZoomingOut ?
                            h.canZoomOutMoreX() :
                            h.canZoomInMoreX();

                    boolean canZoomMoreY = isZoomingOut ?
                            h.canZoomOutMoreY() :
                            h.canZoomInMoreY();

                    float scaleX = (mChart.isScaleXEnabled()) ? scale : 1f;
                    float scaleY = (mChart.isScaleYEnabled()) ? scale : 1f;

                    if (canZoomMoreY || canZoomMoreX) {

                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(scaleX, scaleY, t.x, t.y);

                        if (l != null)
                            l.onChartScale(event, scaleX, scaleY);
                    }

                } else if (mTouchMode == X_ZOOM && mChart.isScaleXEnabled()) {

                    mLastGesture = ChartGesture.X_ZOOM;

                    float xDist = getXDist(event);
                    float scaleX = xDist / mSavedXDist;

                    boolean isZoomingOut = (scaleX < 1);
                    boolean canZoomMoreX = isZoomingOut ?
                            h.canZoomOutMoreX() :
                            h.canZoomInMoreX();

                    if (canZoomMoreX) {

                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(scaleX, 1f, t.x, t.y);

                        if (l != null)
                            l.onChartScale(event, scaleX, 1f);
                    }

                } else if (mTouchMode == Y_ZOOM && mChart.isScaleYEnabled()) {

                    mLastGesture = ChartGesture.Y_ZOOM;

                    float yDist = getYDist(event);
                    float scaleY = yDist / mSavedYDist;

                    boolean isZoomingOut = (scaleY < 1);
                    boolean canZoomMoreY = isZoomingOut ?
                            h.canZoomOutMoreY() :
                            h.canZoomInMoreY();

                    if (canZoomMoreY) {

                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(1f, scaleY, t.x, t.y);

                        if (l != null)
                            l.onChartScale(event, 1f, scaleY);
                    }
                }

                ChartPointF.recycleInstance(t);
            }
        }
    }

    private void performHighlightDrag(MotionEvent e) {

        Highlight h = mChart.getHighlightByTouchPoint(e.getX(), e.getY());

        if (h != null && !h.equalTo(mLastHighlighted)) {
            mLastHighlighted = h;
            mChart.highlightValue(h, true);
        }
    }

    public ChartPointF getTrans(float x, float y) {

        ViewPortHandler vph = mChart.getViewPortHandler();

        float xTrans = x - vph.offsetLeft();
        float yTrans = 0f;


        if (inverted()) {
            yTrans = -(y - vph.offsetTop());
        } else {
            yTrans = -(mChart.getMeasuredHeight() - y - vph.offsetBottom());
        }

        return ChartPointF.getInstance(xTrans, yTrans);
    }


    private boolean inverted() {
        return (mClosestDataSetToTouch == null && mChart.isAnyAxisInverted()) || (mClosestDataSetToTouch != null
                && mChart.isInverted(mClosestDataSetToTouch.getAxisDependency()));
    }


    public Matrix getMatrix() {
        return mMatrix;
    }


    public void setDragTriggerDist(float dragTriggerDistance) {
        this.mDragTriggerDist = Utils.convertDpToPixel(dragTriggerDistance);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        mLastGesture = ChartGesture.DOUBLE_TAP;

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null) {
            l.onChartDoubleTapped(e);
        }


        if (mChart.isDoubleTapToZoomEnabled() && mChart.getData().getEntryCount() > 0) {

            ChartPointF trans = getTrans(e.getX(), e.getY());

            float scaleX = mChart.isScaleXEnabled() ? 1.4f : 1f;
            float scaleY = mChart.isScaleYEnabled() ? 1.4f : 1f;

            mChart.zoom(scaleX, scaleY, trans.x, trans.y);

            if (mChart.isLogEnabled())
                Log.i("BarlineChartTouch", "Double-Tap, Zooming In, x: " + trans.x + ", y: "
                        + trans.y);

            if (l != null) {
                l.onChartScale(e, scaleX, scaleY);
            }

            ChartPointF.recycleInstance(trans);
        }

        return super.onDoubleTap(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {

        mLastGesture = ChartGesture.LONG_PRESS;

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null) {

            l.onChartLongPressed(e);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        mLastGesture = ChartGesture.SINGLE_TAP;

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null) {
            l.onChartSingleTapped(e);
        }

        if (!mChart.isHighlightPerTapEnabled()) {
            return false;
        }

        Highlight h = mChart.getHighlightByTouchPoint(e.getX(), e.getY());
        performHighlight(h, e);

        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        mLastGesture = ChartGesture.FLING;

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null) {
            l.onChartFling(e1, e2, velocityX, velocityY);
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }

    public void stopDeceleration() {
        mDecelerationVelocity.x = 0;
        mDecelerationVelocity.y = 0;
    }

    public void computeScroll() {

        if (mDecelerationVelocity.x == 0.f && mDecelerationVelocity.y == 0.f)
            return;

        final long currentTime = AnimationUtils.currentAnimationTimeMillis();

        mDecelerationVelocity.x *= mChart.getDragDecelerationFrictionCoef();
        mDecelerationVelocity.y *= mChart.getDragDecelerationFrictionCoef();

        final float timeInterval = (float) (currentTime - mDecelerationLastTime) / 1000.f;

        float distanceX = mDecelerationVelocity.x * timeInterval;
        float distanceY = mDecelerationVelocity.y * timeInterval;

        mDecelerationCurrentPoint.x += distanceX;
        mDecelerationCurrentPoint.y += distanceY;

        MotionEvent event = MotionEvent.obtain(currentTime, currentTime, MotionEvent.ACTION_MOVE, mDecelerationCurrentPoint.x,
                mDecelerationCurrentPoint.y, 0);

        float dragDistanceX = mChart.isDragXEnabled() ? mDecelerationCurrentPoint.x - mTouchStartPoint.x : 0.f;
        float dragDistanceY = mChart.isDragYEnabled() ? mDecelerationCurrentPoint.y - mTouchStartPoint.y : 0.f;

        performDrag(event, dragDistanceX, dragDistanceY);

        event.recycle();
        mMatrix = mChart.getViewPortHandler().refresh(mMatrix, mChart, false);

        mDecelerationLastTime = currentTime;

        if (Math.abs(mDecelerationVelocity.x) >= 0.01 || Math.abs(mDecelerationVelocity.y) >= 0.01)
            Utils.postInvalidateOnAnimation(mChart);
        else {


            mChart.calculateOffsets();
            mChart.postInvalidate();

            stopDeceleration();
        }
    }
}
