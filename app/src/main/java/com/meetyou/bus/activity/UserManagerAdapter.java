package com.meetyou.bus.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.meetyou.bus.R;
import com.meetyou.bus.manager.UserManager;
import com.meetyou.bus.model.UserDO;

import java.util.List;

/**
 * Created by clarence on 2017/8/17.
 */

public class UserManagerAdapter extends BaseAdapter {
    private List<UserDO> users;
    UserManager userManager;
    Context context;

    public UserManagerAdapter(Context context, UserManager userManager) {
        this.context = context;
        this.userManager = userManager;
    }

    public void setUsers(List<UserDO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public UserDO getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_manager_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final UserDO userDO = getItem(position);
        holder.tvStation.setText(userDO.getStationName());
        holder.tvName.setText(userDO.getName());
        holder.tvDele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.deleteUser(userDO, UserManagerAdapter.this);
            }
        });
        return convertView;
    }

    private class Holder {
        final TextView tvName,tvStation;
        Button tvDele;

        private Holder(View view) {
            tvStation = (TextView) view.findViewById(R.id.tvStation);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDele = (Button) view.findViewById(R.id.tvDele);
        }
    }
}
