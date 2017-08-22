package com.meetyou.bus.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by clarence on 2017/8/17.
 */
@Entity
public class StationDO {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private long num;
    private double latitude;
    private double longitude;
    private long arriveTime;
    @Transient
    private boolean isSameDay;//是否是今天

    public StationDO(Long id, String name, long num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public StationDO() {
    }

    @Generated(hash = 1287631595)
    public StationDO(Long id, String name, long num, double latitude,
            double longitude, long arriveTime) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.latitude = latitude;
        this.longitude = longitude;
        this.arriveTime = arriveTime;
    }

    public boolean isSameDay() {
        return isSameDay;
    }

    public void setSameDay(boolean sameDay) {
        isSameDay = sameDay;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }
}
