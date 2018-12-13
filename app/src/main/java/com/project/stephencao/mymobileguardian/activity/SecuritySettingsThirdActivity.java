package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;
import com.project.stephencao.mymobileguardian.utils.TouchAndSlide;

public class SecuritySettingsThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private Button prevButton, nextButton, chooseContactButton;
    private EditText editText;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_third_settings);
        initUI();
        TouchAndSlide.jump(linearLayout,nextButton,prevButton);
    }

    private void initUI() {
        linearLayout = findViewById(R.id.ll_security_settings_view3);
        prevButton = findViewById(R.id.btn_security_setting_page3_prev);
        nextButton = findViewById(R.id.btn_security_setting_page3_next);
        chooseContactButton = findViewById(R.id.btn_security_setting_page3_choose_number);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        chooseContactButton.setOnClickListener(this);
        editText = findViewById(R.id.et_setting_target_phone_number);
        String phoneNumber = SharePreferencesUtil.getString(this, ConstantValues.SECURITY_CONTACT_NUMBER);
        if (phoneNumber != null) {
            editText.setText(phoneNumber);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_security_setting_page3_prev: {
                Intent intent = new Intent(this, SecuritySettingsSecondActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.prev_to_right,R.anim.current_to_right);
                break;
            }
            case R.id.btn_security_setting_page3_next: {
                String phoneNumber = editText.getText().toString().trim();
                if (!"".equals(phoneNumber)) {
                    SharePreferencesUtil.recordString(this, ConstantValues.SECURITY_CONTACT_NUMBER, phoneNumber);
                    Intent intent = new Intent(this, SecuritySettingsFourActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.next_to_left,R.anim.current_to_left);
                } else {
                    ToastUtil.show(this, "Contact number cannot be empty.");
                }
                break;
            }
            case R.id.btn_security_setting_page3_choose_number: {
                Intent intent = new Intent(this, ContactInformationActivity.class);
                startActivityForResult(intent, 0);
                break;
            }
        }

    }

    /**
     * Return the phone number from contacts
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==2&&data!=null) {
            String phoneNumber = data.getStringExtra("phoneNumber");
            phoneNumber = phoneNumber.replaceAll("\\D","");
            editText.setText(phoneNumber);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
