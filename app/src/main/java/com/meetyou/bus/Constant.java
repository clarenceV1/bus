package com.meetyou.bus;

/**
 * Created by clarence on 2017/8/17.
 */

public class Constant {
    public static String DINGDING_ROBOT_URL_ROUTE_6 = "https://oapi.dingtalk.com/robot/send?access_token=9b84d4cc6c07e22e72ee5cc4689322e41bad1949e14cd7478a6327250d867f9e";
    public static String DINGDING_ROBOT_URL_TEST = "https://oapi.dingtalk.com/robot/send?access_token=83b5894a4ca2af81d5d290833cb7391973d4d9077ff47309887d258cf6096a41";

    public static boolean isDebug = true;

    public static String GAODE_MAP_KE_NAME = "BusOnline";//高德地图key名称
    public static String GAODE_MAP_KE = "b1b235cff61f6e0a227fc7bba29c8822";//高德地图key
    public static String SHA1 = "6E:4C:21:1F:C5:A8:FD:B5:30:1E:58:EF:76:18:C8:EF:54:09:4C:20";

    public static String getRobotUrl() {
        if (isDebug) {
            return DINGDING_ROBOT_URL_TEST;
        }
        return DINGDING_ROBOT_URL_ROUTE_6;
    }

}
