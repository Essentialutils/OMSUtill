package com.oms.utill.loading;

import com.oms.utill.loading.sprite.Sprite;
import com.oms.utill.loading.style.ChasingDots;
import com.oms.utill.loading.style.Circle;
import com.oms.utill.loading.style.CubeGrid;
import com.oms.utill.loading.style.DoubleBounce;
import com.oms.utill.loading.style.FadingCircle;
import com.oms.utill.loading.style.FoldingCube;
import com.oms.utill.loading.style.MultiplePulse;
import com.oms.utill.loading.style.MultiplePulseRing;
import com.oms.utill.loading.style.Pulse;
import com.oms.utill.loading.style.PulseRing;
import com.oms.utill.loading.style.RotatingCircle;
import com.oms.utill.loading.style.RotatingPlane;
import com.oms.utill.loading.style.ThreeBounce;
import com.oms.utill.loading.style.WanderingCubes;
import com.oms.utill.loading.style.Wave;

public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
