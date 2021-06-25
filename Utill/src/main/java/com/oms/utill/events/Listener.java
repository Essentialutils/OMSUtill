package com.oms.utill.events;

public interface Listener {
    interface DrawableClick {
        void onClick(DrawablePosition target);
        enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}
    }
}
