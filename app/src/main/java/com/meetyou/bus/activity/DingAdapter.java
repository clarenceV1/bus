package com.meetyou.bus.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.meetyou.bus.R;
import com.meetyou.bus.manager.DingManager;
import com.meetyou.bus.model.StationDO;

import java.util.List;

/**
 * Created by clarence on 2017/8/17.
 */

public class DingAdapter extends BaseAdapter {
    private List<StationDO> stationDOs;
    Context context;
    DingManager dingManager;

    public DingAdapter(Context context, DingManager dingManager) {
        this.context = context;
        this.dingManager = dingManager;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.ding_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final StationDO stationDO = getItem(position);
        holder.tvName.setText(stationDO.getName());
        holder.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dingManager.sendDing(context,stationDO);
            }
        });
        return convertView;
    }

    private class Holder {
        final TextView tvName;
        Button tvSend;

        private Holder(View view) {
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvSend = (Button) view.findViewById(R.id.tvSend);
        }
    }
}
