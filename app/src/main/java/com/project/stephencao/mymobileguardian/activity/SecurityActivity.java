package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

public class SecurityActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView  phoneTextView,resetSettingsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initUI();
        initData();
    }

    private void initData() {
        String phoneNumber = SharePreferencesUtil.getString(this, ConstantValues.SECURITY_CONTACT_NUMBER);
        phoneTextView.setText(phoneNumber);
    }

    private void initUI() {
        phoneTextView = findViewById(R.id.tv_security_function_contact_number);
        resetSettingsTextView = findViewById(R.id.tv_security_function_reset_settings);
        resetSettingsTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_security_function_reset_settings: {
                Intent intent = new Intent(this,SecuritySettingsFirstActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}
