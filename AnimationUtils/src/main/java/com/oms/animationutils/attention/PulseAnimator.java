package com.oms.animationutils.attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.oms.animationutils.BaseViewAnimator;

public class PulseAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.1f, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.1f, 1)
        );
    }
}
