package com.project.stephencao.mymobileguardian.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.activity.SmokeBackgroundActivity;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

import java.util.HashMap;
import java.util.Map;

public class RocketManService extends Service {
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private final WindowManager.LayoutParams params = mParams;
    private Point outSize;
    private View mRocketToastView;
    private WindowManager mWindowManager;
    private ImageView mImageView;
    private AnimationDrawable mAnimationDrawable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            params.y = (int) msg.obj;
            mWindowManager.updateViewLayout(mRocketToastView, params);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        outSize = new Point();
        mWindowManager.getDefaultDisplay().getSize(outSize);
        showRocket();

        super.onCreate();
    }

    private void showRocket() {
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.LEFT + Gravity.TOP; // at top left corner
        params.type = WindowManager.LayoutParams.TYPE_PHONE; // show the toast when phone call is coming
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mRocketToastView = View.inflate(getApplicationContext(), R.layout.view_rocket_man, null);
        mImageView = mRocketToastView.findViewById(R.id.iv_rocket_man);

        String rocketManStyle = SharePreferencesUtil.getString(getApplicationContext(), ConstantValues.ROCKET_MAN_STYLE);
        Map<String,Integer> rocketManStyleMap = new HashMap<>();
        rocketManStyleMap.put("Yui Hantano",R.drawable.rocket_man_anime_yui);
        rocketManStyleMap.put("Kazuha",R.drawable.rocket_man_anime_kazuha);
        if(rocketManStyle==null){
            mImageView.setBackgroundResource(R.drawable.rocket_man_anime_yui);
        }
        else{
            mImageView.setBackgroundResource(rocketManStyleMap.get(rocketManStyle));
        }
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP: {

                        if (params.x > 250 && params.x < 350 && params.y >= 720) {
                            launchRocket();
                            Intent intent = new Intent(getApplicationContext(), SmokeBackgroundActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int distanceX = moveX - startX;
                        int distanceY = moveY - startY;

                        params.x = params.x + distanceX;
                        params.y = params.y + distanceY;

                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > outSize.x - mRocketToastView.getWidth()) {
                            params.x = outSize.x - mRocketToastView.getWidth();
                        }
                        if (params.y > outSize.y - mRocketToastView.getHeight() - 22) {
                            params.y = outSize.y - mRocketToastView.getHeight() - 22;
                        }
                        mWindowManager.updateViewLayout(mRocketToastView, params);

                        startX = moveX;
                        startY = moveY;
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                }
                return true;// false for onClick and onTouch exist together
            }
        });
        mAnimationDrawable = (AnimationDrawable) mImageView.getBackground();
        mAnimationDrawable.start();
        mWindowManager.addView(mRocketToastView, params);
    }

    private void launchRocket() {
        new Thread(new Runnable() {
            @Override
            public void run() { // main thread cannot be blocked
                for (int i = 0; i <= 10; i++) {
                    int height = outSize.y * 3 / 4 - outSize.y * 3 * i / 40;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.obj = height;
                    handler.sendMessage(message);
                }
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null && mRocketToastView != null) {
            mWindowManager.removeView(mRocketToastView);
        }
        super.onDestroy();
    }

}
