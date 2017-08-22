package com.meetyou.bus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.meetyou.bus.R;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.model.StationDO;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by clarence on 2017/8/17.
 */

public class StationManagerActivity extends Activity {
    EditText editName;
    EditText editNum;
    ListView listView;

    StationManager stationManager;
    StationManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_manager);
        initView();
        initData();
    }

    private void initView() {
        editName = (EditText) findViewById(R.id.editName);
        editNum = (EditText) findViewById(R.id.editNum);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initData() {
        stationManager = new StationManager();
        adapter = new StationManagerAdapter(this, stationManager);
        listView.setAdapter(adapter);
        Action1 action1 = new Action1<List<StationDO>>() {
            @Override
            public void call(List<StationDO> userDOs) {
                adapter.setStations(userDOs);
                editNum.setText(adapter.getCount() + 1 + "");
                editName.requestFocus();
            }
        };
        stationManager.updateStationList(action1);
    }

    public void addUser(View view) {
        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = editNum.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "序号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Action1 action = new Action1<StationDO>() {
            @Override
            public void call(StationDO stationDO) {
                Action1 action1 = new Action1<List<StationDO>>() {
                    @Override
                    public void call(List<StationDO> userDOs) {
                        adapter.setStations(userDOs);
                        editNum.setText(adapter.getCount() + 1 + "");
                        editName.setText("");
                        editName.requestFocus();
                    }
                };
                stationManager.updateStationList(action1);
            }
        };
        stationManager.addStation(name, Integer.valueOf(phone), action);

    }
}
