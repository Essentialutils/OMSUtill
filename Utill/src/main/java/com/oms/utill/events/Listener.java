package com.oms.utill.events;

public interface Listener {
    enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}
    void onClick(DrawablePosition target);
}
