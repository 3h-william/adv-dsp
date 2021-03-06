package com.advdsp.service.manager.rest.model.statistic;

/**
 */
public class AdvStatisticModel {

    private String app_id;
    private String channel_id;
    private long click_nums;
    private long distinct_click_nums;
    private long activation_nums;
    private long distinct_activation_nums;
    private String ds;

    //mapping
    private String app_name;
    private String channel_name;

    // 点击转化，  点击/排重激活
    private String cvda;

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

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getCvda() {
        return cvda;
    }

    public void setCvda(String cvda) {
        this.cvda = cvda;
    }
}
