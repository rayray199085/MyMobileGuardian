package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.IconItems;

import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {
    private List<IconItems> itemsList;
    private Context context;
    private LayoutInflater inflater;

    public MyGridViewAdapter(List<IconItems> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsList.get(position);
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
            convertView = inflater.inflate(R.layout.gridview_icon_items, null);
            viewHolder.iconName = convertView.findViewById(R.id.view_icon_name);
            viewHolder.iconPic = convertView.findViewById(R.id.view_icon_pic);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        IconItems iconItems = itemsList.get(position);
        viewHolder.iconName.setText(iconItems.getIconNames());
        viewHolder.iconPic.setBackgroundResource(iconItems.getIconID());
        return convertView;
    }

    class ViewHolder {
        public ImageView iconPic;
        public TextView iconName;
    }
}
