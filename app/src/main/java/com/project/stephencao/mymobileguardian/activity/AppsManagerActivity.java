package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.adapter.MyAppsManagerAdapter;
import com.project.stephencao.mymobileguardian.animation.MyAlphaAnimation;
import com.project.stephencao.mymobileguardian.animation.MyScaleAnimation;
import com.project.stephencao.mymobileguardian.bean.AppsManagersListItems;
import com.project.stephencao.mymobileguardian.engine.AcquireAppsInfo;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AppsManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mDiskSpaceTextView, mSDCardSpaceTextView, mAppTypeDisplayTextView;
    private ListView mAppsListView;
    private PopupWindow mPopupWindow;
    private List<AppsManagersListItems> mAppsInfoList;
    private List<AppsManagersListItems> mUserAppsInfoList;
    private List<AppsManagersListItems> mSystemAppsInfoList;
    private MyAppsManagerAdapter myAppsManagerAdapter;
    private AppsManagersListItems mAppsManagersListItems;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mAppsInfoList.clear();
                mAppsInfoList.addAll(mUserAppsInfoList);
                mAppsInfoList.addAll(mSystemAppsInfoList);
                myAppsManagerAdapter = new MyAppsManagerAdapter(mAppsInfoList, AppsManagerActivity.this, mUserAppsInfoList.size());
                mAppsListView.setAdapter(myAppsManagerAdapter);
                mAppTypeDisplayTextView.setText("User Application (" + mUserAppsInfoList.size() + ")");
            }
            mAppsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mSystemAppsInfoList != null) {
                        if (firstVisibleItem > mUserAppsInfoList.size()) {
                            mAppTypeDisplayTextView.setText("System Application (" + mSystemAppsInfoList.size() + ")");
                        } else {
                            mAppTypeDisplayTextView.setText("User Application (" + mUserAppsInfoList.size() + ")");
                        }
                    }
                }
            });

            mAppsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0 || position == mUserAppsInfoList.size() + 1) {
                        return;
                    } else {
                        if (position < mUserAppsInfoList.size() + 1) {
                            mAppsManagersListItems = mAppsInfoList.get(position - 1);
                        } else {
                            mAppsManagersListItems = mAppsInfoList.get(position - 2);
                        }
                        showPopUpWindow(view);
                    }
                }
            });
        }
    };

    private void showPopUpWindow(View view) {
        View popUpView = LayoutInflater.from(AppsManagerActivity.this)
                .inflate(R.layout.view_app_manager_pop_up_window, null);
        LinearLayout uninstallLinearLayout = popUpView.findViewById(R.id.ll_apps_manager_uninstall);
        LinearLayout bootUpLinearLayout = popUpView.findViewById(R.id.ll_apps_manager_boot_up);
        LinearLayout shareLinearLayout = popUpView.findViewById(R.id.ll_apps_manager_share);
        uninstallLinearLayout.setOnClickListener(this);
        bootUpLinearLayout.setOnClickListener(this);
        shareLinearLayout.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new MyAlphaAnimation(1000, 0.0f, 1.0f).addAlphaAnimation();
        ScaleAnimation scaleAnimation = new MyScaleAnimation(1000, 0.0f, 0.0f).addScaleAnimation();

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        popUpView.startAnimation(animationSet);
        mPopupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.showAsDropDown(view, 150, -view.getHeight());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_manager);
        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initTitle();
        initListView();
    }

    private void initListView() {
        mUserAppsInfoList = new ArrayList<>();
        mSystemAppsInfoList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppsInfoList = AcquireAppsInfo.getAppsInfoList(AppsManagerActivity.this);
                for (AppsManagersListItems appsManagersListItems : mAppsInfoList) {
                    if (appsManagersListItems.isSystemApp()) {
                        mSystemAppsInfoList.add(appsManagersListItems);
                    } else {
                        mUserAppsInfoList.add(appsManagersListItems);
                    }
                }
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        }).start();

    }

    private void initTitle() {
        // disk space
        String diskPath = Environment.getDataDirectory().getAbsolutePath();
        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // sd card space
        long diskAvailableSpace = getAvailableSpace(diskPath);
        long sdCardAvailableSpace = getAvailableSpace(sdCardPath);
        String diskAvailableSize = Formatter.formatFileSize(this, diskAvailableSpace);
        String sdCardAvailableSize = Formatter.formatFileSize(this, sdCardAvailableSpace);
        mDiskSpaceTextView.setText("Internal Available Space: " + diskAvailableSize);
        mSDCardSpaceTextView.setText("SD Card Available Space: " + sdCardAvailableSize);
    }

    private long getAvailableSpace(String path) {
        StatFs statFs = new StatFs(path);
        long availableBlocks = (long) statFs.getAvailableBlocks();
        long blockSize = (long) statFs.getBlockSize();
        return availableBlocks * blockSize;
    }

    private void initUI() {
        mDiskSpaceTextView = findViewById(R.id.tv_apps_manager_disk_space);
        mSDCardSpaceTextView = findViewById(R.id.tv_apps_manager_sd_card_space);
        mAppTypeDisplayTextView = findViewById(R.id.tv_apps_manager_display_app_type);
        mAppsListView = findViewById(R.id.lv_apps_manager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_apps_manager_uninstall: {
                if (mAppsManagersListItems.isSystemApp()) {
                    ToastUtil.show(this, "System Application can't be uninstalled.");
                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + mAppsManagersListItems.getPackageName()));
                    startActivity(intent);
                }
                break;
            }
            case R.id.ll_apps_manager_boot_up: {
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(mAppsManagersListItems.getPackageName());
                if (launchIntentForPackage != null) {
                    startActivity(launchIntentForPackage);
                } else {
                    ToastUtil.show(this, "This Application can't be launched.");
                }
                break;
            }
            case R.id.ll_apps_manager_share: {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This app: " + mAppsManagersListItems.getAppName() +
                        " is really great. So I recommend it to you.");
                startActivity(intent);
                break;
            }
        }
        if (mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
