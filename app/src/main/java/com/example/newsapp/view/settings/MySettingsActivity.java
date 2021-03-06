package com.example.newsapp.view.settings;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.newsapp.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.singleitem.SwitchButton;

public class MySettingsActivity extends AppCompatActivity {
    private MyApplication parentApplication;
    private UiModeManager mUiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        parentApplication = (MyApplication) getApplication();
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        boolean dayMode = parentApplication.getDayMode();
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);

        switchButton.setChecked(dayMode);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (parentApplication.getDayMode()) {
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                    parentApplication.setDayMode(false);
                }
                else {
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                    parentApplication.setDayMode(true);
                }
            }
        });

    }

}
