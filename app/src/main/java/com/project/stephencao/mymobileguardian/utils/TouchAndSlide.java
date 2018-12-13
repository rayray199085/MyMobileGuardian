package com.project.stephencao.mymobileguardian.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class TouchAndSlide {
    public static void jump(View view, final Button nextButton, final Button prevButton) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float endX = 0;
            float startX = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        startX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        endX = event.getX();
                        if ((nextButton != null) && (startX - endX > 100)) {
                            nextButton.performClick();

                        } else if ((prevButton != null) && (startX - endX < -100)) {
                            prevButton.performClick();
                        }
                        break;
                    }
                }
                return true;
            }
        });

    }
}
