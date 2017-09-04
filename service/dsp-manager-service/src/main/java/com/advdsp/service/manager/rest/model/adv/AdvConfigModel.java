package com.advdsp.service.manager.rest.model.adv;

import java.util.Date;

/**
 */
public class AdvConfigModel {

    private Integer adv_id;
    private Integer app_id;
    private Integer channel_id;
    private String adv_describe;

    /**
     * 关联
     */
    private String channel_name;
    private String app_name;
    private String customer_name;
    private String last_edit_user_name;
    private String callback;

    private Long last_edit_date;
    private String last_edit_user_id;

    public Integer getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(Integer adv_id) {
        this.adv_id = adv_id;
    }

    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(Integer app_id) {
        this.app_id = app_id;
    }

    public Integer getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
    }

    public String getAdv_describe() {
        return adv_describe;
    }

    public void setAdv_describe(String adv_describe) {
        this.adv_describe = adv_describe;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getLast_edit_user_name() {
        return last_edit_user_name;
    }

    public void setLast_edit_user_name(String last_edit_user_name) {
        this.last_edit_user_name = last_edit_user_name;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Long getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Long last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getLast_edit_user_id() {
        return last_edit_user_id;
    }

    public void setLast_edit_user_id(String last_edit_user_id) {
        this.last_edit_user_id = last_edit_user_id;
    }
}
