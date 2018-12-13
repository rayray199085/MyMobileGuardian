package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;

import java.util.List;

public class MyContactListViewAdapter extends BaseAdapter {
    private List<ContactPerson> personList;
    private Context context;
    private LayoutInflater inflater;

    public MyContactListViewAdapter(List<ContactPerson> personList, Context context) {
        this.personList = personList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyContactListViewAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_contact_items, null);
            viewHolder.nameTextView = convertView.findViewById(R.id.tv_contact_name);
            viewHolder.phoneTextView = convertView.findViewById(R.id.tv_contact_phone);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactPerson contactPerson = personList.get(position);
        viewHolder.nameTextView.setText(contactPerson.getName());
        viewHolder.phoneTextView.setText(contactPerson.getPhone());
        return convertView;
    }

    class ViewHolder {
        public TextView nameTextView;
        public TextView phoneTextView;
    }
}
