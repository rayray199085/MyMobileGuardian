package com.project.stephencao.mymobileguardian.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.engine.SMSBackup;
import com.project.stephencao.mymobileguardian.service.RocketManService;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.ServiceUtil;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.view.MySettingStylesView;
import com.project.stephencao.mymobileguardian.view.MySettingsItemsView;

import java.io.File;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mLocationInquiryTextView, mSmsBackupTextView, mHotLinesTextView, mAppLock;
    private MySettingsItemsView myRocketManItemsView;
    private MySettingStylesView myRocketManStylesView;
    private String[] mRocketManStyle = new String[]{"Yui Hantano", "Kazuha"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        initUI();
    }

    @Override
    protected void onResume() {
        boolean isRocketManOpen = SharePreferencesUtil.getBoolean(this, ConstantValues.ROCKET_MAN);
        boolean isRocketRunning = ServiceUtil.isRunning(this, "com.project.stephencao.mymobileguardian.service.RocketManService");
        myRocketManItemsView.setCheckBoxStatus(isRocketManOpen && isRocketRunning);
        myRocketManStylesView.setStyleDescriptionTextView(SharePreferencesUtil.getString(this, ConstantValues.ROCKET_MAN_STYLE));
        super.onResume();
    }

    private void initUI() {
        mLocationInquiryTextView = findViewById(R.id.tv_tools_location_inquiry);
        mLocationInquiryTextView.setOnClickListener(this);
        myRocketManItemsView = findViewById(R.id.sv_tools_rocket_man_items);
        myRocketManItemsView.setTitleContent("Rocket Man");
        myRocketManItemsView.setDescriptionContent("Rocket Man is closed");
        myRocketManItemsView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        myRocketManItemsView.setOnClickListener(this);

        myRocketManStylesView = findViewById(R.id.sv_tools_rocket_man_styles);
        myRocketManStylesView.setStyleTitleContent("Rock Man Style");
        myRocketManStylesView.setStyleIcon(R.drawable.rocket_icon);
        myRocketManStylesView.setOnClickListener(this);

        mSmsBackupTextView = findViewById(R.id.tv_tools_location_sms_backup);
        mSmsBackupTextView.setOnClickListener(this);

        mHotLinesTextView = findViewById(R.id.tv_tools_location_hot_lines);
        mHotLinesTextView.setOnClickListener(this);

        mAppLock = findViewById(R.id.tv_tools_location_app_lock);
        mAppLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tools_location_inquiry: {
                Intent intent = new Intent(this, ToolsLocationInquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_to_left, R.anim.current_to_left);
                break;
            }
            case R.id.sv_tools_rocket_man_items: {
                boolean flag = myRocketManItemsView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.ROCKET_MAN, flag);
                if (flag) {
                    Intent intent = new Intent(this, RocketManService.class);
                    startService(intent);
                    finish();
                } else {
                    stopService(new Intent(this, RocketManService.class));
                }
                break;
            }
            case R.id.sv_tools_rocket_man_styles: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setSingleChoiceItems(mRocketManStyle, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRocketManStylesView.setStyleDescriptionTextView(mRocketManStyle[which]);
                        SharePreferencesUtil.recordString(ToolsActivity.this, ConstantValues.ROCKET_MAN_STYLE, mRocketManStyle[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            }
            case R.id.tv_tools_location_sms_backup: {
                try {
                    showProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.tv_tools_location_hot_lines:{
                startActivity(new Intent(this, CommonHotLinesEnquiryActivity.class));
                break;
            }
            case R.id.tv_tools_location_app_lock:{
                startActivity(new Intent(this,ApplicationLockActivity.class));
            }
        }
    }

    private void showProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(ToolsActivity.this);
        progressDialog.setTitle("SMS is copying");
        progressDialog.setIcon(R.drawable.sms_icon);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        new Thread(new Runnable() {
            File smsBackup = new File(Environment.getExternalStorageDirectory(), "sms_backup.xml");

            @Override
            public void run() {
                SMSBackup.backup(ToolsActivity.this, smsBackup, new CallBack() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }
                    @Override
                    public void setProgress(int current) {
                        progressDialog.setProgress(current);
                    }

                });
                progressDialog.dismiss();
            }
        }).start();
    }

    /**
     * According to the programmer's decision, using progressDialog or progressBar to display the progress of sms backup
     */
    public interface CallBack {
        public void setMax(int max);

        public void setProgress(int current);

    }
}
