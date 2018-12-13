package com.project.stephencao.mymobileguardian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;
import com.project.stephencao.mymobileguardian.utils.TouchAndSlide;
import com.project.stephencao.mymobileguardian.view.MySettingsItemsView;

public class SecuritySettingsSecondActivity extends AppCompatActivity implements View.OnClickListener {
    private MySettingsItemsView mySettingsItemsView;
    private Button prevButton, nextButton;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_second_settings);
        initUI();
        TouchAndSlide.jump(linearLayout,nextButton,prevButton);
    }

    private void initUI() {
        mySettingsItemsView = findViewById(R.id.sv_security_settings_page2);
        mySettingsItemsView.setTitleContent("Click to bind SIM card");
        mySettingsItemsView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_BIND_UNBIND);
        mySettingsItemsView.setDescriptionContent("SIM card is binding.");
        mySettingsItemsView.setOnClickListener(this);
        prevButton = findViewById(R.id.btn_security_setting_page2_prev);
        nextButton = findViewById(R.id.btn_security_setting_page2_next);
        linearLayout = findViewById(R.id.ll_security_settings_view2);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        boolean isBind = SharePreferencesUtil.getBoolean(this, ConstantValues.BIND_SIM);
        mySettingsItemsView.setCheckBoxStatus(isBind);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sv_security_settings_page2: {
                boolean isBindSIM = mySettingsItemsView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.BIND_SIM, isBindSIM);
                if(isBindSIM){
                    try {
                        // tablet and virtual devices do not have a sim card
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        @SuppressLint("MissingPermission") String simSerialNumber = telephonyManager.getSimSerialNumber();
                        if(!"".equals(simSerialNumber)){
                            SharePreferencesUtil.recordString(this,ConstantValues.SECURITY_SIM_CARD_SERIAL_NUMBER,simSerialNumber);
                        }
                    } catch (Exception e) {
                        ToastUtil.show(this,"SIM card is not exist.");
                    }
                }
                else{
                    String serialNumber = SharePreferencesUtil.getString(this, ConstantValues.SECURITY_SIM_CARD_SERIAL_NUMBER);
                    if (serialNumber!=null){
                        SharePreferencesUtil.remove(this,ConstantValues.SECURITY_SIM_CARD_SERIAL_NUMBER);
                    }
                }
                break;
            }
            case R.id.btn_security_setting_page2_prev: {
                Intent intent = new Intent(this, SecuritySettingsFirstActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.prev_to_right,R.anim.current_to_right);
                break;
            }
            case R.id.btn_security_setting_page2_next: {
                if (SharePreferencesUtil.getBoolean(this,ConstantValues.BIND_SIM)) {
                    Intent intent = new Intent(this, SecuritySettingsThirdActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_to_left,R.anim.current_to_left);
                }
                else{
                    ToastUtil.show(this,"Please bind your SIM card first.");
                }
                break;
            }
        }
    }
}
