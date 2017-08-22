package com.meetyou.bus;

import android.Manifest;

/**
 * Created by clarence on 16/7/19.
 */
public enum PermissionEnum {
    EXTERNAL_LOCATION(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, R.string.EXTERNAL_PERMISSION_LOCATION),
    EXTERNAL_STORAGE(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, R.string.external_permission);

    public String[] permission;
    public int toast;

    PermissionEnum(String[] permission, int toast) {
        this.permission = permission;
        this.toast = toast;
    }
}
