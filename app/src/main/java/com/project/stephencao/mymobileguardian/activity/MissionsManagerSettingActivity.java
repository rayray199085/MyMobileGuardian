package com.project.stephencao.mymobileguardian.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.service.LockScreenClearMissionsService;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.ServiceUtil;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

public class MissionsManagerSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mHideSystemMissionsLinearLayout, mClearSystemMissionsLinearLayout;
    private TextView mHideSystemMissionsTextView,mClearSystemMissionsTextView;
    private CheckBox mHideSystemMissionsCheckBox,mClearSystemMissionsCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions_manager_settings);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isHide = SharePreferencesUtil.getBoolean(this, ConstantValues.HIDE_SYSTEM_MISSIONS);
        if (isHide) {
            mHideSystemMissionsTextView.setText("Hide Systems Missions");
            mHideSystemMissionsCheckBox.setChecked(false);
        } else {
            mHideSystemMissionsTextView.setText("Display Systems Missions");
            mHideSystemMissionsCheckBox.setChecked(true);
            SharePreferencesUtil.recordBoolean(this, ConstantValues.HIDE_SYSTEM_MISSIONS, false);
        }
        boolean isClear = SharePreferencesUtil.getBoolean(this, ConstantValues.CLEAR_MISSIONS);
        boolean isClearServiceRunning = ServiceUtil.isRunning(this,
                "com.project.stephencao.mymobileguardian.service.LockScreenClearMissionsService");
        if(isClear && isClearServiceRunning){
            mClearSystemMissionsTextView.setText("Clear missions when locking screen is on");
            mClearSystemMissionsCheckBox.setChecked(true);
        }
        else {
            mClearSystemMissionsTextView.setText("Clear missions when locking screen is off");
            mClearSystemMissionsCheckBox.setChecked(false);
        }
    }

    private void initUI() {
        mHideSystemMissionsLinearLayout = findViewById(R.id.ll_missions_manager_mission_settings_hide);
        mHideSystemMissionsTextView = findViewById(R.id.tv_missions_manager_mission_settings_hide);
        mHideSystemMissionsCheckBox = findViewById(R.id.cb_missions_manager_mission_settings_hide);
        mHideSystemMissionsLinearLayout.setOnClickListener(this);

        mClearSystemMissionsLinearLayout = findViewById(R.id.ll_missions_manager_mission_settings_clear);
        mClearSystemMissionsTextView = findViewById(R.id.tv_missions_manager_mission_settings_clear);
        mClearSystemMissionsCheckBox = findViewById(R.id.cb_missions_manager_mission_settings_clear);
        mClearSystemMissionsLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_missions_manager_mission_settings_hide: {
                if (mHideSystemMissionsCheckBox.isChecked()) {
                    mHideSystemMissionsTextView.setText("Hide Systems Missions");
                    mHideSystemMissionsCheckBox.setChecked(false);
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.HIDE_SYSTEM_MISSIONS, true);

                } else {
                    mHideSystemMissionsTextView.setText("Display Systems Missions");
                    mHideSystemMissionsCheckBox.setChecked(true);
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.HIDE_SYSTEM_MISSIONS, false);
                }
                break;
            }
            case R.id.ll_missions_manager_mission_settings_clear:{

                if(mClearSystemMissionsCheckBox.isChecked()){
                    mClearSystemMissionsTextView.setText("Clear missions when locking screen is off");
                    mClearSystemMissionsCheckBox.setChecked(false);
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.CLEAR_MISSIONS, false);
                    stopService(new Intent(this, LockScreenClearMissionsService.class));
                }
                else {
                    mClearSystemMissionsTextView.setText("Clear missions when locking screen is on");
                    mClearSystemMissionsCheckBox.setChecked(true);
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.CLEAR_MISSIONS, true);
                    startService(new Intent(this, LockScreenClearMissionsService.class));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = null;
        if (mHideSystemMissionsCheckBox.isChecked()) {
            intent = new Intent();
            intent.putExtra("display", true);
        } else {
            intent = new Intent();
            intent.putExtra("display", false);
        }
        setResult(1, intent);
        finish();
    }
}
