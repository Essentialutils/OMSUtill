package com.oms.utill.loading;
//1.4.0 target

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.oms.utill.R;
import com.oms.utill.loading.sprite.Sprite;

public class LoaderView extends ProgressBar {

    private final Style mStyle;
    private int mColor;
    private Sprite mSprite;

    public LoaderView(Context context) {
        this(context, null);
    }

    public LoaderView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.LoaderViewStyle);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.LoaderView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoaderView, defStyleAttr,
                defStyleRes);
        mStyle = Style.values()[a.getInt(R.styleable.LoaderView_Loader_Style, 0)];
        mColor = a.getColor(R.styleable.LoaderView_Loader_Color, Color.WHITE);
        a.recycle();
        init();
        setIndeterminate(true);
    }

    private void init() {
        Sprite sprite = SpriteFactory.create(mStyle);
        sprite.setColor(mColor);
        setIndeterminateDrawable(sprite);
    }

    @Override
    public Sprite getIndeterminateDrawable() {
        return mSprite;
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (!(d instanceof Sprite)) {
            throw new IllegalArgumentException("this d must be instanceof Sprite");
        }
        setIndeterminateDrawable((Sprite) d);
    }

    public void setIndeterminateDrawable(Sprite d) {
        super.setIndeterminateDrawable(d);
        mSprite = d;
        if (mSprite.getColor() == 0) {

            mSprite.setColor(mColor);
        }
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        if (getVisibility() == VISIBLE) {

            mSprite.start();
        }
    }

    public void setColor(int color) {
        this.mColor = color;
        if (mSprite != null) {

            mSprite.setColor(color);
        }
        invalidate();
    }

    @Override
    public void unscheduleDrawable(Drawable who) {
        super.unscheduleDrawable(who);
        if (who instanceof Sprite) {

            ((Sprite) who).stop();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (mSprite != null && getVisibility() == VISIBLE) {

                mSprite.start();
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == View.SCREEN_STATE_OFF) {

            if (mSprite != null) {

                mSprite.stop();
            }
        }
    }
}
