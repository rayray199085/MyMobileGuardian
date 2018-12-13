package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.BlacklistItems;

import java.util.List;

public class MyBlackListAdapter extends BaseAdapter {
    private List<BlacklistItems> blacklistItemsList;
    private Context context;
    private LayoutInflater inflater;

    public MyBlackListAdapter(List<BlacklistItems> blacklistItemsList, Context context) {
        this.blacklistItemsList = blacklistItemsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return blacklistItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return blacklistItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_settings_styles, null);
            viewHolder.titleTextView = convertView.findViewById(R.id.tv_title_setting_styles);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.tv_description_setting_styles);
            viewHolder.iconImageView = convertView.findViewById(R.id.iv_styles_setting);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BlacklistItems blacklistItems = blacklistItemsList.get(position);
        viewHolder.titleTextView.setText(blacklistItems.getPhoneNumber());
        viewHolder.descriptionTextView.setText(blacklistItems.getType());
        viewHolder.iconImageView.setBackgroundResource(R.drawable.rubbish_bin);
        return convertView;

    }
    class ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        private ImageView iconImageView;
    }
}
