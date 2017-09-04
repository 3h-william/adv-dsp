package com.advdsp.service.dsp.model;

import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.HBaseColumn;
import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.HBaseRowkey;
import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.HBaseTable;

/**
 */
@HBaseTable(name = "counter", defaultFamily = "base_info")
public class CounterDataModel {

    @HBaseRowkey
    private String rowkey;
    /**
     * 产品ID
     */
    private String app_id;

    /**
     * 渠道id
     */
    private String channel_id;

    @HBaseColumn
    private long click_nums;
    @HBaseColumn
    private long distinct_click_nums;
    @HBaseColumn
    private long activation_nums;
    @HBaseColumn
    private long distinct_activation_nums;

    private String ds;

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public long getClick_nums() {
        return click_nums;
    }

    public void setClick_nums(long click_nums) {
        this.click_nums = click_nums;
    }

    public long getDistinct_click_nums() {
        return distinct_click_nums;
    }

    public void setDistinct_click_nums(long distinct_click_nums) {
        this.distinct_click_nums = distinct_click_nums;
    }

    public long getActivation_nums() {
        return activation_nums;
    }

    public void setActivation_nums(long activation_nums) {
        this.activation_nums = activation_nums;
    }

    public long getDistinct_activation_nums() {
        return distinct_activation_nums;
    }

    public void setDistinct_activation_nums(long distinct_activation_nums) {
        this.distinct_activation_nums = distinct_activation_nums;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }
}
