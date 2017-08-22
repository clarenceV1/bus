package com.meetyou.bus.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.meetyou.bus.R;
import com.meetyou.bus.manager.StationManager;
import com.meetyou.bus.model.StationDO;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by clarence on 2017/8/17.
 */

public class StationManagerAdapter extends BaseAdapter {
    private List<StationDO> stationDOs;
    StationManager stationManager;
    Context context;
    boolean isStationList = false;

    public StationManagerAdapter(Context context, StationManager stationManager) {
        this.context = context;
        this.stationManager = stationManager;
    }

    public void isStationList(boolean isStationList) {
        this.isStationList = isStationList;
    }

    public void setStations(List<StationDO> stationDOs) {
        this.stationDOs = stationDOs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stationDOs == null ? 0 : stationDOs.size();
    }

    @Override
    public StationDO getItem(int i) {
        return stationDOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.station_manager_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final StationDO stationDO = getItem(position);
        holder.tvNum.setText("车站" + stationDO.getNum());
        holder.tvName.setText(stationDO.getName());
        if (isStationList) {
            holder.tvDele.setVisibility(View.GONE);
            holder.tvReset.setVisibility(View.GONE);
            holder.tvNum.setVisibility(View.GONE);
        } else {
            holder.tvDele.setVisibility(View.VISIBLE);
            holder.tvReset.setVisibility(View.VISIBLE);
            holder.tvNum.setVisibility(View.VISIBLE);
        }
        holder.tvDele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stationManager.deleteStation(stationDO, StationManagerAdapter.this);
            }
        });
        holder.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Action1 action1 = new Action1() {
                    @Override
                    public void call(Object o) {
                        Toast.makeText(context, "重置成功", Toast.LENGTH_SHORT).show();
                    }
                };
                stationDO.setArriveTime(0);
                stationManager.updateStation(stationDO, action1);
            }
        });
        return convertView;
    }

    private class Holder {
        final TextView tvName, tvNum;
        Button tvDele, tvReset;

        private Holder(View view) {
            tvNum = (TextView) view.findViewById(R.id.tvNum);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDele = (Button) view.findViewById(R.id.tvDele);
            tvReset = (Button) view.findViewById(R.id.tvReset);
        }
    }
}
