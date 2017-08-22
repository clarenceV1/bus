package com.meetyou.bus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.meetyou.bus.R;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.model.StationDO;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by clarence on 2017/8/18.
 */

public class StationMapActivity extends Activity implements AMap.OnMarkerDragListener {

    private MapView mapView;
    TextView infoText;
    Spinner spStation;
    Button btnAddMarker;

    StationManager stationManager;
    StationManagerAdapter stationAdapter;
    private AMap aMap;
    private LatLng latlngA = new LatLng(24.489404, 118.178577);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_map);
        initView();
        initMap(savedInstanceState);
        initData();
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        infoText = (TextView) findViewById(R.id.infoText);
        spStation = (Spinner) findViewById(R.id.spStation);
        btnAddMarker = (Button) findViewById(R.id.btnAddMarker);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerDragListener(this);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngA, 15));
        }

    }

    private void initData() {
        btnAddMarker.setVisibility(View.GONE);

        stationManager = new StationManager();
        stationAdapter = new StationManagerAdapter(this, stationManager);
        stationAdapter.isStationList(true);
        spStation.setAdapter(stationAdapter);
        Action1 action1 = new Action1<List<StationDO>>() {
            @Override
            public void call(List<StationDO> userDOs) {
                stationAdapter.setStations(userDOs);
            }
        };
        stationManager.updateStationList(action1);
        spStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        stationManager.addMarkers(this, aMap, true);
    }

    private void spinnerSelected() {
        StationDO stationDO = (StationDO) spStation.getSelectedItem();
        if (stationDO.getLatitude() != 0 && stationDO.getLongitude() != 0) {
            LatLng latLng = new LatLng(stationDO.getLatitude(), stationDO.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            btnAddMarker.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "未添加", Toast.LENGTH_SHORT).show();
            btnAddMarker.setVisibility(View.VISIBLE);
        }
    }

    public void addNewMarker(View view) {
        StationDO stationDO = (StationDO) spStation.getSelectedItem();
        stationDO.setLongitude(latlngA.longitude);
        stationDO.setLatitude(latlngA.latitude);
        Marker marker = stationManager.addMarker(this, stationDO, aMap, true);
        save(marker);
        btnAddMarker.setVisibility(View.GONE);
    }

    public void save(Marker marker) {
        StationDO stationDO = (StationDO) marker.getObject();
        stationDO.setLatitude(marker.getPosition().latitude);
        stationDO.setLongitude(marker.getPosition().longitude);
        Action1 action1 =  new Action1<StationDO>() {
            @Override
            public void call(StationDO stationDO) {
                Toast.makeText(StationMapActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        };
        stationManager.updateStation(stationDO,action1);
    }

    @Override
    public void onMarkerDragStart(com.amap.api.maps.model.Marker marker) {

    }

    @Override
    public void onMarkerDrag(com.amap.api.maps.model.Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(com.amap.api.maps.model.Marker marker) {
        infoText.setText("lng:" + marker.getPosition().longitude + "  lat:" + marker.getPosition().latitude);
        save(marker);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
