package com.project.stephencao.mymobileguardian.activity;

import android.os.*;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.engine.AddressDao;

public class ToolsLocationInquiryActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button queryButton;
    private TextView textView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String numberInfo = (String) msg.obj;
            textView.setText(numberInfo);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.prev_to_right,R.anim.current_to_right);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_location_inquiry);
        initUI();
    }

    private void initUI() {
        editText = findViewById(R.id.et_tools_location_inquiry);
        textView = findViewById(R.id.tv_tools_location_inquiry);
        queryButton = findViewById(R.id.btn_tools_location_inquiry);
        queryButton.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = editText.getText().toString().trim();
                if(!"".equals(input)){
                    queryDB(input);
                }
                else{
                    textView.setText("");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tools_location_inquiry: {
                final String input = editText.getText().toString().trim();
                if ("".equals(input)) {
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    editText.startAnimation(shake);
                   Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                   if(vibrator.hasVibrator()){ // vibrate when the edit text is empty and the button is pressed
                        vibrator.vibrate(1000);
                   }
                } else {
                    queryDB(input);
                }
                break;
            }
        }
    }

    private void queryDB(final String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressDao.getAddress(input);
                Message message = Message.obtain();
                message.obj = address;
                handler.sendMessage(message);
            }
        }).start();
    }


}
