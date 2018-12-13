package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.MissionsManagerListItems;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

import java.util.List;

public class MyMissionsManagerAdapter extends BaseAdapter {
    private List<MissionsManagerListItems> mUserMissionList;
    private List<MissionsManagerListItems> mSystemMissionList;
    private Context context;
    private LayoutInflater inflater;

    public MyMissionsManagerAdapter(List<MissionsManagerListItems> mUserMissionList, List<MissionsManagerListItems> mSystemMissionList, Context context) {
        this.mUserMissionList = mUserMissionList;
        this.mSystemMissionList = mSystemMissionList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position == mUserMissionList.size() + 1)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        if(SharePreferencesUtil.getBoolean(context, ConstantValues.HIDE_SYSTEM_MISSIONS)){
            return mUserMissionList.size() + 1;
        }
        return mUserMissionList.size() + mSystemMissionList.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || (position == mUserMissionList.size() + 1)) {
            return null;
        } else {
            if (position < mUserMissionList.size() + 1) {
                return mUserMissionList.get(position - 1);
            } else {
                return mSystemMissionList.get(position - mUserMissionList.size() - 2);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_missions_manager_items, null);
                viewHolder = new ViewHolder();
                viewHolder.iconImageView = convertView.findViewById(R.id.iv_missions_manager_icon);
                viewHolder.missionNameTextView = convertView.findViewById(R.id.tv_missions_manager_mission_name);
                viewHolder.spaceOccupationTextView = convertView.findViewById(R.id.tv_missions_manager_mission_occupy_space);
                viewHolder.checkBox = convertView.findViewById(R.id.cb_missions_manager_is_chosen);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MissionsManagerListItems missionsManagerListItems = (MissionsManagerListItems) getItem(position);

            if (missionsManagerListItems.getPackageName().equals(context.getPackageName())) {
                viewHolder.checkBox.setVisibility(View.GONE);
            } else {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            }
            viewHolder.checkBox.setChecked(missionsManagerListItems.isChosen());

            viewHolder.iconImageView.setBackgroundDrawable(missionsManagerListItems.getMissionIcon());
            viewHolder.missionNameTextView.setText(missionsManagerListItems.getMissionName());
            long ramSpaceOccupation = missionsManagerListItems.getRamSpaceOccupation();
            String spaceConsumption = Formatter.formatFileSize(context, ramSpaceOccupation);
            viewHolder.spaceOccupationTextView.setText("This mission consumes " + spaceConsumption);
            return convertView;
        } else {
            TextViewHolder textViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_apps_manager_pure_text_view, null);
                textViewHolder = new TextViewHolder();
                textViewHolder.subTitleTextView = convertView.findViewById(R.id.tv_apps_manager_pure_text);
                convertView.setTag(textViewHolder);
            } else {
                textViewHolder = (TextViewHolder) convertView.getTag();
            }
            if (position == 0) {
                textViewHolder.subTitleTextView.setText("User Mission (" + mUserMissionList.size() + ")");
            } else if (position == mUserMissionList.size() + 1) {
                int sysMissionCount = mSystemMissionList.size();
                textViewHolder.subTitleTextView.setText("System Mission (" + sysMissionCount + ")");
            }
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView iconImageView;
        public TextView missionNameTextView, spaceOccupationTextView;
        public CheckBox checkBox;
    }

    class TextViewHolder {
        public TextView subTitleTextView;
    }
}
