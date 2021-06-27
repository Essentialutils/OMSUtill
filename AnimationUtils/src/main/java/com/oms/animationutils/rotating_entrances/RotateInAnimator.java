package com.oms.animationutils.rotating_entrances;

import android.animation.ObjectAnimator;
import android.view.View;

import com.oms.animationutils.BaseViewAnimator;

public class RotateInAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotation", -200, 0),
                ObjectAnimator.ofFloat(target, "alpha", 0, 1)
        );
    }
}
