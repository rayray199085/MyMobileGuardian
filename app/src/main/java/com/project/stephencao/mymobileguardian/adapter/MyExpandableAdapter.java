package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Dimension;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.CommonNumClassListItems;

import java.util.List;

public class MyExpandableAdapter extends BaseExpandableListAdapter {
    private List<CommonNumClassListItems> commonNumClassListItemsList;
    private Context context;

    public MyExpandableAdapter(List<CommonNumClassListItems> commonNumClassListItemsList, Context context) {
        this.commonNumClassListItemsList = commonNumClassListItemsList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return commonNumClassListItemsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return commonNumClassListItemsList.get(groupPosition).getCommonNumChildClassListItemsList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return commonNumClassListItemsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return commonNumClassListItemsList.get(groupPosition).getCommonNumChildClassListItemsList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_expandable_adapter_items, null);
        TextView titleTextView =  view.findViewById(R.id.tv_expandable_adapter_items_title);
        titleTextView.setText(commonNumClassListItemsList.get(groupPosition).getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_expandable_adapter_child_items, null);
        TextView nameTextView = view.findViewById(R.id.tv_expandable_adapter_items_name);
        TextView phoneTextView = view.findViewById(R.id.tv_expandable_adapter_items_phone);
        nameTextView.setText(commonNumClassListItemsList.get(groupPosition)
                .getCommonNumChildClassListItemsList().get(childPosition).getName());
        phoneTextView.setText(commonNumClassListItemsList.get(groupPosition)
                .getCommonNumChildClassListItemsList().get(childPosition).getNumber());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
