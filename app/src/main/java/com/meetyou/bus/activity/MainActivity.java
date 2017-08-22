package com.meetyou.bus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.meetyou.bus.Constant;
import com.meetyou.bus.R;

public class MainActivity extends AppCompatActivity {

    Switch switchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniEnv();
    }

    private void iniEnv() {
        switchView = (Switch) findViewById(R.id.switchView);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    Constant.isDebug = true;
                    switchView.setText("测试环境");
                } else {
                    Constant.isDebug = false;
                    switchView.setText("正式环境");
                }
            }
        });
        switchView.setText("测试环境");
        switchView.setChecked(false);
    }

    public void managerStation(View view) {
        Intent intent = new Intent();
        intent.setClass(this, StationManagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void managerStationMap(View view) {
        Intent intent = new Intent();
        intent.setClass(this, StationMapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void managerUser(View view) {
        Intent intent = new Intent();
        intent.setClass(this, UserManagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void location(View view) {
        Intent intent = new Intent();
        intent.setClass(this, LocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
