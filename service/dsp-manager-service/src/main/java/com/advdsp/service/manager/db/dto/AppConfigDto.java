package com.advdsp.service.manager.db.dto;

import java.util.Date;

/**
 * App 配置
 */
public class AppConfigDto {

    private Integer app_id;
    private Integer customer_id;
    private String callback_url_base;
    private String system_id;
    private String app_name;
    private String app_describe;
    private Date last_edit_date;
    private String last_edit_user_id;
    private String last_edit_user_name;

    /**
     * mapping
     */
    private String customer_name;

    private String system_name;


    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(Integer app_id) {
        this.app_id = app_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCallback_url_base() {
        return callback_url_base;
    }

    public void setCallback_url_base(String callback_url_base) {
        this.callback_url_base = callback_url_base;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_describe() {
        return app_describe;
    }

    public void setApp_describe(String app_describe) {
        this.app_describe = app_describe;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getLast_edit_user_id() {
        return last_edit_user_id;
    }

    public void setLast_edit_user_id(String last_edit_user_id) {
        this.last_edit_user_id = last_edit_user_id;
    }

    public String getLast_edit_user_name() {
        return last_edit_user_name;
    }

    public void setLast_edit_user_name(String last_edit_user_name) {
        this.last_edit_user_name = last_edit_user_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }
}
