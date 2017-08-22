package com.meetyou.bus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.meetyou.bus.R;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.manager.UserManager;
import com.meetyou.bus.model.StationDO;

import java.util.List;

import rx.functions.Action1;


/**
 * Created by clarence on 2017/8/17.
 */

public class UserManagerActivity extends Activity {
    EditText editName;
    EditText editPhone;
    ListView listView;
    Spinner spStation;

    UserManager userManager;
    StationManager stationManager;
    UserManagerAdapter adapter;
    StationManagerAdapter stationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manager);
        initView();
        initData();
    }

    private void initView() {
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        listView = (ListView) findViewById(R.id.listView);
        spStation = (Spinner) findViewById(R.id.spStation);
    }

    private void initData() {
        stationManager = new StationManager();
        stationAdapter = new StationManagerAdapter(this, stationManager);
        stationAdapter.isStationList(true);
        Action1 action1 = new Action1<List<StationDO>>() {
            @Override
            public void call(List<StationDO> userDOs) {
                stationAdapter.setStations(userDOs);
            }
        };
        stationManager.updateStationList(action1);
        spStation.setAdapter(stationAdapter);

        userManager = new UserManager();
        adapter = new UserManagerAdapter(this, userManager);
        listView.setAdapter(adapter);
        userManager.updateUserList(adapter);

    }

    public void addUser(View view) {
        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = editPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        StationDO stationDO = (StationDO) spStation.getSelectedItem();
        userManager.addUser(name, phone, stationDO.getId(), stationDO.getName(), adapter);
        editName.setText("");
        editPhone.setText("");
    }
}
