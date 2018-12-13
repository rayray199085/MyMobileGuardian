package com.project.stephencao.mymobileguardian.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.project.stephencao.mymobileguardian.R;

public class ClearSDCardDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.soon);
        setContentView(imageView);
    }
}
