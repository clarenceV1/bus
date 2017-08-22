package com.meetyou.bus.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by clarence on 2017/8/17.
 */
@Entity
public class UserDO {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String phone;
    private long stationId;
    private String stationName;

    @Generated(hash = 1870538266)
    public UserDO(Long id, String name, String phone, long stationId,
            String stationName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.stationId = stationId;
        this.stationName = stationName;
    }

    @Generated(hash = 604207703)
    public UserDO() {
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
