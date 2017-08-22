package com.meetyou.bus.manager;

import android.support.annotation.MainThread;
import android.widget.Adapter;

import com.meetyou.bus.App;
import com.meetyou.bus.activity.UserManagerAdapter;
import com.meetyou.bus.model.DaoSession;
import com.meetyou.bus.model.UserDO;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by clarence on 2017/8/17.
 */

public class UserManager {
    DaoSession daoSession;
    RxDao<UserDO, Long> userRxDao;
    RxQuery<UserDO> userRxQueryDao;

    public UserManager() {
        daoSession = App.getDaoSession();
        userRxDao = daoSession.getUserDODao().rx();
        userRxQueryDao = daoSession.getUserDODao().queryBuilder().rx();
    }

    public void deleteUser(UserDO userDO, final UserManagerAdapter adapter) {
        userRxDao.delete(userDO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        updateUserList(adapter);
                    }
                 });
    }

    public void addUser(String name, String phone, long stationId, String stationName,final UserManagerAdapter adapter) {
        UserDO userDO = new UserDO(null, name, phone,stationId,stationName);
        userRxDao.insert(userDO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserDO>() {
                    @Override
                    public void call(UserDO userDO) {
                        updateUserList(adapter);
                    }
                });
    }

    public void updateUserList(final UserManagerAdapter adapter) {
        userRxQueryDao.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserDO>>() {
                    @Override
                    public void call(List<UserDO> userDOs) {
                        adapter.setUsers(userDOs);
                    }
                });
    }
}
