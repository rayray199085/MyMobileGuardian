package com.project.stephencao.mymobileguardian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;

public class MySettingsItemsView extends RelativeLayout  {
    private TextView titleTextView, descriptionTextView;
    private CheckBox checkBox;
    private int mDescriptionStyle;
    public MySettingsItemsView(Context context) {
        this(context,null);
    }

    public MySettingsItemsView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySettingsItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_settings_items,this);
        titleTextView = findViewById(R.id.tv_title_setting);
        descriptionTextView = findViewById(R.id.tv_description_setting);
        checkBox = findViewById(R.id.cb_status_setting);
    }


    public boolean setCheckBoxChecked(){
        if(checkBox.isChecked()){
            checkBox.setChecked(false);
            descriptionChange(false);
            return false;
        }
        else{
            checkBox.setChecked(true);
            descriptionChange(true);
            return true;
        }
    }

    /**
     * Provide two public methods for SettingActivity to set text views content
     * @param content
     */
    public void setTitleContent(String content){
        titleTextView.setText(content);
    }
    public void setDescriptionContent(String content){
        descriptionTextView.setText(content);
    }

    public void setCheckBoxStatus(boolean flag){
        checkBox.setChecked(flag);
        descriptionChange(flag);
    }
    public void setDescriptionStyle(int style){
        mDescriptionStyle = style;
    }
    /**
     * Increase the function's compatibility by using different word description styles
     * According to the check box status to change the description
     * @param flag
     */
    private void descriptionChange(boolean flag){
        String switchOn = "";
        String switchOff = "";
        switch(mDescriptionStyle){
            case ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED:{
                switchOn = ConstantValues.SETTINGS_STATUS_OPEN;
                switchOff = ConstantValues.SETTINGS_STATUS_CLOSE;
                break;
            }
            case ConstantValues.DESCRIPTION_STYLE_BIND_UNBIND:{
                switchOn = ConstantValues.SETTINGS_STATUS_BIND;
                switchOff = ConstantValues.SETTINGS_STATUS_UNBIND;
                break;
            }
        }
        if(!flag){
            if(descriptionTextView.getText()!=null){
                String content = (String) descriptionTextView.getText();
                descriptionTextView.setText(content.replaceAll(switchOn,switchOff));
            }
        }
        else{
            if(descriptionTextView.getText()!=null){
                String content = (String) descriptionTextView.getText();
                descriptionTextView.setText(content.replaceAll(switchOff,switchOn));
            }
        }
    }
}
