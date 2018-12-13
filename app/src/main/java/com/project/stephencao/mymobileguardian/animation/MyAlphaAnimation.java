package com.project.stephencao.mymobileguardian.animation;

import android.view.animation.AlphaAnimation;

/**
 * A shortcut for adding alpha animation
 */
public class MyAlphaAnimation {
    private long duration;
    private float from;
    private float to;

    public MyAlphaAnimation(long duration, float from, float to) {
        this.duration = duration;
        this.from = from;
        this.to = to;
    }

    public AlphaAnimation addAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(from,to);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }
}
