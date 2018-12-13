package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

public class BlockedPageActivity extends AppCompatActivity implements View.OnClickListener {
    private String mPackageName;
    private TextView mAppNameTextView;
    private ImageView mAppIconImageView;
    private Button mSubmitButton;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_page);
        initUI();
        initData();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        super.onBackPressed();
    }

    private void initData() {
        mPackageName = getIntent().getStringExtra("packageName");
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mPackageName, 0);
            String appName = applicationInfo.loadLabel(packageManager).toString();
            Drawable icon = applicationInfo.loadIcon(packageManager);

            mAppIconImageView.setBackgroundDrawable(icon);
            mAppNameTextView.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        mAppNameTextView = findViewById(R.id.tv_blocked_page_app_name);
        mAppIconImageView = findViewById(R.id.iv_blocked_page_app_icon);
        mSubmitButton = findViewById(R.id.btn_blocked_page_submit);
        mSubmitButton.setOnClickListener(this);
        mPasswordEditText = findViewById(R.id.et_blocked_page_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_blocked_page_submit: {
                String password = mPasswordEditText.getText().toString().trim();
                if (password.equals("123")) {
                    Intent intent = new Intent();
                    intent.putExtra("packageName",mPackageName);
                    intent.setAction("android.intent.action.PASS_WATCHING_THIS_APP");
                    sendBroadcast(intent);
                    finish();
                } else {
                    ToastUtil.show(this, "Try again. Password is incorrect.");
                }
                break;
            }
        }
    }
}
