package com.advdsp.service.dsp.model;


import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.*;

@javax.xml.bind.annotation.XmlRootElement
@HBaseTable(name = "action_data",defaultFamily="base_info")
public class ActionDataModel {

    @HBaseRowkey
    private String rowkey;
    @HBaseColumn
    private String type;
    @HBaseColumn
    private String idfa;
    @HBaseColumn
    private String ip;
    @HBaseColumn
    private String callback;
    @HBaseColumn
    private String channel;
    @HBaseColumn
    private String clickKeyword;
    @HBaseColumn
    private String appid;
    /**
     * 服务器(Service)接收到数据的时间 timestamp
     */
    @HBaseColumn
    private String timestamp;

    @HBaseColumn
    private String click_timestamp;

    @HBaseColumn
    private String actionRowkey;


    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClickKeyword() {
        return clickKeyword;
    }

    public void setClickKeyword(String clickKeyword) {
        this.clickKeyword = clickKeyword;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getClick_timestamp() {
        return click_timestamp;
    }

    public void setClick_timestamp(String click_timestamp) {
        this.click_timestamp = click_timestamp;
    }

    public String getActionRowkey() {
        return actionRowkey;
    }

    public void setActionRowkey(String actionRowkey) {
        this.actionRowkey = actionRowkey;
    }

    @Override
    public String toString() {
        return "ActionDataModel{" +
                "rowkey='" + rowkey + '\'' +
                ", type='" + type + '\'' +
                ", idfa='" + idfa + '\'' +
                ", ip='" + ip + '\'' +
                ", callback='" + callback + '\'' +
                ", channel='" + channel + '\'' +
                ", clickKeyword='" + clickKeyword + '\'' +
                ", appid='" + appid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", click_timestamp='" + click_timestamp + '\'' +
                ", actionRowkey='" + actionRowkey + '\'' +
                '}';
    }
}