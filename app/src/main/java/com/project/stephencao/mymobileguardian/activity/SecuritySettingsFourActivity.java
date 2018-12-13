package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;
import com.project.stephencao.mymobileguardian.utils.TouchAndSlide;

public class SecuritySettingsFourActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox checkBox;
    private TextView textView;
    private Button prevButton, nextButton;
    private LinearLayout protectionLinearLayout,linearLayout;
    private String notification = "You haven't opened the security protection on your phone.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_four_settings);
        initUI();
        TouchAndSlide.jump(linearLayout,nextButton,prevButton);

    }

    private void initUI() {
        linearLayout = findViewById(R.id.ll_security_settings_view4);
        checkBox = findViewById(R.id.cb_security_settings_page4);
        protectionLinearLayout = findViewById(R.id.ll_security_settings_page4);
        textView = findViewById(R.id.tv_security_settings_page4);
        textView.setText(notification);
        protectionLinearLayout.setOnClickListener(this);
        prevButton = findViewById(R.id.btn_security_setting_page4_prev);
        nextButton = findViewById(R.id.btn_security_setting_page4_next);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        boolean isChecked = SharePreferencesUtil.getBoolean(this, ConstantValues.SECURITY_SETTINGS_FINISHED);
        if(isChecked){
            textView.setText(notification.replaceAll("haven't", "have"));
        }
        checkBox.setChecked(isChecked);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_security_setting_page4_next: {
                if(checkBox.isChecked()){
                    Intent intent = new Intent(this, SecurityActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_to_left,R.anim.current_to_left);
                }
                else{
                    ToastUtil.show(this,"You should open the security protection first.");
                }
                break;
            }
            case R.id.btn_security_setting_page4_prev: {
                Intent intent = new Intent(this, SecuritySettingsThirdActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.prev_to_right,R.anim.current_to_right);
                break;
            }
            case R.id.ll_security_settings_page4: {
                if (checkBox.isChecked()) {
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.SECURITY_SETTINGS_FINISHED,false);
                    checkBox.setChecked(false);
                    textView.setText(notification);

                } else {
                    checkBox.setChecked(true);
                    SharePreferencesUtil.recordBoolean(this, ConstantValues.SECURITY_SETTINGS_FINISHED,true);
                    textView.setText(notification.replaceAll("haven't", "have"));
                }
            }
        }
    }
}
