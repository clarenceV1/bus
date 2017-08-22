package com.meetyou.bus;

import android.app.Application;

import com.meetyou.bus.model.DaoMaster;
import com.meetyou.bus.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by clarence on 2017/8/17.
 */

public class App extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;

    private  static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession  getDaoSession() {
        return daoSession;
    }
}
