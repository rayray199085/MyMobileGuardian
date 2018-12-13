package com.project.stephencao.mymobileguardian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.*;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.adapter.MyMissionsManagerAdapter;
import com.project.stephencao.mymobileguardian.bean.MissionsManagerListItems;
import com.project.stephencao.mymobileguardian.engine.AcquireMissionInfo;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MissionsManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mMissionNumTextView, mAvailableRAMTextView, mMissionCountDisplayTextView;
    private ListView mMissionListView;
    private int mTotalNumberOfMissions;
    private Button mSelectAllButton, mReverseAllButton, mClearAllButton, mSettingsButton;
    private List<MissionsManagerListItems> mUserMissionList;
    private List<MissionsManagerListItems> mSystemMissionList;
    private ListView mListView;
    private long mAvailableRAMSpace;
    private String mTotalSpace;
    private MyMissionsManagerAdapter myMissionsManagerAdapter;
    private MissionsManagerListItems missionsManagerListItems;
    private List<MissionsManagerListItems> missionsManagerListItemsList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                myMissionsManagerAdapter = new MyMissionsManagerAdapter(mUserMissionList, mSystemMissionList,
                        MissionsManagerActivity.this);
                mListView.setAdapter(myMissionsManagerAdapter);
                mMissionCountDisplayTextView.setText("User Mission (" + mUserMissionList.size() + ")");
            }
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (missionsManagerListItemsList != null) {
                        if (firstVisibleItem > mUserMissionList.size()) {
                            mMissionCountDisplayTextView.setText("System Application (" + mSystemMissionList.size() + ")");
                        } else {
                            mMissionCountDisplayTextView.setText("User Application (" + mUserMissionList.size() + ")");
                        }
                    }
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0 || position == mUserMissionList.size() + 1) {
                        return;
                    } else {
                        if (position < mUserMissionList.size() + 1) {
                            missionsManagerListItems = mUserMissionList.get(position - 1);
                        } else {
                            missionsManagerListItems = mSystemMissionList.get(position - 2);
                        }
                    }
                    CheckBox checkBox = view.findViewById(R.id.cb_missions_manager_is_chosen);
                    if (!missionsManagerListItems.getPackageName().equals(MissionsManagerActivity.this.getPackageName())) {
                        if (!missionsManagerListItems.isChosen()) {
                            checkBox.setChecked(true);
                            missionsManagerListItems.setChosen(true);
                        } else {
                            checkBox.setChecked(false);
                            missionsManagerListItems.setChosen(false);
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions_manager);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTitle();
        initListViewData();
    }

    private void initListViewData() {
        mUserMissionList = new ArrayList<>();
        mSystemMissionList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                missionsManagerListItemsList = AcquireMissionInfo.getMissionInfo(MissionsManagerActivity.this);
                for (MissionsManagerListItems missionsManagerListItems : missionsManagerListItemsList) {
                    if (missionsManagerListItems.isSystemMission()) {
                        mSystemMissionList.add(missionsManagerListItems);
                    } else {
                        mUserMissionList.add(missionsManagerListItems);
                    }
                }
                missionsManagerListItemsList.clear();
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void initTitle() {
        mTotalNumberOfMissions = AcquireMissionInfo.getTotalNumberOfMissions(this);
        mMissionNumTextView.setText("Number of Running Missions: " + mTotalNumberOfMissions);
        mAvailableRAMSpace = AcquireMissionInfo.getAvailableRAMSpace(this);
        long totalRAMSpace = AcquireMissionInfo.getTotalRAMSpace(this);
        String availableSpace = Formatter.formatFileSize(this, mAvailableRAMSpace);
        mTotalSpace = Formatter.formatFileSize(this, totalRAMSpace);
        mAvailableRAMTextView.setText("Available/Total RAM Space: " + availableSpace + "/" + mTotalSpace);
    }

    private void initUI() {
        mMissionCountDisplayTextView = findViewById(R.id.tv_missions_manager_mission_count);
        mMissionNumTextView = findViewById(R.id.tv_missions_manager_mission_number);
        mAvailableRAMTextView = findViewById(R.id.tv_missions_manager_available_ram);
        mMissionListView = findViewById(R.id.lv_missions_manager_list);
        mSelectAllButton = findViewById(R.id.btn_missions_manager_select_all);
        mReverseAllButton = findViewById(R.id.btn_missions_manager_reverse_all);
        mClearAllButton = findViewById(R.id.btn_missions_manager_clear_all);
        mSettingsButton = findViewById(R.id.btn_missions_manager_settings);
        mListView = findViewById(R.id.lv_missions_manager_list);

        mSelectAllButton.setOnClickListener(this);
        mReverseAllButton.setOnClickListener(this);
        mClearAllButton.setOnClickListener(this);
        mSettingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_missions_manager_select_all: {
                for (MissionsManagerListItems item : mUserMissionList) {
                    if (!item.getPackageName().equals(this.getPackageName())) {
                        item.setChosen(true);
                    }
                }
                for (MissionsManagerListItems item : mSystemMissionList) {
                    item.setChosen(true);
                }
                if (myMissionsManagerAdapter != null) {
                    myMissionsManagerAdapter.notifyDataSetChanged();
                }
                break;
            }
            case R.id.btn_missions_manager_reverse_all: {
                for (MissionsManagerListItems item : mUserMissionList) {
                    if (!item.getPackageName().equals(this.getPackageName())) {
                        item.setChosen(!item.isChosen());
                    }
                }
                for (MissionsManagerListItems item : mSystemMissionList) {
                    item.setChosen(!item.isChosen());
                }
                if (myMissionsManagerAdapter != null) {
                    myMissionsManagerAdapter.notifyDataSetChanged();
                }
                break;
            }
            case R.id.btn_missions_manager_clear_all: {
                List<MissionsManagerListItems> removeList = new ArrayList<>();
                for (MissionsManagerListItems item : mUserMissionList) {
                    if (!item.getPackageName().equals(this.getPackageName()) && item.isChosen()) {
                        removeList.add(item);
                    }
                }
                for (MissionsManagerListItems item : mSystemMissionList) {
                    if (item.isChosen()) {
                        removeList.add(item);
                    }
                }
                long totalRAMSpaceRelease = 0;
                for (MissionsManagerListItems item : removeList) {
                    if (mUserMissionList.contains(item)) {
                        mUserMissionList.remove(item);
                    } else if (mSystemMissionList.contains(item)) {
                        mSystemMissionList.remove(item);
                    }
                    totalRAMSpaceRelease += item.getRamSpaceOccupation();
                    AcquireMissionInfo.killProcess(this, item);
                }

                if (myMissionsManagerAdapter != null) {
                    myMissionsManagerAdapter.notifyDataSetChanged();
                }

                // update the status and mission quantity display
                mTotalNumberOfMissions -= removeList.size();
                mAvailableRAMSpace += totalRAMSpaceRelease;
                String updatedRAMAvailableSpace = Formatter.formatFileSize(this, mAvailableRAMSpace);
                mMissionNumTextView.setText("Number of Running Missions: " + mTotalNumberOfMissions);
                mAvailableRAMTextView.setText("Available/Total RAM Space: " + updatedRAMAvailableSpace + "/" + mTotalSpace);
                ToastUtil.show(this, removeList.size() + " " +
                        "missions have been killed, and "
                        + Formatter.formatFileSize(this, totalRAMSpaceRelease) + " space is released.");
                break;
            }
            case R.id.btn_missions_manager_settings: {
                startActivity(new Intent(this, MissionsManagerSettingActivity.class));
                break;
            }
        }
    }

}
