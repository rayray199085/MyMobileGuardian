package com.project.stephencao.mymobileguardian.view;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;

public class MySettingStylesView extends RelativeLayout {
    private TextView mStyleTitleTextView, mStyleDescriptionTextView;
    private ImageView mImageView;

    public MySettingStylesView(Context context) {
        this(context, null);
    }

    public MySettingStylesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySettingStylesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_settings_styles, this);
        mStyleTitleTextView = findViewById(R.id.tv_title_setting_styles);
        mStyleDescriptionTextView = findViewById(R.id.tv_description_setting_styles);
        mImageView = findViewById(R.id.iv_styles_setting);
    }

    public void setStyleTitleContent(String content){
        mStyleTitleTextView.setText(content);
    }

    public void setStyleDescriptionTextView(String content){
        mStyleDescriptionTextView.setText(content);
    }
    public void setStyleIcon(int id){
        mImageView.setBackgroundResource(id);
    }


}
