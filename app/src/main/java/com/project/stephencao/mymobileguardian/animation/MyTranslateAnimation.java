package com.project.stephencao.mymobileguardian.animation;

import android.view.animation.TranslateAnimation;

public class MyTranslateAnimation {
    private float toXValue;
    private float toYValue;
    private long duration;

    public MyTranslateAnimation(float toXValue, float toYValue, long duration) {
        this.toXValue = toXValue;
        this.toYValue = toYValue;
        this.duration = duration;
    }

    public TranslateAnimation addTranslateAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,
                0.0f, TranslateAnimation.RELATIVE_TO_SELF, toXValue,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, toYValue);
        translateAnimation.setDuration(duration);
        return translateAnimation;
    }
}
