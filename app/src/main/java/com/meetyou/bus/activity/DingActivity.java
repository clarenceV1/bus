package com.meetyou.bus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.meetyou.bus.Constant;
import com.meetyou.bus.R;
import com.meetyou.bus.manager.DingManager;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.model.DingDingRobotAtModel;
import com.meetyou.bus.model.DingDingRobotModel;
import com.meetyou.bus.model.DingDingRobotTextModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by clarence on 2017/8/17.
 */

public class DingActivity extends Activity {
    ListView listView;
    DingAdapter adapter;
    DingManager dingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ding_content);

        listView = (ListView) findViewById(R.id.listView);
        dingManager = new DingManager();
        adapter = new DingAdapter(this, dingManager);
        listView.setAdapter(adapter);
        dingManager.updateStationList(adapter);
    }
}
