package com.meetyou.bus;

import android.content.Context;
import android.util.Log;

import com.meetyou.bus.model.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by clarence on 2017/8/22.
 */

public class SQLiteOpenHelper extends DaoMaster.OpenHelper{

    public SQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);
        onCreate(db);
    }
}
