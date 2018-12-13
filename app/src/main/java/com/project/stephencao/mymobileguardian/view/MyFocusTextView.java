package com.project.stephencao.mymobileguardian.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Define a class which extends TextView has a marquee function.
 */
public class MyFocusTextView extends android.support.v7.widget.AppCompatTextView {

    public MyFocusTextView(Context context) {
        super(context);
    }

    public MyFocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
