package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.TouchAndSlide;

public class SecuritySettingsFirstActivity extends AppCompatActivity {
    private Button nextButton;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_first_settings);
        initUI();
        TouchAndSlide.jump(linearLayout,nextButton, null);
    }


    private void initUI() {
        linearLayout = findViewById(R.id.ll_security_settings_view1);
        nextButton = findViewById(R.id.btn_security_setting_page1_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecuritySettingsFirstActivity.this, SecuritySettingsSecondActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.next_to_left, R.anim.current_to_left);
            }
        });
    }
}
