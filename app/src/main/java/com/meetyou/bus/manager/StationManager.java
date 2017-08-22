package com.meetyou.bus.manager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.meetyou.bus.App;
import com.meetyou.bus.R;
import com.meetyou.bus.activity.StationManagerAdapter;
import com.meetyou.bus.activity.StationMapActivity;
import com.meetyou.bus.model.DaoSession;
import com.meetyou.bus.model.StationDO;
import com.meetyou.bus.model.StationDODao;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by clarence on 2017/8/17.
 */

public class StationManager {
    DaoSession daoSession;
    RxDao<StationDO, Long> stationRxDao;
    RxQuery<StationDO> stationRxQueryDao;

    public StationManager() {
        daoSession = App.getDaoSession();
        stationRxDao = daoSession.getStationDODao().rx();
        stationRxQueryDao = daoSession.getStationDODao().queryBuilder().orderAsc(StationDODao.Properties.Num).rx();
    }

    public void deleteStation(StationDO stationDO, final StationManagerAdapter adapter) {
        stationRxDao.delete(stationDO)
                .flatMap(new Func1<Void, Observable<List<StationDO>>>() {
                    @Override
                    public Observable<List<StationDO>> call(Void aVoid) {
                        return stationRxQueryDao.list();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<StationDO>>() {
                    @Override
                    public void call(List<StationDO> stationDOs) {
                        adapter.setStations(stationDOs);
                    }
                });
    }

    public boolean isSameDay(long time1, long time2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time1Str = dateFormat.format(new Date(time1));
        String time2Str = dateFormat.format(new Date(time2));
        if (time1Str.equals(time2Str)) {
            return true;
        }
        return false;
    }

    public void addStation(String name, int position, Action1 action) {
        StationDO stationDO = new StationDO(null, name, position);
        stationRxDao.insert(stationDO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    public void updateStationList(Action1 action1) {
        stationRxQueryDao.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public void updateStation(StationDO stationDO, Action1 action1) {
        stationRxDao.update(stationDO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public void addMarkers(final Activity activity, final AMap aMap, final boolean draggable) {
        stationRxQueryDao.list()
                .flatMap(new Func1<List<StationDO>, Observable<StationDO>>() {
                    @Override
                    public Observable<StationDO> call(List<StationDO> stationDOs) {
                        return Observable.from(stationDOs);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StationDO>() {
                    @Override
                    public void call(StationDO station) {
                        if (station.getLatitude() != 0 && station.getLongitude() != 0) {
                            addMarker(activity, station, aMap, draggable);
                        }
                    }
                });
    }

    public List<StationDO> getStationList() {
        return daoSession.getStationDODao().queryBuilder().orderAsc(StationDODao.Properties.Num).list();
    }

    public Marker addMarker(Activity activity, StationDO stationDO, AMap aMap, boolean draggable) {
        LatLng latlngA = new LatLng(stationDO.getLatitude(), stationDO.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(stationDO.getName());
        markerOptions.position(latlngA);
        markerOptions.draggable(draggable);
        markerOptions.icon(BitmapDescriptorFactory.fromView(getMarkerView(activity, stationDO.getName())));
        Marker makerA = aMap.addMarker(markerOptions);
        makerA.setObject(stationDO);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngA, 15));
        return makerA;
    }

    public View getMarkerView(Activity activity, String title) {
        View view = activity.getLayoutInflater().inflate(R.layout.maker_view, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        ivIcon.setImageBitmap(bitmapDescriptor.getBitmap());
        tvTitle.setText(title);
        return view;
    }
}
