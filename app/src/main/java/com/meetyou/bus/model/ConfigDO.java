package com.meetyou.bus.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by clarence on 2017/8/17.
 */
@Entity
public class ConfigDO {
    @Id(autoincrement = true)
    private Long id;
    private String robotUrl;
    private int routeId;

    @Generated(hash = 518714672)
    public ConfigDO(Long id, String robotUrl, int routeId) {
        this.id = id;
        this.robotUrl = robotUrl;
        this.routeId = routeId;
    }

    @Generated(hash = 1153904181)
    public ConfigDO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRobotUrl() {
        return robotUrl;
    }

    public void setRobotUrl(String robotUrl) {
        this.robotUrl = robotUrl;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
