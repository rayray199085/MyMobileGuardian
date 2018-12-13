package com.project.stephencao.mymobileguardian.activity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

public class ToastIncomingCallLocationActivity extends Activity {
    private ImageView mImageView;
    private Button mTopButton, mBottomButton;
    private Point mOutSize;
    private long[] mHits = new long[2];
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_incoming_call_location);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        initUI();
    }

    @Override
    protected void onResume() {
        int left = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_POSITION_X);
        int top = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_POSITION_Y);
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        mImageView.setLayoutParams(layoutParams);
        int height = mImageView.getBottom() - mImageView.getTop();
        if (height / 2 + top > (mOutSize.y - 22) / 2) {
            mTopButton.setVisibility(View.VISIBLE);
            mBottomButton.setVisibility(View.INVISIBLE);
        } else {
            mTopButton.setVisibility(View.INVISIBLE);
            mBottomButton.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void initUI() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mOutSize = new Point();
        windowManager.getDefaultDisplay().getSize(mOutSize);
        mImageView = findViewById(R.id.iv_toast_location_drag);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    int width = mImageView.getWidth();
                    layoutParams.leftMargin = mOutSize.x / 2 - width / 2;
                    layoutParams.topMargin = (mOutSize.y - 22) / 2;
                    mImageView.setLayoutParams(layoutParams);
                    SharePreferencesUtil.recordInteger(ToastIncomingCallLocationActivity.this,
                            ConstantValues.TOAST_POSITION_X, layoutParams.leftMargin);
                    SharePreferencesUtil.recordInteger(ToastIncomingCallLocationActivity.this,
                            ConstantValues.TOAST_POSITION_Y, layoutParams.topMargin);
                }
            }
        });
        mTopButton = findViewById(R.id.btn_toast_location_top);
        mBottomButton = findViewById(R.id.btn_toast_location_bottom);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP: {
                        SharePreferencesUtil.recordInteger(ToastIncomingCallLocationActivity.this,
                                ConstantValues.TOAST_POSITION_X, mImageView.getLeft());
                        SharePreferencesUtil.recordInteger(ToastIncomingCallLocationActivity.this,
                                ConstantValues.TOAST_POSITION_Y, mImageView.getTop());
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int distanceX = moveX - startX;
                        int distanceY = moveY - startY;
                        if ( mImageView.getTop() + distanceY > (mOutSize.y - 22) / 2) {
                            mBottomButton.setVisibility(View.INVISIBLE);
                            mTopButton.setVisibility(View.VISIBLE);
                        } else {
                            mTopButton.setVisibility(View.INVISIBLE);
                            mBottomButton.setVisibility(View.VISIBLE);
                        }
                        if (mImageView.getLeft() + distanceX < 0 || mImageView.getRight() + distanceX > mOutSize.x ||
                                mImageView.getTop() + distanceY < 0 || mImageView.getBottom() + distanceY > mOutSize.y - 22) {
                            return false;
                        }

                        mImageView.layout(mImageView.getLeft() + distanceX,
                                mImageView.getTop() + distanceY,
                                +mImageView.getRight() + distanceX,
                                mImageView.getBottom() + distanceY);
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
                return false;// false for onClick and onTouch exist together
            }
        });


    }

}
