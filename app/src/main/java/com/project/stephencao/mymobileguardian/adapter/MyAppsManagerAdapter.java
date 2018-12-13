package com.project.stephencao.mymobileguardian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.AppsManagersListItems;

import java.util.List;

public class MyAppsManagerAdapter extends BaseAdapter {
    private List<AppsManagersListItems> appsManagersListItemsList;
    private Context context;
    private LayoutInflater inflater;
    private int userAppCount;

    public MyAppsManagerAdapter(List<AppsManagersListItems> appsManagersListItemsList, Context context, int userAppCount) {
        this.appsManagersListItemsList = appsManagersListItemsList;
        this.context = context;
        this.userAppCount = userAppCount;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position == userAppCount + 1)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return appsManagersListItemsList.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || (position == userAppCount + 1)) {
            return null;
        } else {
            if (position < userAppCount + 1) {
                return appsManagersListItemsList.get(position - 1);
            } else {
                return appsManagersListItemsList.get(position - 2);
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
                convertView = inflater.inflate(R.layout.listview_apps_manager_items, null);
                viewHolder = new ViewHolder();
                viewHolder.iconImageView = convertView.findViewById(R.id.iv_apps_manager_icon);
                viewHolder.appNameTextView = convertView.findViewById(R.id.tv_apps_manager_apps_name);
                viewHolder.typeTextView = convertView.findViewById(R.id.tv_apps_manager_apps_type);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int tempPosition = 0;
            if (position < userAppCount + 1) {
                tempPosition = position - 1;
            } else {
                tempPosition = position - 2;
            }
            AppsManagersListItems appsManagersListItems = appsManagersListItemsList.get(tempPosition);
            viewHolder.iconImageView.setBackgroundDrawable(appsManagersListItems.getAppIcon());
            viewHolder.appNameTextView.setText(appsManagersListItems.getAppName());
            if (appsManagersListItems.isStoreInSDCard()) {
                viewHolder.typeTextView.setText("This app is stored in SD Card.");
            } else {
                viewHolder.typeTextView.setText("This app is stored in the internal storage.");
            }
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
                textViewHolder.subTitleTextView.setText("User Application (" + userAppCount + ")");
            } else if (position == userAppCount + 1) {
                int sysAppCount = appsManagersListItemsList.size() - userAppCount;
                textViewHolder.subTitleTextView.setText("System Application (" + sysAppCount + ")");
            }
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView iconImageView;
        public TextView appNameTextView, typeTextView;
    }

    class TextViewHolder {
        public TextView subTitleTextView;
    }
}
