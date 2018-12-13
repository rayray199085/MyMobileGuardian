package com.project.stephencao.mymobileguardian.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.BlacklistItems;
import com.project.stephencao.mymobileguardian.adapter.MyBlackListAdapter;
import com.project.stephencao.mymobileguardian.engine.BlacklistDao;

import java.util.ArrayList;
import java.util.List;

public class BlacklistManagerActivity extends AppCompatActivity {
    private ListView mListView;
    private ImageView mImageView;
    private MyBlackListAdapter myBlackListAdapter;
    private List<BlacklistItems> mBlacklistItemsList;
    private long[] mHits = new long[2];
    private boolean mIsLoad = false;
    private int mIndex = 0;
    private int mDataItemsCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    myBlackListAdapter.notifyDataSetChanged();
                    mIsLoad = false;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist_manager);
        initUI();
        initData();
    }

    private void initData() {
        mDataItemsCount = BlacklistDao.getDataItemsCount(this);
        mBlacklistItemsList = new ArrayList<>();
        initItemList();
        myBlackListAdapter = new MyBlackListAdapter(mBlacklistItemsList, this);
        mListView.setAdapter(myBlackListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mBlacklistItemsList.size() && mBlacklistItemsList.get(position) != null) {
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                        BlacklistItems blacklistItems = mBlacklistItemsList.get(position);
                        BlacklistDao.deleteItemFromDB(BlacklistManagerActivity.this, blacklistItems.getPhoneNumber(), blacklistItems.getType());
                        mBlacklistItemsList.remove(position);
                        myBlackListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mBlacklistItemsList != null && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        mListView.getLastVisiblePosition() >= mBlacklistItemsList.size() - 1 && !mIsLoad) {
                    if (mDataItemsCount > mBlacklistItemsList.size()) {
                        mIsLoad = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Cursor cursor = BlacklistDao.queryPartialItemsFromDB(
                                        BlacklistManagerActivity.this, mBlacklistItemsList.size());
                                putItemIntoList(cursor);
                                Message message = Message.obtain();
                                message.what = 1;
                                mHandler.sendMessage(message);
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initItemList() {
        mBlacklistItemsList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = BlacklistDao.queryPartialItemsFromDB(BlacklistManagerActivity.this, mIndex);
                putItemIntoList(cursor);
            }
        }).start();
    }

    private void putItemIntoList(Cursor cursor) {
        while (cursor.moveToNext()) {
            BlacklistItems blacklistItems = new BlacklistItems();
            blacklistItems.setPhoneNumber(cursor.getString(0));
            blacklistItems.setType(cursor.getString(1));
            mBlacklistItemsList.add(blacklistItems);
        }
    }

    private void initUI() {
        mListView = findViewById(R.id.lv_blacklist_manager);
        mImageView = findViewById(R.id.iv_blacklist_manager);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });
    }

    private void displayDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_blacklist, null);
        alertDialog.setView(view);
        final EditText editText = view.findViewById(R.id.et_blacklist_choice_dialog);
        final RadioGroup radioGroup = view.findViewById(R.id.rg_blacklist_choice_dialog);
        radioGroup.check(R.id.rbtn_blacklist_dialog_text);
        final BlacklistItems blacklistItems = new BlacklistItems();

        Button confirmButton = view.findViewById(R.id.btn_blacklist_choice_dialog_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(editText.getText().toString()) && radioGroup.getCheckedRadioButtonId() != -1) {
                    String regex = "\\d+";
                    if (editText.getText().toString().matches(regex)) {
                        blacklistItems.setPhoneNumber(editText.getText().toString());
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.rbtn_blacklist_dialog_text: {
                                blacklistItems.setType("SMS");
                                break;
                            }
                            case R.id.rbtn_blacklist_dialog_call: {
                                blacklistItems.setType("Call");
                                break;
                            }
                            case R.id.rbtn_blacklist_dialog_both: {
                                blacklistItems.setType("Both");
                                break;
                            }
                        }
                        alertDialog.dismiss();
                        mBlacklistItemsList.add(0, blacklistItems);
                        BlacklistDao.insertItemToDB(BlacklistManagerActivity.this, blacklistItems.getPhoneNumber(), blacklistItems.getType());
                        myBlackListAdapter.notifyDataSetChanged();
                    } else {
                        editText.setText("");
                    }
                }
            }
        });
        Button cancelButton = view.findViewById(R.id.btn_blacklist_choice_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
