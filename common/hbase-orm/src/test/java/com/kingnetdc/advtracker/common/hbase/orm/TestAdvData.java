package com.kingnetdc.advtracker.common.hbase.orm;


import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.*;

/**
 * Created by william on 2017/5/23.
 */
@HBaseTable(name = "adv_data",defaultFamily="base_info")
public class TestAdvData {
    @HBaseRowkey
    private String rowkey;

    /**
     * base_info
     */
    @HBaseColumn
    private Long timestamp;
    @HBaseColumn
    private String referer;
    @HBaseColumn
    private String useragent;
    @HBaseColumn
    private String ip;
    @HBaseColumn
    private String client;
    @HBaseColumn
    private String xforwarded;
    @HBaseColumn
    private String hostname;
    @HBaseColumn
    private String uri;
    @HBaseColumn
    private String real_remote;
    @HBaseColumn
    private String adv_short_url;
    @HBaseColumn
    private String idfa;

    /**
     * 内部计算
     */
    @HBaseColumn
    private String real_ip;
    @HBaseColumn
    private String md5_idfa;
    // note md5_ip = md5(real_ip)
    @HBaseColumn
    private String md5_ip;
    @HBaseColumn
    private String channel_id;
    @HBaseColumn
    private String adv_id;
    @HBaseColumn
    private String app_id;


    public TestAdvData() {
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getXforwarded() {
        return xforwarded;
    }

    public void setXforwarded(String xforwarded) {
        this.xforwarded = xforwarded;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getReal_remote() {
        return real_remote;
    }

    public void setReal_remote(String real_remote) {
        this.real_remote = real_remote;
    }

    public String getAdv_short_url() {
        return adv_short_url;
    }

    public void setAdv_short_url(String adv_short_url) {
        this.adv_short_url = adv_short_url;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getReal_ip() {
        return real_ip;
    }

    public void setReal_ip(String real_ip) {
        this.real_ip = real_ip;
    }

    public String getMd5_idfa() {
        return md5_idfa;
    }

    public void setMd5_idfa(String md5_idfa) {
        this.md5_idfa = md5_idfa;
    }

    public String getMd5_ip() {
        return md5_ip;
    }

    public void setMd5_ip(String md5_ip) {
        this.md5_ip = md5_ip;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
}
