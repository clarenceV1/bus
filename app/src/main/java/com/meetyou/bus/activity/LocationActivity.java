package com.meetyou.bus.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.meetyou.bus.PermissionCallBack;
import com.meetyou.bus.PermissionEnum;
import com.meetyou.bus.PermissionsManager;
import com.meetyou.bus.PermissionsResultAction;
import com.meetyou.bus.R;
import com.meetyou.bus.manager.DingManager;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.model.StationDO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.functions.Action1;

/**
 * AMapV2地图中介绍定位几种类型
 */
public class LocationActivity extends Activity implements AMap.OnMyLocationChangeListener, AMapLocationListener {

    private MapView mapView;
    TextView tvDistance;
    private Button nextStation;

    private MyLocationStyle myLocationStyle;
    private LatLng latlngA = new LatLng(24.489404, 118.178577);
    private AMap aMap;
    private boolean isFirst = true;

    DingManager dingManager;
    StationManager stationManager;
    List<StationDO> stationList;
    private int currentNum;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        initView();
        requestPermissions(this, PermissionEnum.EXTERNAL_LOCATION, new PermissionCallBack() {
            @Override
            public void permission() {
                initMap(savedInstanceState);
                initData();
            }

            @Override
            public void denied() {
                Toast.makeText(LocationActivity.this, "需要定位权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.map);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        nextStation = (Button) findViewById(R.id.nextStation);

        nextStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stationList != null && stationList.size() > 0 && currentNum < stationList.size()) {
                    currentNum++;
                    hideJump();
                }
            }
        });
    }

    /**
     * 跳过此站
     */
    private void hideJump() {
        if (currentNum == stationList.size()) {
            nextStation.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化
     */
    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        setLocationStyle(7);

    }

    private void initData() {
        stationManager = new StationManager();
        stationManager.addMarkers(this, aMap, false);
        stationList = stationManager.getStationList();
        if (stationList != null && stationList.size() > 0) {
            List<StationDO> list = new ArrayList<>();
            long rightnowTime = Calendar.getInstance().getTimeInMillis();
            for (StationDO stationDO : stationList) {
                if (stationDO.getLatitude() == 0 && stationDO.getLongitude() == 0) {
                    continue;
                }
                stationDO.setSameDay(stationManager.isSameDay(rightnowTime, stationDO.getArriveTime()));
                list.add(stationDO);
            }
            stationList.clear();
            stationList.addAll(list);
        }
        dingManager = new DingManager();
    }

    public void setLocationStyle(int position) {
        switch (position) {
            case 0:
                // 只定位，不进行其他操作
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
                break;
            case 1:
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
                break;
            case 2:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));
                break;
            case 3:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE));
                break;
            case 4:
                // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
                break;
            case 5:
                // 定位、但不会移动到地图中心点，并且会跟随设备移动。
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER));
                break;
            case 6:
                // 定位、但不会移动到地图中心点，地图依照设备方向旋转，并且会跟随设备移动。
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER));
                break;
            case 7:
                // 定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
                break;
        }
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

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            LatLng latLngB = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirst) {
                isFirst = false;
                //将地图移动到定位点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngB));
                currentNum = getCurrentNum(stationList, latLngB);
            }
            if (stationList != null && stationList.size() > 0 && currentNum < stationList.size()) {
                final StationDO station = stationList.get(currentNum);
                LatLng ll = new LatLng(station.getLatitude(), station.getLongitude());
                double distance = AMapUtils.calculateLineDistance(ll, latLngB);

                StringBuilder sb = new StringBuilder();
                sb.append("距离下一站");
                sb.append("(");
                sb.append(station.getName());
                sb.append(")");
                sb.append((int) distance);
                sb.append("米");
                tvDistance.setText(sb);

                if (station.isSameDay()) {
                    currentNum++;
                    hideJump();
                } else if (distance <= 200) {
                    station.setSameDay(true);
                    currentNum++;
                    hideJump();
                    station.setArriveTime(Calendar.getInstance().getTimeInMillis());
                    Action1 action1 = new Action1() {
                        @Override
                        public void call(Object o) {
                            dingManager.sendDing(LocationActivity.this, station);

                            Toast.makeText(LocationActivity.this, "已经发出", Toast.LENGTH_SHORT).show();
                        }
                    };
                    stationManager.updateStation(station, action1);
                }
            }

            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");
            }
        } else {
            Log.e("amap", "定位失败");
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


    }

    //请求权限
    public void requestPermissions(Activity activity, PermissionEnum permissionEnum, final PermissionCallBack callBack) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, permissionEnum.permission, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                callBack.permission();
            }

            @Override
            public void onDenied(String permission) {
                callBack.denied();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * 获取最近距离的站点
     *
     * @param stations
     * @param latLngB
     * @return
     */
    public int getCurrentNum(List<StationDO> stations, LatLng latLngB) {
        int position = 0;
        if (stations != null && stations.size() > 0) {
            double cache = Double.MAX_VALUE;
            for (int i = 0; i < stations.size(); i++) {
                StationDO station = stations.get(i);
                LatLng ll = new LatLng(station.getLatitude(), station.getLongitude());
                double distance = AMapUtils.calculateLineDistance(ll, latLngB);
                if (distance < cache) {
                    position = i;
                    cache = distance;
                }
            }
        }
        return position;
    }
}
