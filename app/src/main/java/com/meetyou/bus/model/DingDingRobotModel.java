package com.meetyou.bus.model;

/**
 * Created by clarence on 2017/8/17.
 */

public class DingDingRobotModel {
    private String msgtype = "text";
    private DingDingRobotTextModel text;
    private DingDingRobotAtModel at;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public DingDingRobotTextModel getText() {
        return text;
    }

    public void setText(DingDingRobotTextModel text) {
        this.text = text;
    }

    public DingDingRobotAtModel getAt() {
        return at;
    }

    public void setAt(DingDingRobotAtModel at) {
        this.at = at;
    }
}
