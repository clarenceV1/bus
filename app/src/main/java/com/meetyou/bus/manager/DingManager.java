package com.meetyou.bus.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.meetyou.bus.App;
import com.meetyou.bus.Constant;
import com.meetyou.bus.activity.DingAdapter;
import com.meetyou.bus.model.DaoSession;
import com.meetyou.bus.model.DingDingRobotAtModel;
import com.meetyou.bus.model.DingDingRobotModel;
import com.meetyou.bus.model.DingDingRobotTextModel;
import com.meetyou.bus.model.StationDO;
import com.meetyou.bus.model.StationDODao;
import com.meetyou.bus.model.UserDO;
import com.meetyou.bus.model.UserDODao;

import org.greenrobot.greendao.rx.RxQuery;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by clarence on 2017/8/17.
 */

public class DingManager {

    DaoSession daoSession;
    RxQuery<StationDO> stationRxQueryDao;

    public DingManager() {
        daoSession = App.getDaoSession();
        stationRxQueryDao = daoSession.getStationDODao().queryBuilder().orderAsc(StationDODao.Properties.Num).rx();

    }

    public void updateStationList(final DingAdapter adapter) {
        stationRxQueryDao.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<StationDO>>() {
                    @Override
                    public void call(List<StationDO> stationDOs) {
                        adapter.setStations(stationDOs);
                    }
                });
    }

    public void sendDing(final Context context, final StationDO stationDO) {
        RxQuery<UserDO> stationRxQuery = daoSession.getUserDODao().queryBuilder()
                .where(UserDODao.Properties.StationId.eq(stationDO.getId()))
                .orderAsc(UserDODao.Properties.Name).rx();

        stationRxQuery.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserDO>>() {
                    @Override
                    public void call(List<UserDO> userDOs) {
                        String[] at = null;
                        if (userDOs != null && userDOs.size() > 0) {
                            at = new String[userDOs.size()];
                            for (int i = 0; i < userDOs.size(); i++) {
                                at[i] = userDOs.get(i).getPhone();
                            }
                        }
                        String content = stationDO.getName() + "发车，请下一站同学准备";
                        sendDingDingRobot(context,content, at);
                    }
                });

    }

    public void sendDingDingRobot(Context context,String content, String[] at) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, "发送内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = getJson(content, at);
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.getRobotUrl())
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    public String getJson(String content, String[] at) {
        DingDingRobotModel model = new DingDingRobotModel();
        model.setMsgtype("text");

        DingDingRobotTextModel textModel = new DingDingRobotTextModel();
        textModel.setContent(content);
        model.setText(textModel);

        DingDingRobotAtModel atModel = new DingDingRobotAtModel();
        atModel.setAtAll(false);
        atModel.setAtMobiles(at);
        model.setAt(atModel);
        return JSON.toJSONString(model);
    }
}
