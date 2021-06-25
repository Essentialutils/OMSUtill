package com.oms.utill.loading.style;

import android.animation.ValueAnimator;

import com.oms.utill.loading.animation.SpriteAnimatorBuilder;
import com.oms.utill.loading.sprite.CircleSprite;


public class Pulse extends CircleSprite {

    public Pulse() {
        setScale(0f);
    }

    @Override
    public ValueAnimator onCreateAnimation() {
        float[] fractions = new float[]{0f, 1f};
        return new SpriteAnimatorBuilder(this).
                scale(fractions, 0f, 1f).
                alpha(fractions, 255, 0).
                duration(1000).
                easeInOut(fractions)
                .build();
    }
}
