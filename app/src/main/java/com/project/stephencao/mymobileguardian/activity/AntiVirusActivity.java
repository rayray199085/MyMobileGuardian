package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.ScanFilesInformation;
import com.project.stephencao.mymobileguardian.engine.AntiVirusDao;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.Md5Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntiVirusActivity extends AppCompatActivity {
    private ImageView mRadarImageView;
    private ProgressBar mProgressBar;
    private List<ScanFilesInformation> mVirusFilesList;
    private TextView mProgressBarTitleTextView;
    private LinearLayout mLinearLayout;
    private int counter = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstantValues.SCANNING_VIRUS:{
                    ScanFilesInformation scanFilesInformation = (ScanFilesInformation) msg.obj;
                    mProgressBarTitleTextView.setText(scanFilesInformation.getName());
                    TextView textView = new TextView(AntiVirusActivity.this);
                    if(scanFilesInformation.isVirus()){
                        textView.setTextColor(Color.RED);
                        textView.setText("Find dangerous virus: "+scanFilesInformation.getName());
                    }
                    else{
                        textView.setTextColor(Color.BLACK);
                        textView.setText("Safe App : "+scanFilesInformation.getName());
                    }
                    mLinearLayout.addView(textView,0);
                    break;
                }
                case ConstantValues.SCAN_FINISH:{
                    String finishNotification = (String) msg.obj;
                    mProgressBarTitleTextView.setText(finishNotification);
                    mRadarImageView.clearAnimation();
                    uninstallApplications();
                    break;
                }
            }
        }
    };

    private void uninstallApplications() {
        for(ScanFilesInformation scanFilesInformation: mVirusFilesList){
            String packageName = scanFilesInformation.getPackageName();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        initUI();
        initAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanVirus();
            }
        }).start();
    }

    private void scanVirus() {
        PackageManager packageManager = getPackageManager();
        List<String> virusList = AntiVirusDao.queryAll();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES
                + PackageManager.GET_UNINSTALLED_PACKAGES);

        mVirusFilesList = new ArrayList<>();
        List<ScanFilesInformation> allFilesList = new ArrayList<>();

        mProgressBar.setMax(installedPackages.size());

        for (PackageInfo packageInfo : installedPackages) {
            ScanFilesInformation scanFilesInformation = new ScanFilesInformation();
            Signature[] signatures = packageInfo.signatures;
            Signature signature = signatures[0];
            String signatureString = signature.toCharsString();
            String encryption = Md5Util.encryption(signatureString, false);

            scanFilesInformation.setPackageName(packageInfo.packageName);
            scanFilesInformation.setName(packageInfo.applicationInfo.loadLabel(packageManager).toString());

            if (virusList.contains(encryption)) {
                scanFilesInformation.setVirus(true);
                mVirusFilesList.add(scanFilesInformation);
            } else {
                scanFilesInformation.setVirus(false);
            }
            allFilesList.add(scanFilesInformation);
            try {
                Thread.sleep(50 + new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mProgressBar.setProgress(++counter);
            Message message = Message.obtain();
            message.what = ConstantValues.SCANNING_VIRUS;
            message.obj = scanFilesInformation;
            handler.sendMessage(message);
        }
        Message message = Message.obtain();
        message.what = ConstantValues.SCAN_FINISH;
        message.obj = "Virus scan is done!";
        handler.sendMessage(message);
    }

    private void initAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);
        mRadarImageView.startAnimation(rotateAnimation);
    }

    private void initUI() {
        mRadarImageView = findViewById(R.id.iv_anti_virus_scan_radar_sector);
        mProgressBar = findViewById(R.id.pb_anti_virus_progress_bar);
        mProgressBarTitleTextView = findViewById(R.id.tv_anti_virus_progress_bar_title);
        mLinearLayout = findViewById(R.id.ll_anti_virus_files);

    }
}
