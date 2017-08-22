package com.meetyou.bus.manager;

import com.meetyou.bus.App;
import com.meetyou.bus.activity.UserManagerAdapter;
import com.meetyou.bus.model.DaoSession;
import com.meetyou.bus.model.UserDO;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

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
                .flatMap(new Func1<Void, Observable<List<UserDO>>>() {
                    @Override
                    public Observable<List<UserDO>> call(Void aVoid) {
                        return userRxQueryDao.list();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserDO>>() {
                    @Override
                    public void call(List<UserDO> userDOs) {
                        adapter.setUsers(userDOs);
                    }
                });
    }

    public void addUser(String name, String phone, long stationId, String stationName, final UserManagerAdapter adapter) {
        UserDO userDO = new UserDO(null, name, phone, stationId, stationName);
        userRxDao.insert(userDO)
                .flatMap(new Func1<UserDO, Observable<List<UserDO>>>() {
                    @Override
                    public Observable<List<UserDO>> call(UserDO userDO) {
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserDO>>() {
                    @Override
                    public void call(List<UserDO> userDOs) {
                        adapter.setUsers(userDOs);
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
