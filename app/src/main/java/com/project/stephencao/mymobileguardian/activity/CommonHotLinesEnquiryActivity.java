package com.project.stephencao.mymobileguardian.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.adapter.MyExpandableAdapter;
import com.project.stephencao.mymobileguardian.bean.CommonNumClassListItems;
import com.project.stephencao.mymobileguardian.engine.CommonNumDao;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

import java.util.List;

public class CommonHotLinesEnquiryActivity extends AppCompatActivity {
    private ExpandableListView mExpandableListView;
    private List<CommonNumClassListItems> mGroupData;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==0){
               MyExpandableAdapter myExpandableAdapter = new MyExpandableAdapter(
                       mGroupData,CommonHotLinesEnquiryActivity.this);
                mExpandableListView.setAdapter(myExpandableAdapter);
           }
           mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
               @SuppressLint("MissingPermission")
               @Override
               public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                   String number = mGroupData.get(groupPosition).getCommonNumChildClassListItemsList().get(childPosition).getNumber();
                   Intent intent = new Intent(Intent.ACTION_CALL);
                   intent.setData(Uri.parse("tel:"+number));
                   startActivity(intent);
                   return false;
               }
           });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_hot_lines_enquiry);
        initUI();
        initData();
    }

    /**
     * Prepare and insert data into expandable list view
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGroupData = CommonNumDao.getGroupData();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initUI() {
        mExpandableListView = findViewById(R.id.elv_common_hot_lines_enquiry);
    }
}
