package com.project.stephencao.mymobileguardian.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.animation.MyAlphaAnimation;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.StreamUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private TextView mTextView;
    private String mVersionDescription;
    private String mDownloadUrl;
    private int mLocalVersionCode;
    private RelativeLayout relativeLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.UPDATE_VERSION: {
                    showUpdateDialog();
                    break;
                }
                case ConstantValues.ENTER_HOME_PAGE: { // enter home page
                    enterHomePage();
                    break;
                }
                case ConstantValues.IO_ERROR: {
                    ToastUtil.show(SplashActivity.this, "Can't connect to the server.\nPlease check the Internet connection");
                    enterHomePage();
                    break;
                }
                case ConstantValues.JSON_ERROR: {
                    ToastUtil.show(SplashActivity.this, "JSON file content can't be parsed");
                    enterHomePage();
                    break;
                }
            }
        }
    };


    /**
     * AlertDialog to notify user to update the app when the server version is newer than the local version
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.drawable.alert);
        builder.setTitle("Upgrade Notification");
        builder.setMessage(mVersionDescription);
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHomePage();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // download the new version apk through downloadURL
                downloadAPK();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                enterHomePage();
            }
        });
        builder.show();
    }

    /**
     * Use xUtils to download the new version APK
     */
    private void downloadAPK() {
        //Check whether SD card is mounted or not
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), "MobileGuardian.apk");
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, file.getAbsolutePath(), new RequestCallBack<File>() {
                ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    File resultFile = responseInfo.result;
                    progressDialog.dismiss();
                    ToastUtil.show(SplashActivity.this, "Download Successfully!");
                    installAPK(resultFile);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    ToastUtil.show(SplashActivity.this, "The network is busy, try again later.");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    progressDialog.setTitle("Downloading, don't quit the app.");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(Integer.parseInt(String.valueOf(total)));
                    progressDialog.setProgress(Integer.parseInt(String.valueOf(current)));
                    progressDialog.show();
                }
            });
        }

    }

    /**
     * Install the upgrade version APK
     */
    private void installAPK(File resultFile) {
        Intent intent = new Intent("android.intent.action.VIEW");// signature and package name should be the same, otherwise it could not be updated
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(resultFile), "application/vnd.android.package-archive");
        startActivityForResult(intent, 0);
    }

    /**
     * If the user enter the installation page, but press cancel, jump to the home page.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        enterHomePage();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Jump to the homepage activity
     */

    private void enterHomePage() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish(); // after start a new activity, finish the guide page
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
        if(!SharePreferencesUtil.getBoolean(this,ConstantValues.HAS_SHORTCUT)){
            initShortCut();
        }
        //Animation for Initializing the entry page
        initAnimation();
        initDatabase();

    }

    private void initShortCut() {
        Intent intent =  new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,Intent.ShortcutIconResource.fromContext(this,R.drawable.shield));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"MG");
        Intent shortcutHome = new Intent("android.intent.action.HOME_ENTRANCE");
        shortcutHome.addCategory("android.intent.category.DEFAULT");
        shortcutHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortcutHome);
        SharePreferencesUtil.recordBoolean(this,ConstantValues.HAS_SHORTCUT,true);
        sendBroadcast(intent);
    }


    private void initDatabase() {
        initBasicFunctionDB("address.db");
        initBasicFunctionDB("commonnum.db");
        initBasicFunctionDB("antivirus.db");
    }

    /**
     * Copy address.db to device
     * @param dbName
     */
    private void initBasicFunctionDB(String dbName) {
        File filesDir = getFilesDir();
        File dbFile = new File(filesDir.getAbsolutePath(), dbName);
        if(dbFile.exists()){
            return;
        }
        else {
            try {
                InputStream inputStream = getAssets().open(dbName);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dbFile));
                int len = 0;
                byte[] buffer = new byte[1024];
                while((len=inputStream.read(buffer))!=-1){
                    bufferedOutputStream.write(buffer,0,len);
                }
                bufferedOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Add alpha animation to the entry page
     */
    private void initAnimation() {
        relativeLayout.startAnimation(new MyAlphaAnimation(3000, 0.1f, 1.0f).addAlphaAnimation());
    }

    /**
     * Acquire the UI data
     */
    private void initData() {
        // App version name
        mTextView.setText("Version Name: " + getVersionName());
        // compare the local version code to the server one
        mLocalVersionCode = getVersionCode();
        if (SharePreferencesUtil.getBoolean(this, ConstantValues.AUTO_UPGRADE)) {
            checkVersionCode();
        } else {
            mHandler.sendEmptyMessageDelayed(ConstantValues.ENTER_HOME_PAGE,4000);
        }
    }

    private void checkVersionCode() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                long start = System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.0.6:8080/update.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(2000);
                    httpURLConnection.setReadTimeout(2000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String json = StreamUtil.streamToString(inputStream);
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = (String) jsonObject.get("versionName");
                        mVersionDescription = (String) jsonObject.get("versionDescription");
                        String versionCode = (String) jsonObject.get("versionCode");
                        mDownloadUrl = (String) jsonObject.get("downloadUrl");
                        if (versionCode == null) {
                            versionCode = String.valueOf(mLocalVersionCode);
                        }
                        if (Integer.parseInt(versionCode) > mLocalVersionCode) {
                            message.what = ConstantValues.UPDATE_VERSION;
                        } else {
                            // enter the main page
                            message.what = ConstantValues.ENTER_HOME_PAGE;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = ConstantValues.IO_ERROR;


                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = ConstantValues.JSON_ERROR;
                } finally {
                    long end = System.currentTimeMillis();
                    if (end - start < 4000) {
                        try {
                            Thread.sleep(4000 - (end - start));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }

            }
        }).start();

    }

    /**
     * return the version code
     * 0 means fail to get the code
     *
     * @return
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * Return the version name
     * Return null means exception happens
     *
     * @return
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * Initialize the user interface
     */
    private void initUI() {
        mTextView = findViewById(R.id.tv_version_name);
        relativeLayout = findViewById(R.id.rl_root);
    }
}
