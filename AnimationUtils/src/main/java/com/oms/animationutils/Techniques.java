package com.oms.animationutils;

import com.oms.animationutils.attention.BounceAnimator;
import com.oms.animationutils.attention.FlashAnimator;
import com.oms.animationutils.attention.PulseAnimator;
import com.oms.animationutils.attention.RubberBandAnimator;
import com.oms.animationutils.attention.ShakeAnimator;
import com.oms.animationutils.attention.StandUpAnimator;
import com.oms.animationutils.attention.SwingAnimator;
import com.oms.animationutils.attention.TadaAnimator;
import com.oms.animationutils.attention.WaveAnimator;
import com.oms.animationutils.attention.WobbleAnimator;
import com.oms.animationutils.bouncing_entrances.BounceInAnimator;
import com.oms.animationutils.bouncing_entrances.BounceInDownAnimator;
import com.oms.animationutils.bouncing_entrances.BounceInLeftAnimator;
import com.oms.animationutils.bouncing_entrances.BounceInRightAnimator;
import com.oms.animationutils.bouncing_entrances.BounceInUpAnimator;
import com.oms.animationutils.fading_entrances.FadeInAnimator;
import com.oms.animationutils.fading_entrances.FadeInDownAnimator;
import com.oms.animationutils.fading_entrances.FadeInLeftAnimator;
import com.oms.animationutils.fading_entrances.FadeInRightAnimator;
import com.oms.animationutils.fading_entrances.FadeInUpAnimator;
import com.oms.animationutils.fading_exits.FadeOutAnimator;
import com.oms.animationutils.fading_exits.FadeOutDownAnimator;
import com.oms.animationutils.fading_exits.FadeOutLeftAnimator;
import com.oms.animationutils.fading_exits.FadeOutRightAnimator;
import com.oms.animationutils.fading_exits.FadeOutUpAnimator;
import com.oms.animationutils.flippers.FlipInXAnimator;
import com.oms.animationutils.flippers.FlipInYAnimator;
import com.oms.animationutils.flippers.FlipOutXAnimator;
import com.oms.animationutils.flippers.FlipOutYAnimator;
import com.oms.animationutils.rotating_entrances.RotateInAnimator;
import com.oms.animationutils.rotating_entrances.RotateInDownLeftAnimator;
import com.oms.animationutils.rotating_entrances.RotateInDownRightAnimator;
import com.oms.animationutils.rotating_entrances.RotateInUpLeftAnimator;
import com.oms.animationutils.rotating_entrances.RotateInUpRightAnimator;
import com.oms.animationutils.rotating_exits.RotateOutAnimator;
import com.oms.animationutils.rotating_exits.RotateOutDownLeftAnimator;
import com.oms.animationutils.rotating_exits.RotateOutDownRightAnimator;
import com.oms.animationutils.rotating_exits.RotateOutUpLeftAnimator;
import com.oms.animationutils.rotating_exits.RotateOutUpRightAnimator;
import com.oms.animationutils.sliders.SlideInDownAnimator;
import com.oms.animationutils.sliders.SlideInLeftAnimator;
import com.oms.animationutils.sliders.SlideInRightAnimator;
import com.oms.animationutils.sliders.SlideInUpAnimator;
import com.oms.animationutils.sliders.SlideOutDownAnimator;
import com.oms.animationutils.sliders.SlideOutLeftAnimator;
import com.oms.animationutils.sliders.SlideOutRightAnimator;
import com.oms.animationutils.sliders.SlideOutUpAnimator;
import com.oms.animationutils.specials.HingeAnimator;
import com.oms.animationutils.specials.RollInAnimator;
import com.oms.animationutils.specials.RollOutAnimator;
import com.oms.animationutils.specials.in.DropOutAnimator;
import com.oms.animationutils.specials.in.LandingAnimator;
import com.oms.animationutils.specials.out.TakingOffAnimator;
import com.oms.animationutils.zooming_entrances.ZoomInAnimator;
import com.oms.animationutils.zooming_entrances.ZoomInDownAnimator;
import com.oms.animationutils.zooming_entrances.ZoomInLeftAnimator;
import com.oms.animationutils.zooming_entrances.ZoomInRightAnimator;
import com.oms.animationutils.zooming_entrances.ZoomInUpAnimator;
import com.oms.animationutils.zooming_exits.ZoomOutAnimator;
import com.oms.animationutils.zooming_exits.ZoomOutDownAnimator;
import com.oms.animationutils.zooming_exits.ZoomOutLeftAnimator;
import com.oms.animationutils.zooming_exits.ZoomOutRightAnimator;
import com.oms.animationutils.zooming_exits.ZoomOutUpAnimator;

public enum Techniques {

    DropOut(DropOutAnimator.class),
    Landing(LandingAnimator.class),
    TakingOff(TakingOffAnimator.class),

    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    Hinge(HingeAnimator.class),
    RollIn(RollInAnimator.class),
    RollOut(RollOutAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),
    FlipInY(FlipInYAnimator.class),
    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);


    private final Class animatorClazz;

    Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
