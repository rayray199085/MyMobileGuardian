package com.project.stephencao.mymobileguardian.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.animation.MyTranslateAnimation;
import com.project.stephencao.mymobileguardian.bean.AppsManagersListItems;
import com.project.stephencao.mymobileguardian.engine.AcquireAppsInfo;
import com.project.stephencao.mymobileguardian.engine.AppLockDao;

import java.util.ArrayList;
import java.util.List;

public class ApplicationLockActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mUnlockLinearLayout, mLockLinearLayout;
    private Button mUnlockButton, mLockButton;
    private TextView mUnlockAppsCount, mLockAppsCount;
    private ListView mUnlockAppsListView, mLockAppsListView;
    private List<AppsManagersListItems> mLockedAppsInfoList;
    private List<AppsManagersListItems> mUnlockedAppsInfoList;
    private List<AppsManagersListItems> mAppsInfoList;
    private boolean isUnlockedListDisplay;
    private MyAppLockListAdapter myLockedAppLockListAdapter;
    private MyAppLockListAdapter myUnlockedAppLockListAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                myLockedAppLockListAdapter = new MyAppLockListAdapter(true);
                myUnlockedAppLockListAdapter = new MyAppLockListAdapter(false);
                mLockAppsListView.setAdapter(myLockedAppLockListAdapter);
                mUnlockAppsListView.setAdapter(myUnlockedAppLockListAdapter);
            }

        }
    };

    class MyAppLockListAdapter extends BaseAdapter {
        private boolean isLocked;

        public MyAppLockListAdapter(boolean isLocked) {
            this.isLocked = isLocked;
        }

        @Override
        public int getCount() {
            if (isLocked) {
                mLockAppsCount.setText("Locked Applications Number: " + mLockedAppsInfoList.size());
                return mLockedAppsInfoList.size();
            } else {
                mUnlockAppsCount.setText("UnLocked Applications Number: " + mUnlockedAppsInfoList.size());
                return mUnlockedAppsInfoList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (isLocked) {
                return mLockedAppsInfoList.get(position);
            } else {
                return mUnlockedAppsInfoList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_app_lock_list_items, null);
                viewHolder.iconImageView = convertView.findViewById(R.id.iv_app_lock_icon);
                viewHolder.textView = convertView.findViewById(R.id.tv_app_lock_app_name);
                viewHolder.lockImageView = convertView.findViewById(R.id.iv_app_lock_lock_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final AppsManagersListItems appsManagersListItems;
            if (isLocked) {
                appsManagersListItems = mLockedAppsInfoList.get(position);
            } else {
                appsManagersListItems = mUnlockedAppsInfoList.get(position);
            }
            viewHolder.iconImageView.setBackgroundDrawable(appsManagersListItems.getAppIcon());
            viewHolder.textView.setText(appsManagersListItems.getAppName());
            if (isLocked) {
                viewHolder.lockImageView.setBackgroundResource(R.drawable.lock);
            } else {
                viewHolder.lockImageView.setBackgroundResource(R.drawable.unlock);
            }
            final View finalConvertView = convertView;
            viewHolder.lockImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLocked) {
                        TranslateAnimation translateAnimation = new MyTranslateAnimation(-1.0f, 0.0f, 500).addTranslateAnimation();
                        finalConvertView.startAnimation(translateAnimation);
                        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mLockedAppsInfoList.remove(appsManagersListItems);
                                mUnlockedAppsInfoList.add(appsManagersListItems);
                                AppLockDao.deleteItemFromDB(getApplicationContext(), appsManagersListItems.getPackageName());
                                myLockedAppLockListAdapter.notifyDataSetChanged();
                                myUnlockedAppLockListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    } else {
                        TranslateAnimation translateAnimation = new MyTranslateAnimation(1.0f, 0.0f, 500).addTranslateAnimation();
                        finalConvertView.startAnimation(translateAnimation);
                        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mUnlockedAppsInfoList.remove(appsManagersListItems);
                                mLockedAppsInfoList.add(appsManagersListItems);
                                AppLockDao.insertItemIntoDB(getApplicationContext(), appsManagersListItems.getPackageName());
                                myUnlockedAppLockListAdapter.notifyDataSetChanged();
                                myLockedAppLockListAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iconImageView, lockImageView;
        TextView textView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        initUI();
        initData();
    }

    private void switchListView() {
        if (isUnlockedListDisplay) {
            mUnlockButton.setBackgroundResource(R.drawable.app_lock_button_pressed);
            mLockButton.setBackgroundResource(R.drawable.app_lock_button_unpress);
            mUnlockLinearLayout.setVisibility(View.VISIBLE);
            mLockLinearLayout.setVisibility(View.GONE);
        } else {
            mUnlockButton.setBackgroundResource(R.drawable.app_lock_button_unpress);
            mLockButton.setBackgroundResource(R.drawable.app_lock_button_pressed);
            mUnlockLinearLayout.setVisibility(View.GONE);
            mLockLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppsInfoList = AcquireAppsInfo.getAppsInfoList(ApplicationLockActivity.this);
                mLockedAppsInfoList = new ArrayList<>();
                mUnlockedAppsInfoList = new ArrayList<>();
                List<String> packageNameList = AppLockDao.queryAll(ApplicationLockActivity.this);
                for (AppsManagersListItems appsManagersListItems : mAppsInfoList) {
                    if (packageNameList.contains(appsManagersListItems.getPackageName())) {
                        mLockedAppsInfoList.add(appsManagersListItems);
                        AppLockDao.insertItemIntoDB(ApplicationLockActivity.this, appsManagersListItems.getPackageName());
                    } else {
                        mUnlockedAppsInfoList.add(appsManagersListItems);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initUI() {
        isUnlockedListDisplay = true;
        mUnlockLinearLayout = findViewById(R.id.ll_app_lock_unlocked);
        mUnlockButton = findViewById(R.id.btn_app_lock_unlock);
        mUnlockAppsCount = findViewById(R.id.tv_app_unlock_number);
        mUnlockAppsListView = findViewById(R.id.lv_app_unlock_content);

        mLockLinearLayout = findViewById(R.id.ll_app_lock_locked);
        mLockButton = findViewById(R.id.btn_app_lock_lock);
        mLockAppsCount = findViewById(R.id.tv_app_lock_number);
        mLockAppsListView = findViewById(R.id.lv_app_lock_content);

        mLockButton.setOnClickListener(this);
        mUnlockButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_app_lock_unlock: {
                isUnlockedListDisplay = true;
                switchListView();
                break;
            }
            case R.id.btn_app_lock_lock: {
                isUnlockedListDisplay = false;
                switchListView();
                break;
            }
        }
    }
}
