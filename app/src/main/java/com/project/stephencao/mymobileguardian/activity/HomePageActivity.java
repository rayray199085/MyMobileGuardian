package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.IconItems;
import com.project.stephencao.mymobileguardian.adapter.MyGridViewAdapter;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.Md5Util;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private String[] mFunctionNames;
    private int[] mFunctionIcons;
    private List<IconItems> listIcons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initUI();
        initData();
    }

    /**
     * Initialize the data for the grid view
     */
    private void initData() {
        mFunctionNames = new String[]{"Security", "Block List", "Apps Manager",
                "Mission", "Data Usage", "Antivirus", "Cached Data", "Tools", "Settings"};
        mFunctionIcons = new int[]{R.drawable.security, R.drawable.block,
                R.drawable.apps, R.drawable.mission_control, R.drawable.data,
                R.drawable.antivirus, R.drawable.cache,
                R.drawable.tools, R.drawable.settings};
        listIcons = new ArrayList<>();
        for (int i = 0; i < mFunctionIcons.length; i++) {
            IconItems iconItems = new IconItems();
            iconItems.setIconNames(mFunctionNames[i]);
            iconItems.setIconID(mFunctionIcons[i]);
            listIcons.add(iconItems);
        }
        MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter(listIcons, HomePageActivity.this);
        mGridView.setAdapter(myGridViewAdapter);
        mGridView.setOnItemClickListener(this);
    }


    private void initUI() {
        mGridView = findViewById(R.id.gv_functions);
    }

    /**
     * @param parent
     * @param view
     * @param position 0-8 9 button position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: { // security
                String password = SharePreferencesUtil.getString(this, ConstantValues.SECURITY_PASSWORD);
                if (password == null) {
                    SharePreferencesUtil.initSPfile(this);
                    initPassword();
                } else {
                    confirmPassword();
                }
                break;
            }
            case 1: { // block list
                startActivity(new Intent(this, BlacklistManagerActivity.class));
                break;
            }
            case 2: { // apps manager
                startActivity(new Intent(this,AppsManagerActivity.class));
                break;
            }
            case 3: { // mission control
                startActivity(new Intent(this,MissionsManagerActivity.class));
                break;
            }
            case 4: { // data usage
                startActivity(new Intent(this,DataUsageActivity.class));
                break;
            }
            case 5: { // antivirus
                startActivity(new Intent(this, AntiVirusActivity.class));
                break;
            }
            case 6: { // cached data
//                startActivity(new Intent(this,ClearCachedDataActivity.class));
                startActivity(new Intent(this,CachedDataActivity.class));
                break;
            }
            case 7: { // advanced tools
                Intent intent = new Intent(HomePageActivity.this, ToolsActivity.class);
                startActivity(intent);
                break;
            }
            case 8: { // settings
                Intent intent = new Intent(HomePageActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void confirmPassword() {
        AlertDialog.Builder confirmPasswordDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_password, null);
        final EditText typeInPassword = view.findViewById(R.id.et_dg_confirm_password);
        typeInPassword.setHint("Enter Your Password");
        confirmPasswordDialogBuilder.setView(view);
        final AlertDialog alertDialog = confirmPasswordDialogBuilder.create();
        Button confirmButton = view.findViewById(R.id.btn_dg_confirm_password_confirm);
        Button cancelButton = view.findViewById(R.id.btn_dg_confirm_password_cancel);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = typeInPassword.getText().toString().trim();
                if (Md5Util.encryption(password,true).equals(SharePreferencesUtil.
                        getString(HomePageActivity.this, ConstantValues.SECURITY_PASSWORD))) {
                    alertDialog.dismiss();
                    boolean finished = SharePreferencesUtil.getBoolean(HomePageActivity.this,
                            ConstantValues.SECURITY_SETTINGS_FINISHED);
                    if(finished) {
                        Intent intent = new Intent(HomePageActivity.this, SecurityActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(HomePageActivity.this, SecuritySettingsFirstActivity.class);
                        startActivity(intent);
                    }

                } else {
                    typeInPassword.setText("");
                    ToastUtil.show(HomePageActivity.this, "Incorrect Password");
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

       alertDialog.show();
    }

    private void initPassword() {
        final AlertDialog.Builder initPasswordDialogBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_init_password, null);
        initPasswordDialogBuilder.setView(view);
        final EditText enterPassword = view.findViewById(R.id.et_dg_init_password);
        final EditText confirmPassword = view.findViewById(R.id.et_dg_confrim_init_password);
        Button confirmButton = view.findViewById(R.id.btn_dg_init_password_confirm);
        Button cancelButton = view.findViewById(R.id.btn_dg_init_password_cancel);
        final AlertDialog alertDialog = initPasswordDialogBuilder.create();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    String password = enterPassword.getText().toString().trim();
                    SharePreferencesUtil.recordString(HomePageActivity.this,
                            ConstantValues.SECURITY_PASSWORD, Md5Util.encryption(password,true));
                    alertDialog.dismiss();
                    Intent intent = new Intent(HomePageActivity.this, SecuritySettingsFirstActivity.class);
                    startActivity(intent);
                } else {
                    enterPassword.setText("");
                    confirmPassword.setText("");
                    ToastUtil.show(HomePageActivity.this, "You have to type in the same password. Try again.");
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
