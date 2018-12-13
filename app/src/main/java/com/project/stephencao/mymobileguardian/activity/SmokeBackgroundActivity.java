package com.project.stephencao.mymobileguardian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.animation.MyAlphaAnimation;

public class SmokeBackgroundActivity extends Activity {
    private ImageView imageView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke_background);
        imageView = findViewById(R.id.iv_rocket_smoke_background);
        AlphaAnimation alphaAnimation = new MyAlphaAnimation(1000, 0.0f, 1.0f).addAlphaAnimation();
        imageView.setAnimation(alphaAnimation);
        Message message = Message.obtain();
        handler.sendEmptyMessageDelayed(0,1500);
    }

}
