package com.project.stephencao.mymobileguardian.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TabHost;
import com.project.stephencao.mymobileguardian.R;

public class CachedDataActivity extends TabActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cached_data_base);
        TabHost.TabSpec tabSpecCachedData = getTabHost().newTabSpec("clear_cached_data").setIndicator("Clear Cached Data");
        TabHost.TabSpec tabSpecSDCardCachedData = getTabHost().newTabSpec("sd_card_cached_data").setIndicator("Clear SD Card cache");

        tabSpecCachedData.setContent(new Intent(this,ClearCachedDataActivity.class));
        tabSpecSDCardCachedData.setContent(new Intent(this,ClearSDCardDataActivity.class));

        getTabHost().addTab(tabSpecCachedData);
        getTabHost().addTab(tabSpecSDCardCachedData);

    }
}
