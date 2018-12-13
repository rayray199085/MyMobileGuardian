package com.project.stephencao.mymobileguardian.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;

/**
 * A shortcut for adding alpha animation
 */
public class MyScaleAnimation {
    private long duration;
    private float fromX;
    private float fromY;

    public MyScaleAnimation(long duration, float fromX, float fromY) {
        this.duration = duration;
        this.fromX = fromX;
        this.fromY = fromY;
    }

    public ScaleAnimation addScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, 1.0f,
                fromY, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }
}
