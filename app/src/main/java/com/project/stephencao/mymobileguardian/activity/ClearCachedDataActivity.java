package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.CachedAppInfoListItems;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;


public class ClearCachedDataActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBroomIconImageView;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;
    private PackageManager mPackageManager;
    private TextView mScanStatusTextView;
    private int counter = 0;
    private boolean clearAllAvailable = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.CLEAR_CACHED_DATA: {
                    View view = LayoutInflater.from(ClearCachedDataActivity.this).inflate(
                            R.layout.view_clear_cached_app_info_items, null);
                    ImageView icon = view.findViewById(R.id.iv_app_cached_data_info_icon);
                    TextView appName = view.findViewById(R.id.tv_app_cached_data_info_app_name);
                    TextView cacheSize = view.findViewById(R.id.tv_app_cached_data_info_cache_size);
                    LinearLayout clearSingleLinearLayout = view.findViewById(R.id.ll_app_cached_info_clear_single_item);
                    final CachedAppInfoListItems cachedAppInfoListItems = (CachedAppInfoListItems) msg.obj;
                    icon.setBackgroundDrawable(cachedAppInfoListItems.getIcon());
                    appName.setText(cachedAppInfoListItems.getAppName());
                    cacheSize.setText("Cached Data Size: " + cachedAppInfoListItems.getCachedDataSize());
                    mScanStatusTextView.setText(cachedAppInfoListItems.getAppName());
                    mLinearLayout.addView(view, 0);
                    clearSingleLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (clearAllAvailable) {
                                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.parse("package:"+cachedAppInfoListItems.getPackageName()));
                                startActivity(intent);
                            }
                        }
                    });
                    break;
                }
                case ConstantValues.SCAN_CACHED_DATA_FINISH: {
                    mScanStatusTextView.setText("Cached data scan is done!");
                    clearAllAvailable = true;
                    break;
                }
                case ConstantValues.CLEAR_ALL_CACHED_DATA: {
                    mLinearLayout.removeAllViews();
                    break;
                }
                case ConstantValues.DELETE_SINGLE_APP_CACHED_DATA:{
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cached_data);
        initUI();
        mPackageManager = getPackageManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
    }

    private void getCacheDataSize(final PackageManager packageManager, final PackageInfo packageInfo) {

        IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                long cacheSize = pStats.cacheSize;
                if (cacheSize > 0) {
                    String cachedDataSize = Formatter.formatFileSize(ClearCachedDataActivity.this, cacheSize);
                    CachedAppInfoListItems cachedAppInfoListItems = new CachedAppInfoListItems();

                    Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                    String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();

                    cachedAppInfoListItems.setAppName(appName);
                    cachedAppInfoListItems.setPackageName(packageInfo.packageName);
                    cachedAppInfoListItems.setIcon(icon);
                    cachedAppInfoListItems.setCachedDataSize(cachedDataSize);

                    Message message = Message.obtain();
                    message.what = ConstantValues.CLEAR_CACHED_DATA;
                    message.obj = cachedAppInfoListItems;
                    handler.sendMessage(message);
                }
            }
        };
        try {
            String packageName = packageInfo.packageName;
            Class<?> aClass = Class.forName("android.content.pm.PackageManager");
            Method method = aClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, mStatsObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        List<PackageInfo> installedPackages = mPackageManager.getInstalledPackages(0);
        mProgressBar.setMax(installedPackages.size());
        for (PackageInfo packageInfo : installedPackages) {
            getCacheDataSize(mPackageManager, packageInfo);
            try {
                Thread.sleep(50 + new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mProgressBar.setProgress(++counter);
        }
        handler.sendEmptyMessage(ConstantValues.SCAN_CACHED_DATA_FINISH);
    }

    private void initUI() {
        mBroomIconImageView = findViewById(R.id.iv_clear_cached_data_broom_icon);
        mProgressBar = findViewById(R.id.pb_clear_cached_data_progress_bar);
        mLinearLayout = findViewById(R.id.ll_clear_cached_data_content);
        mScanStatusTextView = findViewById(R.id.tv_clear_cached_data_scan_status);

        mBroomIconImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_cached_data_broom_icon: {
                if (clearAllAvailable) {
                    clearCachedData();
                }
                break;
            }
        }
    }

    private void clearCachedData() {
        try {
            Class<?> aClass = Class.forName("android.content.pm.PackageManager");
            Method method = aClass.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            method.invoke(mPackageManager, Long.MAX_VALUE, new IPackageDataObserver.Stub() {

                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                    handler.sendEmptyMessage(ConstantValues.CLEAR_ALL_CACHED_DATA);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
